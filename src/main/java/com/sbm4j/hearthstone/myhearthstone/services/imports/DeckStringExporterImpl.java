package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.DeckCardListItem;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import de.saxsys.mvvmfx.utils.commands.Action;

import java.util.List;

public class DeckStringExporterImpl extends Action implements DeckStringExporter {

    @Inject
    protected DBFacade dbFacade;

    protected DeckListItem deckListItem;

    protected String deckstring;

    @Override
    protected void action() throws Exception {
        Deckstrings.DeckStringDeck result = new Deckstrings.DeckStringDeck();

        if(this.deckListItem.isStandard()){
            result.format = Deckstrings.FT_STANDARD;
        }
        else{
            result.format = Deckstrings.FT_WILD;
        }

        result.heroes = List.of(this.dbFacade.getHero(this.deckListItem.getHeroCode()).getDbfId());

        List<DeckCardListItem> items = this.dbFacade.getDeckCardList(this.deckListItem.getDeckId());
        result.cards = items.stream()
                .map(c -> new Deckstrings.DeckStringCard(c.getDbfId(), c.getNbCards()))
                .toList();

        this.deckstring = Deckstrings.encode(result);
    }

    @Override
    public String export(DeckListItem deck) throws Exception {
        this.deckListItem = deck;
        this.action();
        return this.deckstring;
    }
}
