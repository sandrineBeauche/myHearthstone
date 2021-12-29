package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModule;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleTesting;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DBManagerTests {

    @TempDir
    protected File tempDir;

    protected DBManager manager;

    @BeforeEach
    public void beforeEach(){
        Injector injector = Guice.createInjector(new HearthstoneModuleTesting(this.tempDir));
        this.manager = injector.getInstance(DBManager.class);
    }

    @Test
    public void saveToDatabaseTest() throws Exception {
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
        card.getCardClass().add(classe);
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
