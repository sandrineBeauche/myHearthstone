package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.DataSetFormat;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.api.exporter.ExportDataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import javafx.util.Pair;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import javax.persistence.NoResultException;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@Disabled
@ExtendWith(DBUnitExtension.class)
public class DBFacadeTests {

    @TempDir
    protected File tempDir;

    protected DBFacade facade;

    protected DBManager db;

    protected DBInitializer initializer;

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();

    @BeforeEach
    public void beforeEach(){
        Injector injector = Guice.createInjector(new HearthstoneModuleDBTesting(this.tempDir));
        this.facade = injector.getInstance(DBFacade.class);
        this.db = injector.getInstance(DBManager.class);
        this.initializer = injector.getInstance(DBInitializer.class);
    }

    @Test
    //@Disabled
    @DataSet("emptyDataset.xml")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/allTables.xml")
    public void initDBTest() throws Exception{
        this.initializer.initDB();
    }

    @Test
    @DataSet(value = "initDBDataset.xml")
    public void getExisitingRarity() throws Exception {
        Rarity result = this.facade.getRarity("COMMON");
        assertNotNull(result);
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getNonExisitingRarity() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            Rarity result = this.facade.getRarity("BLABLA");
        });
    }


    @Test
    @DataSet("initDBDataset.xml")
    public void getExisitingCardClass() throws Exception {
        CardClass result = this.facade.getClasse("SHAMAN");
        assertNotNull(result);
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getNonExisitingCardClasse() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            CardClass result = this.facade.getClasse("BLABLA");
        });
    }


    @Test
    @DataSet("initDBDataset.xml")
    public void getExisitingCardSet() throws Exception {
        CardSet result = this.facade.getSet("NAXX");
        assertNotNull(result);
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getNonExisitingCardSet() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            CardSet result = this.facade.getSet("BLABLA");
        });
    }


    @Test
    @DataSet("initDBDataset.xml")
    public void getExisitingTag() throws Exception {
        CardTag result = this.facade.getTag("BATTLECRY");
        assertNotNull(result);
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getNonExisitingTag() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            CardTag result = this.facade.getTag("BLABLA");
        });
    }


    @Test
    @DataSet("initDBDataset.xml")
    public void getAvailableClasses(){
        List<CardClass> results = this.facade.getClasses(false);
        assertEquals(12, results.size());
        assertEquals("Chaman", results.get(0).getName());
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getAvailableClassesWithAll(){
        List<CardClass> results = this.facade.getClasses(true);
        assertEquals(13, results.size());
        assertEquals("ALL", results.get(0).getCode());
    }

    @Test
    @DataSet(value = "initDBDataset.xml")
    public void getAvailableCardSets(){
        List<CardSet> results = this.facade.getSets(false);
        assertEquals(30, results.size());
        assertEquals("ALTERAC_VALLEY", results.get(0).getCode());
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getAvailableCardSetsWithWild(){
        List<CardSet> results = this.facade.getSets(true);
        assertEquals(32, results.size());
        assertEquals("STANDARD", results.get(0).getCode());
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getAvailableRarities(){
        List<Rarity> results = this.facade.getRarities(false);
        assertEquals(5, results.size());
        assertEquals("FREE", results.get(0).getCode());
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getAvailableRaritiesWithAll(){
        List<Rarity> results = this.facade.getRarities(true);
        assertEquals(6, results.size());
        assertEquals("ALL", results.get(0).getCode());
    }


    @Test
    @DataSet("miniCatalogWithCollectionDataset.xml")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/allTables.xml")
    public void createNewDeckTest(){
        Session session = this.db.getSession();
        Hero hero = session.get(Hero.class, 1);

        Deck newDeck = this.facade.createDeck("deck1", hero);
        assertNotNull(newDeck);
    }

    @Test
    @Disabled
    @DataSet("collectionWithDeck2.xml")
    public void createNewDeck2Test(){
        Session session = this.db.getSession();
        Hero hero = session.get(Hero.class, 1);

        Deck newDeck = this.facade.createDeck("deck1", hero);
        assertNull(newDeck);
    }


    @Test
    @DataSet("collectionWithDeck1.xml")
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/allTables.xml")
    public void addCardToDeck1Test(){
        Session session = this.db.getSession();
        Deck deck = session.get(Deck.class, 2);
        boolean result1 = this.facade.addCardToDeck(66848, deck);
        this.db.closeSession();
        assertTrue(result1);

        Session session2 = this.db.getSession();
        Deck deck2 = session2.get(Deck.class, 2);
        assertEquals(1, deck2.getCards().size());
        assertEquals(1, deck2.getCards().get(0).getNbCards());

        boolean result2 = this.facade.addCardToDeck(66848, deck2);
        this.db.closeSession();
        assertTrue(result2);

        Session session3 = this.db.getSession();
        Deck deck3 = session3.get(Deck.class, 2);
        assertEquals(1, deck3.getCards().size());
        assertEquals(2, deck3.getCards().get(0).getNbCards());

        this.facade.addCardToDeck(66880, deck3);
    }

    @Test
    @DataSet("collectionWithDeck1.xml")
    public void addUnknownCardToDeck(){
        Session session = this.db.getSession();
        Deck deck = session.get(Deck.class, 2);
        boolean result = this.facade.addCardToDeck(90000, deck);
        this.db.closeSession();

        assertFalse(result);
    }

    @Test
    @DataSet("collectionWithDeck2.xml")
    @ExpectedDataSet(value="miniCatalogWithCollectionDataset.xml")
    public void deleteDeckTest(){
        boolean result = this.facade.deleteDeck(2);
        assertTrue(result);
    }

    @Test
    @DataSet("collectionWithDeck2.xml")
    public void duplicateDeckTest(){
        Deck anotherDeck = this.facade.duplicateDeck(2, "another deck");

        Session session = this.db.getSession();
        Deck d = session.get(Deck.class, anotherDeck.getId());

        assertNotNull(anotherDeck);
        assertEquals("another deck", d.getName());
        assertEquals(2, d.getCards().size());
        assertEquals(2, d.getCards().get(0).getNbCards());
        assertEquals(1, d.getCards().get(1).getNbCards());
        assertEquals("MAGE", d.getHero().getClasse().getCode());
    }


    @Test
    @DataSet("collectionWithDeck2.xml")
    public void removeCardFromDeckTest(){
        Session session = this.db.getSession();
        Deck deck = session.get(Deck.class, 2);

        boolean result = this.facade.removeCardFromDeck(66848, deck, false);
        assertTrue(result);
        assertEquals(1, deck.getCards().get(0).getNbCards());
    }

    @Test
    @DataSet("collectionWithDeck2.xml")
    public void removeCardFromDeckTest2(){
        Session session = this.db.getSession();
        Deck deck = session.get(Deck.class, 2);

        boolean result = this.facade.removeCardFromDeck(66880, deck, false);
        assertTrue(result);
        assertEquals(1, deck.getCards().size());
    }

    @Test
    @DataSet("collectionWithDeck2.xml")
    public void removeCardFromDeckTest3(){
        Session session = this.db.getSession();
        Deck deck = session.get(Deck.class, 2);

        boolean result = this.facade.removeCardFromDeck(66848, deck, true);
        assertTrue(result);
        assertEquals(1, deck.getCards().size());
        assertEquals(1, deck.getCards().get(0).getNbCards());
    }

    @Test
    @DataSet("collectionWithDeck2.xml")
    public void removeCardFromDeckTest4(){
        Session session = this.db.getSession();
        Deck deck = session.get(Deck.class, 2);

        boolean result = this.facade.removeCardFromDeck(60000, deck, false);
        assertFalse(result);
    }

    @Test
    @Disabled
    @DataSet("collectionWithDecks1.xml")
    public void getDecksList(){
        List<DeckListItem>  result = this.facade.getDeckList();

        assertNotNull(result);
        assertThat(result.toArray(new DeckListItem[0]),
                arrayContaining(allOf(hasProperty("name", equalTo("deck3")),
                                        hasProperty("nbCards", equalTo(0L))),

                                allOf(hasProperty("name", equalTo("deck1")),
                                        hasProperty("nbCards", equalTo(3L))),

                                allOf(hasProperty("name", equalTo("deck2")),
                                        hasProperty("nbCards", equalTo(4L)))
                ));
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    public void getDeckListItem(){
        DeckListItem result = this.facade.getDeckListItem(2);

        assertNotNull(result);
        assertEquals("deck1", result.getName());
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    public void getDeckCardsList(){
        Session session = this.db.getSession();
        Deck deck = session.get(Deck.class, 2);
        List<DeckCardListItem> result = this.facade.getDeckCardList(deck);

        assertNotNull(result);
        assertThat(result.toArray(new DeckCardListItem[0]),
                arrayContaining(
                        allOf(hasProperty("dbfId", equalTo(66880)),
                                hasProperty("nbCards", equalTo(1)),
                                hasProperty("classCode", equalTo("NEUTRAL"))),

                        allOf(hasProperty("dbfId", equalTo(66848)),
                                hasProperty("nbCards", equalTo(2)),
                                hasProperty("classCode", equalTo("MAGE")))
                ));
    }

    @Test
    @DataSet("collectionWithDecks1.xml")
    public void getDeckCardListItem(){
        Session session = this.db.getSession();
        Deck deck = session.get(Deck.class, 2);
        DeckCardListItem result = this.facade.getDeckCardListItem(deck, 66848);

        assertNotNull(result);
        assertEquals("Magistère Aubétreinte", result.getName());
    }


    @Test
    @DataSet("collectionWithDecks1.xml")
    public void getDeckCardsManaCurve(){
        Session session = this.db.getSession();
        Deck deck = session.get(Deck.class, 2);
        Integer[] result = this.facade.getManaCurveStats(deck);

        assertNotNull(result);
    }


    @Test
    @DataSet("collectionWithDecks1.xml")
    public void getDeckTagsStats(){
        Session session = this.db.getSession();
        Deck deck = session.get(Deck.class, 2);
        List<TagStat> result = this.facade.getTagsStats(deck);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @Disabled
    @DataSet("miniCatalogWithCollectionDataset.xml")
    public void deleteUserTag(){
        Session session = this.db.getSession();
        CardTag tag = session.get(CardTag.class, 170);

        boolean result = this.facade.deleteUserTag(tag);

        assertTrue(result);

        CardDetail card = session.get(CardDetail.class, 66848);
        assertEquals(2, card.getUserData().getTags().size());
    }

    @Test
    @DataSet("importedCatalogWithCollectionDataset.xml")
    public void getCardSetDetails(){
        Session session = this.db.getSession();
        List<CardSetDetail> result = this.facade.getCardSetDetailList();
        assertEquals(6, result.size());
    }

    @Test
    @DataSet("battleAccount.xml")
    public void getConnectedAccountTest(){
        Session session = this.db.getSession();
        BattleAccount account = this.facade.getConnectedAccount();
        assertThat(account, hasProperty("battleTag", equalTo("battleTag1")));
    }

    @Test
    @DataSet("battleAccount2.xml")
    public void getConnectedAccountTest2(){
        Session session = this.db.getSession();
        BattleAccount account = this.facade.getConnectedAccount();
        assertThat(account, nullValue());
    }

    @Test
    @DataSet("battleAccount.xml")
    public void getAccountsTest(){
        Session session = this.db.getSession();
        List<BattleAccount> accounts = this.facade.getAccounts();
        assertThat(accounts, hasSize(2));
    }
}
