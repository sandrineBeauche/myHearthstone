package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.sbm4j.hearthstone.myhearthstone.model.*;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;

import javax.persistence.NoResultException;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

public interface DBFacade {

    Rarity getRarity(String code) throws NoResultException;

    CardClass getClasse(String code) throws NoResultException;

    CardSet getSet(String code) throws NoResultException;

    CardTag getTag(String code);

    Hero getHero(String code) throws NoResultException;

    List<CardClass> getClasses(boolean includeAll);

    List<CardSet> getSets(boolean includeWild);

    List<CardCatalogItem> getCatalog(CatalogCriteria criteria);

    List<Rarity> getRarities(boolean includeAll);

    List<CardTag> getTags();

    List<Hero> getHeros();

    Deck createDeck(String name, Hero hero);

    boolean deleteDeck(int id);

    Deck duplicateDeck(int id, String name);

    boolean addCardToDeck(int dbfId, Deck deck);

    boolean removeCardFromDeck(int dbfId, Deck deck, boolean all);

    List<DeckListItem> getDeckList();

    List<DeckCardListItem> getDeckCardList(Deck deck);

    Integer[] getManaCurveStats(Deck deck);

    List<Pair<String, Integer>> getTagsStats(Deck deck);
}
