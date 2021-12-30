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
import static org.junit.jupiter.api.Assertions.assertNull;

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

        CardUserData userData = new CardUserData();
        userData.setNbCards(1);
        userData.setNbGolden(0);
        userData.setNbTotalCards(1);
        userData.getTags().add(tag);

        card.setUserData(userData);

        session.beginTransaction();
        session.save(userData);
        session.save(cardSet);
        session.save(classe);
        session.save(rarity);
        session.save(tag);
        session.save(card);
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void detail2UserRelationTest(){
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

        CardDetail card = new CardDetail();
        card.setDbfId(123);
        card.setId("ESSAI_01");
        card.setName("essai");
        card.setCardSet(cardSet);
        card.getCardClass().add(classe);
        card.setRarity(rarity);

        CardUserData data = new CardUserData();
        data.setDbfId(123);
        card.setUserData(data);

        session.beginTransaction();
        session.save(cardSet);
        session.save(classe);
        session.save(rarity);
        session.save(card);
        session.save(data);
        session.getTransaction().commit();
        this.manager.closeSession();;

        Session anotherSession = this.manager.getSession();
        CardDetail c2 = anotherSession.get(CardDetail.class, 123);
        assertNotNull(c2);
        assertNotNull(c2.getUserData());
        anotherSession.beginTransaction();
        anotherSession.delete(c2);
        anotherSession.getTransaction().commit();
        this.manager.closeSession();

        Session anotherSession2 = this.manager.getSession();
        CardDetail details = anotherSession2.get(CardDetail.class, 123);
        CardUserData d = anotherSession2.get(CardUserData.class, 123);

        assertNull(details);
        assertNotNull(d);
    }

}
