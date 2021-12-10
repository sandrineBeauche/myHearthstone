package com.sbm4j.hearthstone.myhearthstone.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sbm4j.hearthstone.myhearthstone.model.*;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class GameDataImporterTests {

    @Test
    public void importJsonTest() throws URISyntaxException, FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("cards.json").getFile());
        FileReader reader = new FileReader(file);
        JSONCardImporter importer = new JSONCardImporter();
        ArrayList<CardDetail> result = importer.importCards(reader);

        assertEquals(5, result.size());
    }


    @Test
    public void saveToDatabaseTest() throws Exception {
        DBManagerTesting manager = new DBManagerTesting();
        Session session = manager.createSession();

        CardSet cardSet = new CardSet();
        cardSet.setCode("EXT1");
        cardSet.setName("Première extension");

        CardClass classe  = new CardClass();
        classe.setCode("HUNTER");
        classe.setName("Chasseur");

        Rarity rarity = new Rarity();
        rarity.setCost(40);
        rarity.setCostGold(160);
        rarity.setGain(10);
        rarity.setGainGold(40);
        rarity.setCode("COMMON");
        rarity.setName("Commune");

        CardTag tag = new CardTag();
        tag.setUser(true);
        tag.setCode("TAG");
        tag.setExclusiveGroup(0);
        tag.setName("tag1");

        CardDetail card = new CardDetail();
        card.setArtist("sandrine");
        card.setAttack(3);
        card.setCost(6);
        card.setDbfId(123);
        card.setDurability(2);
        card.setFlavor("coucou");
        card.setId("ESSAI_01");
        card.setName("essai");
        card.setText("ceci est un essai");
        card.setCardSet(cardSet);
        card.setCardClass(classe);
        card.setRarity(rarity);
        card.getTags().add(tag);

        session.beginTransaction();
        session.save(cardSet);
        session.save(classe);
        session.save(rarity);
        session.save(tag);
        session.save(card);
        session.getTransaction().commit();
        session.close();
    }
}
