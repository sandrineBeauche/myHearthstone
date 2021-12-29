package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModule;
import com.sbm4j.hearthstone.myhearthstone.HearthstoneModuleTesting;
import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.persistence.NoResultException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DBFacadeTests {

    @TempDir
    protected File tempDir;

    protected DBFacade facade;

    protected DBManager db;

    @BeforeEach
    public void beforeEach(){
        Injector injector = Guice.createInjector(new HearthstoneModuleTesting(this.tempDir));
        this.facade = injector.getInstance(DBFacade.class);
        this.db = injector.getInstance(DBManager.class);
    }

    @Test
    public void getExisitingRarity() throws Exception {
        Session session = this.db.getSession();

        Rarity rarity = new Rarity();
        rarity.setCode("COMMON");
        rarity.setName("Commune");

        session.beginTransaction();
        session.save(rarity);
        session.getTransaction().commit();
        this.db.closeSession();

        Rarity result = this.facade.getRarity("COMMON");
        assertNotNull(result);
    }

    @Test
    public void getNonExisitingRarity() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            Rarity result = this.facade.getRarity("BLABLA");
        });
    }


    @Test
    public void getExisitingCardClass() throws Exception {
        Session session = this.db.getSession();

        CardClass classe = new CardClass();
        classe.setCode("SHAMAN");
        classe.setName("Chaman");

        session.beginTransaction();
        session.save(classe);
        session.getTransaction().commit();
        this.db.closeSession();

        CardClass result = this.facade.getClasse("SHAMAN");
        assertNotNull(result);
    }

    @Test
    public void getNonExisitingCardClasse() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            CardClass result = this.facade.getClasse("SHAMAN");
        });
    }


    @Test
    public void getExisitingCardSet() throws Exception {
        Session session = this.db.getSession();

        CardSet cardSet = new CardSet();
        cardSet.setCode("NAXX");
        cardSet.setName("Naxxramas");

        session.beginTransaction();
        session.save(cardSet);
        session.getTransaction().commit();
        this.db.closeSession();

        CardSet result = this.facade.getSet("NAXX");
        assertNotNull(result);
    }

    @Test
    public void getNonExisitingCardSet() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            CardSet result = this.facade.getSet("NAXX");
        });
    }


    @Test
    public void getExisitingTag() throws Exception {
        Session session = this.db.getSession();

        CardTag tag = new CardTag();
        tag.setCode("BATTLECRY");
        tag.setName("Cri de guerre");

        session.beginTransaction();
        session.save(tag);
        session.getTransaction().commit();
        this.db.closeSession();

        CardTag result = this.facade.getTag("BATTLECRY");
        assertNotNull(result);
    }

    @Test
    public void getNonExisitingTag() throws Exception {
        Assertions.assertThrows(NoResultException.class, () ->{
            CardTag result = this.facade.getTag("BATTLECRY");
        });
    }

    @Test
    public void initDBTest() throws Exception{
        this.facade.initDB();
    }
}
