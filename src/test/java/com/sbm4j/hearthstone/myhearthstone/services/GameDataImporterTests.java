package com.sbm4j.hearthstone.myhearthstone.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sbm4j.hearthstone.myhearthstone.model.json.JsonCard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

public class GameDataImporterTests {

    protected File jsonCardsFile;

    protected DBManagerTesting dbManager;

    protected JSONCardImporter importer;

    @BeforeEach
    public void beforeEach() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        this.jsonCardsFile= new File(classLoader.getResource("cards.json").getFile());

        this.dbManager = new DBManagerTesting();
        this.dbManager.initDB();

        this.importer = new JSONCardImporter(this.dbManager, null);
    }

    @AfterEach
    public void afterEach(){
        this.dbManager.closeSession();
    }

    @Test
    public void parseJsonTest() throws URISyntaxException, IOException {
        ArrayList<JsonCard> result = this.importer.parseCards(this.jsonCardsFile);

        assertEquals(10, result.size());
    }


    @Test
    public void verifyTagsTest() throws Exception {
        HashSet<String> result = this.importer.verifyTags(this.jsonCardsFile);
    }

    @Test
    public void getCardStatusTest() throws Exception {
        ArrayList<JsonCard> jsonCards = this.importer.parseCards(this.jsonCardsFile);
        JSONCardImporter.CardStatus status = this.importer.cardDetailStatus(jsonCards.get(0));

        Assertions.assertEquals(JSONCardImporter.CardStatus.NEW_CARD, status);
    }

    @Test
    public void importNewCards() throws IOException {
        this.importer.importCards(this.jsonCardsFile);
    }
}
