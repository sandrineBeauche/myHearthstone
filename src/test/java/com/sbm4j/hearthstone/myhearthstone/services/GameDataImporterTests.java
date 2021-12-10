package com.sbm4j.hearthstone.myhearthstone.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sbm4j.hearthstone.myhearthstone.model.CardDetail;
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
        DBManager manager = new DBManager();
        Session session = manager.createSession();

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

        session.beginTransaction();
        session.save(card);
        session.getTransaction().commit();
        session.close();
    }
}
