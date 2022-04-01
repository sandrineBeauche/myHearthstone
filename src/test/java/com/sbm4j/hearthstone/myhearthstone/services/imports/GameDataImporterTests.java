package com.sbm4j.hearthstone.myhearthstone.services.imports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.DataSetFormat;
import com.github.database.rider.core.api.exporter.ExportDataSet;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleDBTesting;
import com.sbm4j.hearthstone.myhearthstone.model.CardDetail;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonCard;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;


@ExtendWith(DBUnitExtension.class)
public class GameDataImporterTests {

    protected File jsonCardsFile;

    protected DBManager dbManager;

    protected ImportCatalogAction importer;

    private ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance("pu-hearthstone").connection();

    @TempDir
    protected File tempDir;

    @BeforeEach
    public void beforeEach() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        this.jsonCardsFile = new File(classLoader.getResource("cards.json").getFile());

        Injector injector = Guice.createInjector(
                new HearthstoneModuleDBTesting(this.tempDir));

        this.dbManager = injector.getInstance(DBManager.class);
        this.importer = injector.getInstance(ImportCatalogAction.class);
    }

    @AfterEach
    public void afterEach(){
        this.dbManager.closeSession();
    }


    @Test
    public void downloadJsonFile() throws IOException {
        File jsonFile = this.importer.getJsonFile();
        assertNotNull(jsonFile);
    }

    @Test
    public void parseJsonTest() throws IOException {
        ArrayList<JsonCard> result = this.importer.parseCards(this.jsonCardsFile);

        assertEquals(40, result.size());
    }


    @Test
    public void verifyTagsTest() throws Exception {
        ArrayList<JsonCard> cards = this.importer.parseCards(this.jsonCardsFile);
        HashSet<String> result = this.importer.verifyTags(cards);
        assertEquals(0, result.size());
    }

    @Test
    public void getCardStatus_New() throws Exception {
        ArrayList<JsonCard> jsonCards = this.importer.parseCards(this.jsonCardsFile);
        JSONCardImporter.CardStatus status = this.importer.cardDetailStatus(jsonCards.get(0));

        Assertions.assertEquals(JSONCardImporter.CardStatus.NEW_CARD, status);
    }


    @Test
    @Disabled
    public void getCardStatus_UptoDate() throws Exception {
        JsonCard jsonCard = this.importer.parseCards(this.jsonCardsFile).get(0);

        this.importer.addCardDetail(jsonCard);

        JSONCardImporter.CardStatus status = this.importer.cardDetailStatus(jsonCard);

        Assertions.assertEquals(JSONCardImporter.CardStatus.UPTODATE_CARD, status);
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void getCardStatus_Modified() throws Exception {
        JsonCard jsonCard = this.importer.parseCards(this.jsonCardsFile).get(0);

        this.importer.addCardDetail(jsonCard);

        jsonCard.setJsonDesc("another md5");

        JSONCardImporter.CardStatus status = this.importer.cardDetailStatus(jsonCard);

        Assertions.assertEquals(JSONCardImporter.CardStatus.MODIFIED_CARD, status);
    }


    @Test
    @DataSet("initDBDataset.xml")
    public void addNewCard() throws IOException {
        JsonCard jsonCard = this.importer.parseCards(this.jsonCardsFile).get(1);

        this.importer.addCardDetail(jsonCard);
        this.dbManager.closeSession();

        CardDetail card = this.dbManager.getSession().get(CardDetail.class, jsonCard.getDbfId());
        assertNotNull(card);
        assertEquals(2, card.getUserData().getTags().size());
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void addNewDoubleClassCard() throws IOException {
        JsonCard jsonCard = this.importer.parseCards(this.jsonCardsFile).get(24);

        this.importer.addCardDetail(jsonCard);
        this.dbManager.closeSession();

        CardDetail card = this.dbManager.getSession().get(CardDetail.class, jsonCard.getDbfId());
        assertNotNull(card);
        assertEquals(2, card.getCardClass().size());
    }


    @Test
    public void updateCard() throws IOException {
        JsonCard jsonCard = this.importer.parseCards(this.jsonCardsFile).get(0);

        this.importer.addCardDetail(jsonCard);
        this.dbManager.closeSession();

        jsonCard.setJsonDesc("another md5");
        jsonCard.setType("MINION");
        jsonCard.setCardClass("PALADIN");
        jsonCard.setSpellSchool(null);
        jsonCard.setRace("HUMAN");
        jsonCard.getMechanics().clear();
        jsonCard.getMechanics().add("TAUNT");
        jsonCard.getMechanics().add("BATTLECRY");
        jsonCard.setAttack(3);
        jsonCard.setHealth(5);

        this.importer.updateCardDetail(jsonCard);
        this.dbManager.closeSession();

        CardDetail card = this.dbManager.getSession().get(CardDetail.class, jsonCard.getDbfId());
        assertNotNull(card);
        assertEquals(4, card.getUserData().getTags().size());
        assertEquals("PALADIN", card.getCardClass().get(0).getCode());
    }


    @Test
    //@ExportDataSet(format = DataSetFormat.XML,outputName="target/exported/xml/allTables.xml")
    public void importNewCards() throws IOException {
        this.importer.importCards(this.jsonCardsFile);
        this.importer.importCards(this.jsonCardsFile);
    }

    @Test
    @DataSet("initDBDataset.xml")
    public void performImport() throws Exception {
        JSONCardImporter action = (JSONCardImporter) this.importer;
        action.action();

        assert(action.getReport().errors.isEmpty());
        assert(action.getReport().globalErrors.isEmpty());
    }
}
