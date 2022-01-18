package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.views.ManaOption;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(DBUnitExtension.class)
public class DBFacadeCatalogTest {

    protected DBFacade facade;

    protected DBManager db;

    protected CardSet wildSet = new CardSet(0, "WILD", "Libre", false, -1);

    protected CardSet standardSet = new CardSet(0, "STANDARD", "Standard", true, -1);

    protected Rarity rarityAll = new Rarity(0, "ALL", "Tout");

    protected ManaOption manaAll = new ManaOption("ALL", "Tout", -1);

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();


    @BeforeEach
    public void beforeEach() {
        Injector injector = Guice.createInjector(new HearthstoneModuleDBTesting(null, false));
        this.facade = injector.getInstance(DBFacade.class);
        this.db = injector.getInstance(DBManager.class);

    }

    @Test
    @DataSet("importedCatalogWithCollectionDataset.xml")
    public void getCatalogMageItemsTest(){
        CardClass cardClass = this.facade.getClasse("MAGE");
        CatalogCriteria criteria = new CatalogCriteria(cardClass, wildSet, rarityAll, manaAll, false, null);
        List<CardCatalogItem> results = this.facade.getCatalog(criteria);

        assertNotNull(results);
        assertEquals(21, results.size());
        Session session = this.db.getSession();
        results.forEach(current -> {
            int id = current.dbfId();
            CardDetail card = session.get(CardDetail.class, id);
            List<CardClass> classes = card.getCardClass();
            if(classes.size() == 1){
                assertEquals("MAGE", classes.get(0).getCode());
            }
        });
    }

    @Test
    @DataSet("importedCatalogWithCollectionDataset.xml")
    public void getCatalogAlteracItemTest(){
        CardClass cardClass = this.facade.getClasse("MAGE");
        CardSet cardSet = this.facade.getSet("ALTERAC_VALLEY");
        CatalogCriteria criteria = new CatalogCriteria(cardClass, cardSet, rarityAll, manaAll, false, null);
        List<CardCatalogItem> results = this.facade.getCatalog(criteria);

        assertNotNull(results);
        assertEquals(6, results.size());
        Session session = this.db.getSession();
        results.forEach(current -> {
            int id = current.dbfId();
            CardDetail card = session.get(CardDetail.class, id);
            List<CardClass> classes = card.getCardClass();
            if(classes.size() == 1){
                assertEquals("MAGE", classes.get(0).getCode());
            }
            assertEquals("ALTERAC_VALLEY", card.getCardSet().getCode());
        });
    }

    @Test
    @DataSet("importedCatalogWithCollectionDataset.xml")
    public void getCatalogStandardItemTest(){
        CardClass cardClass = this.facade.getClasse("MAGE");
        CatalogCriteria criteria = new CatalogCriteria(cardClass, standardSet, rarityAll, manaAll, false, null);
        List<CardCatalogItem> results = this.facade.getCatalog(criteria);

        assertNotNull(results);
        assertEquals(15, results.size());
        Session session = this.db.getSession();
        results.forEach(current -> {
           int id = current.dbfId();
            CardDetail card = session.get(CardDetail.class, id);
            List<CardClass> classes = card.getCardClass();
            if(classes.size() == 1){
                assertEquals("MAGE", classes.get(0).getCode());
            }
            assertEquals(true, card.getCardSet().isStandard());
        });
    }


    @Test
    @DataSet("importedCatalogWithCollectionDataset.xml")
    public void getCatalogCommonItemsTest(){
        CardClass cardClass = this.facade.getClasse("MAGE");
        Rarity rarity = this.facade.getRarity("COMMON");
        CatalogCriteria criteria = new CatalogCriteria(cardClass, wildSet, rarity, manaAll, false, null);
        List<CardCatalogItem> results = this.facade.getCatalog(criteria);

        assertNotNull(results);
        assertEquals(8, results.size());
        Session session = this.db.getSession();
        results.forEach(current -> {
            int id = current.dbfId();
            CardDetail card = session.get(CardDetail.class, id);
            List<CardClass> classes = card.getCardClass();
            if(classes.size() == 1){
                assertEquals("MAGE", classes.get(0).getCode());
            }
            assertEquals("COMMON", card.getRarity().getCode());
        });
    }

