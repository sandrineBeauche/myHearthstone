package com.sbm4j.hearthstone.myhearthstone.services.imports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleTesting;
import com.sbm4j.hearthstone.myhearthstone.model.CardDetail;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonCard;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;

public class GameDataImporterTests {

    protected File jsonCardsFile;

    protected DBManager dbManager;

    protected ImportCatalogAction importer;

    @TempDir
    protected File tempDir;

    @BeforeEach
    public void beforeEach() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        this.jsonCardsFile= new File(classLoader.getResource("cards.json").getFile());

        Injector injector = Guice.createInjector(
                new HearthstoneModuleTesting(this.tempDir, true));

        this.dbManager = injector.getInstance(DBManager.class);
        this.importer = injector.getInstance(ImportCatalogAction.class);
    }

    @AfterEach
    public void afterEach(){
        this.dbManager.closeSession();
    }

    @Test
    public void parseJsonTest() throws IOException {
        ArrayList<JsonCard> result = this.importer.parseCards(this.jsonCardsFile);

        assertEquals(10, result.size());
    }


    @Test
    public void verifyTagsTest() throws Exception {
        HashSet<String> result = this.importer.verifyTags(this.jsonCardsFile);
        assertEquals(0, result.size());
    }

    @Test
    public void getCardStatus_New() throws Exception {
        ArrayList<JsonCard> jsonCards = this.importer.parseCards(this.jsonCardsFile);
        JSONCardImporter.CardStatus status = this.importer.cardDetailStatus(jsonCards.get(0));

        Assertions.assertEquals(JSONCardImporter.CardStatus.NEW_CARD, status);
    }


    @Test
    public void getCardStatus_UptoDate() throws Exception {
        JsonCard jsonCard = this.importer.parseCards(this.jsonCardsFile).get(0);

        this.importer.addCardDetail(jsonCard);

        JSONCardImporter.CardStatus status = this.importer.cardDetailStatus(jsonCard);

        Assertions.assertEquals(JSONCardImporter.CardStatus.UPTODATE_CARD, status);
    }

    @Test
    public void getCardStatus_Modified() throws Exception {
        JsonCard jsonCard = this.importer.parseCards(this.jsonCardsFile).get(0);

        this.importer.addCardDetail(jsonCard);

        jsonCard.setJsonDesc("another md5");

        JSONCardImporter.CardStatus status = this.importer.cardDetailStatus(jsonCard);

        Assertions.assertEquals(JSONCardImporter.CardStatus.MODIFIED_CARD, status);
    }


    @Test
    public void addNewCard() throws IOException {
        JsonCard jsonCard = this.importer.parseCards(this.jsonCardsFile).get(1);

        this.importer.addCardDetail(jsonCard);
        this.dbManager.closeSession();

        CardDetail card = this.dbManager.getSession().get(CardDetail.class, jsonCard.getDbfId());
        assertNotNull(card);
        assertEquals(3, card.getTags().size());
    }


    @Test
    public void updateCard() throws IOException {
        JsonCard jsonCard = this.importer.parseCards(this.jsonCardsFile).get(1);

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
        assertEquals(4, card.getTags().size());
        assertEquals("PALADIN", card.getCardClass().get(0).getCode());
    }


    @Test
    public void importNewCards() throws IOException {
        this.importer.importCards(this.jsonCardsFile);
        this.importer.importCards(this.jsonCardsFile);
    }

}