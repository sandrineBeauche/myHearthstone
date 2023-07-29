package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.sbm4j.hearthstone.myhearthstone.model.*;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;

import javax.persistence.NoResultException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface DBFacade {

    Rarity getRarity(String code) throws NoResultException;

    CardClass getClasse(String code) throws NoResultException;

    CardSet getSet(String code) throws NoResultException;

    CardTag getTag(String code);

    Hero getHero(String code) throws NoResultException;

    CardDetail getCardFromDbfId(int dbfId);

    List<CardClass> getClasses(boolean includeAll);

    List<CardSet> getSets(boolean includeWild);

    List<CardCatalogItem> getCatalog(CatalogCriteria criteria);

    List<Rarity> getRarities(boolean includeAll);

    List<CardTag> getTags();

    List<CardTag> getAvailableUserTags();

    List<CardTag> getUserTags(CardDetail card);

    CardTag createUserTag(String name);

    boolean deleteUserTag(CardTag tag);

    boolean addUserTagToCard(CardTag tag, CardDetail card);

    boolean removeUserTagFromCard(CardTag tag, CardDetail card);

    List<Hero> getHeros();

    Hero getHero(int dbfId);

    Deck createDeck(String name, Hero hero);

    boolean deleteDeck(int id);

    Deck duplicateDeck(int id, String name);

    boolean addCardToDeck(int dbfId, Deck deck);

    boolean addCardToDeck(int dbfId, Deck deck, int count);

    boolean removeCardFromDeck(int dbfId, Deck deck, boolean all);

    List<DeckListItem> getDeckList();

    DeckListItem getDeckListItem(int deckId);

    List<DeckCardListItem> getDeckCardList(int deckId);

    List<DeckCardListItem> getDeckCardList(Deck deck);

    DeckCardListItem getDeckCardListItem(Deck deck, int dbfId);

    Integer[] getManaCurveStats(Deck deck);

    List<TagStat> getTagsStats(Deck deck);

    List<TypeTagStat> getTypeTagsStats(Deck deck);

    List<CardTag> getTypeTagsfromCard(int dbfId);

    void updateDB() throws IOException;

    List<CardSetDetail> getCardSetDetailList();
}