    @Test
    @DataSet("importedCatalogWithCollectionDataset.xml")
    public void getCatalogMana3ItemsTest(){
        CardClass cardClass = this.facade.getClasse("MAGE");
        ManaOption mana = new ManaOption("3", "3", 3);
        CatalogCriteria criteria = new CatalogCriteria(cardClass, wildSet, rarityAll, mana, false, null);
        List<CardCatalogItem> results = this.facade.getCatalog(criteria);

        assertNotNull(results);
        assertEquals(4, results.size());
        Session session = this.db.getSession();
        results.forEach(current -> {
            int id = current.dbfId();
            CardDetail card = session.get(CardDetail.class, id);
            List<CardClass> classes = card.getCardClass();
            if(classes.size() == 1){
                assertEquals("MAGE", classes.get(0).getCode());
            }
            assertEquals(3, card.getCost());
        });
    }

    @Test
    @DataSet("importedCatalogWithCollectionDataset.xml")
    public void getCatalogMana7ItemsTest(){
        CardClass cardClass = this.facade.getClasse("MAGE");
        ManaOption mana = new ManaOption("7+", "7+", 7);
        CatalogCriteria criteria = new CatalogCriteria(cardClass, wildSet, rarityAll, mana, false, null);
        List<CardCatalogItem> results = this.facade.getCatalog(criteria);

        assertNotNull(results);
        //assertEquals(4, results.size());
        Session session = this.db.getSession();
        results.forEach(current -> {
            int id = current.dbfId();
            CardDetail card = session.get(CardDetail.class, id);
            List<CardClass> classes = card.getCardClass();
            if(classes.size() == 1){
                assertEquals("MAGE", classes.get(0).getCode());
            }
            assertTrue(card.getCost() >= 7);
        });
    }


    @Test
    @DataSet("importedCatalogWithCollectionDataset.xml")
    public void getCatalogInCollectionItemsTest(){
        CardClass cardClass = this.facade.getClasse("MAGE");
        CatalogCriteria criteria = new CatalogCriteria(cardClass, wildSet, rarityAll, manaAll, true, null);
        List<CardCatalogItem> results = this.facade.getCatalog(criteria);

        assertNotNull(results);
        assertEquals(16, results.size());
        Session session = this.db.getSession();
        results.forEach(current -> {
            int id = current.dbfId();
            CardDetail card = session.get(CardDetail.class, id);
            List<CardClass> classes = card.getCardClass();
            if(classes.size() == 1){
                assertEquals("MAGE", classes.get(0).getCode());
            }
            assertTrue(card.getUserData().getNbTotalCards() > 0);
        });
    }


    @Test
    @DataSet("importedCatalogWithCollectionDataset.xml")
    public void getCatalogTagsItemsTest(){
        CardClass cardClass = this.facade.getClasse("MAGE");
        List<CardTag> tags = new ArrayList<CardTag>();
        tags.add(this.facade.getTag("SPELL"));
        CatalogCriteria criteria = new CatalogCriteria(cardClass, wildSet, rarityAll, manaAll, false, tags);
        List<CardCatalogItem> results = this.facade.getCatalog(criteria);

        assertNotNull(results);
        assertEquals(10, results.size());
        Session session = this.db.getSession();
        results.forEach(current -> {
            int id = current.dbfId();
            CardDetail card = session.get(CardDetail.class, id);
            List<CardClass> classes = card.getCardClass();
            if(classes.size() == 1){
                assertEquals("MAGE", classes.get(0).getCode());
            }

        });
    }
}
