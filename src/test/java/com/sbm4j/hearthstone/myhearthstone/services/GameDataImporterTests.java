package com.sbm4j.hearthstone.myhearthstone.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonCard;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

public class GameDataImporterTests {

    @Test
    public void importJsonTest() throws URISyntaxException, IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("cards.json").getFile());
        FileReader reader = new FileReader(file);
        JSONCardImporter importer = new JSONCardImporter();
        ArrayList<JsonCard> result = importer.importCards(reader, true);

        assertEquals(10, result.size());
    }


    @Test
    public void saveToDatabaseTest() throws Exception {
        DBManagerTesting manager = new DBManagerTesting();
        Session session = manager.getSession();

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

    //@Test
    //public void importCards(@TempDir File tmpDir){
    //
    //}

    @Test
    public void getExisitingRarity() throws Exception {
        DBManagerTesting manager = new DBManagerTesting();
        Session session = manager.getSession();

        Rarity rarity = new Rarity();
        rarity.setCode("COMMON");
        rarity.setName("Commune");

        session.beginTransaction();
        session.save(rarity);
        session.getTransaction().commit();
        manager.closeSession();

        Rarity result = manager.getRarity("COMMON");
        assertNotNull(result);
    }

    @Test
    public void getNonExisitingRarity() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            DBManagerTesting manager = new DBManagerTesting();
            Rarity result = manager.getRarity("COMMON");
        });
    }


    @Test
    public void getExisitingCardClass() throws Exception {
        DBManagerTesting manager = new DBManagerTesting();
        Session session = manager.getSession();

        CardClass classe = new CardClass();
        classe.setCode("SHAMAN");
        classe.setName("Chaman");

        session.beginTransaction();
        session.save(classe);
        session.getTransaction().commit();
        manager.closeSession();

        CardClass result = manager.getClasse("SHAMAN");
        assertNotNull(result);
    }

    @Test
    public void getNonExisitingCardClasse() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            DBManagerTesting manager = new DBManagerTesting();
            CardClass result = manager.getClasse("SHAMAN");
        });
    }


    @Test
    public void getExisitingCardSet() throws Exception {
        DBManagerTesting manager = new DBManagerTesting();
        Session session = manager.getSession();

        CardSet cardSet = new CardSet();
        cardSet.setCode("NAXX");
        cardSet.setName("Naxxramas");

        session.beginTransaction();
        session.save(cardSet);
        session.getTransaction().commit();
        manager.closeSession();

        CardSet result = manager.getSet("NAXX");
        assertNotNull(result);
    }

    @Test
    public void getNonExisitingCardSet() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            DBManagerTesting manager = new DBManagerTesting();
            CardSet result = manager.getSet("NAXX");
        });
    }


    @Test
    public void getExisitingTag() throws Exception {
        DBManagerTesting manager = new DBManagerTesting();
        Session session = manager.getSession();

        CardTag tag = new CardTag();
        tag.setCode("BATTLECRY");
        tag.setName("Cri de guerre");

        session.beginTransaction();
        session.save(tag);
        session.getTransaction().commit();
        manager.closeSession();

        CardTag result = manager.getTag("BATTLECRY");
        assertNotNull(result);
    }

    @Test
    public void getNonExisitingTag() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            DBManagerTesting manager = new DBManagerTesting();
            CardTag result = manager.getTag("BATTLECRY");
        });
    }

    @Test
    public void initDBTest() throws Exception{
        DBManagerTesting manager = new DBManagerTesting();
        manager.initDB();
    }

    @Test
    public void verifyTagsTest() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("cards.collectible.json").getFile());

        DBManagerTesting manager = new DBManagerTesting();
        manager.initDB();

        HashSet<String> result = manager.verifyTags(file);
    }
}
