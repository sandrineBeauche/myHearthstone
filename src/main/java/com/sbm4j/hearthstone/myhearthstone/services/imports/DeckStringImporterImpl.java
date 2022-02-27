package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.Deck;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.model.Hero;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import de.saxsys.mvvmfx.utils.commands.Action;

import java.util.Arrays;
import java.util.Optional;

public class DeckStringImporterImpl extends Action implements DeckStringImporter{

    @Inject
    protected DBFacade dbFacade;

    protected Deck deck;

    protected DeckListItem deckListItem;

    protected String deckstring;

    protected String newName;

    @Override
    protected void action() throws Exception {
        String [] lines = this.deckstring.split("\n");
        Optional<String> opt = Arrays.stream(lines).filter(l -> !l.startsWith("#")).findFirst();

        if(opt.isPresent()) {
            Deckstrings.DeckStringDeck decoded = Deckstrings.decode(opt.get());
            Hero hero = this.dbFacade.getHero(decoded.heroes.get(0));
            this.deck = this.dbFacade.createDeck(this.newName, hero);

            for (Deckstrings.DeckStringCard current : decoded.cards) {
                this.dbFacade.addCardToDeck(current.getDbfId(), this.deck, current.getCount());
            }

            this.deckListItem = this.dbFacade.getDeckListItem(this.deck.getId());
        }
    }

    public DeckListItem importDeckString(String deckstring, String deckName) throws Exception {
        this.deckstring = deckstring;
        this.newName = deckName;
        this.action();
        return this.deckListItem;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }


    public DeckListItem getDeckListItem() {
        return deckListItem;
    }

    public void setDeckListItem(DeckListItem deckListItem) {
        this.deckListItem = deckListItem;
    }

    public String getDeckstring() {
        return deckstring;
    }

    public void setDeckstring(String deckstring) {
        this.deckstring = deckstring;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
