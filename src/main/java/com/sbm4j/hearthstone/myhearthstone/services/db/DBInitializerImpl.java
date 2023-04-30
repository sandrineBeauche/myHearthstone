package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.model.json.DBInitiator;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonHero;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DBInitializerImpl implements DBInitializer {

    protected static Logger logger = LogManager.getLogger();

    @Inject
    protected DBManager db;

    @Inject
    protected ConfigManager config;

    @Inject
    protected DBFacade facade;

    @Override
    public void initDB() throws IOException {
        logger.info("Initialize database...");
        File file = this.config.getJsonGameData();

        if(file.exists()) {
            logger.info("... with file " + file.getAbsolutePath());
            FileReader reader = new FileReader(file, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            DBInitiator initiator = gson.fromJson(reader, DBInitiator.class);

            Session session = this.db.getSession();
            session.beginTransaction();
            this.initRarity(initiator.getRarity(), session);
            this.initCardClass(initiator.getClasses(), session);
            this.initCardSet(initiator.getExtensions(), session);
            this.initCardTags(initiator.getTags(), session);
            this.initHeros(initiator.getHeros(), session);
            session.getTransaction().commit();
            this.db.closeSession();
        }
        else{
            logger.info(file.getAbsolutePath() + " does not exists. Database cannot be initialized");
        }
    }

    protected void initRarity(ArrayList<Rarity> rarity, Session session){
        for(Rarity current: rarity){
            try {
                Rarity r = this.facade.getRarity(current.getCode());
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
    }


    protected void initCardClass(ArrayList<CardClass> classes, Session session){
        for(CardClass current: classes) {
            try {
                CardClass c = this.facade.getClasse(current.getCode());
            } catch (NoResultException ex) {
                session.save(current);
            }
        }
    }

    protected void initCardSet(ArrayList<CardSet> sets, Session session){
        for(CardSet current: sets){
            try {
                CardSet c = this.facade.getSet(current.getCode());
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
    }


    protected void initHeros(ArrayList<JsonHero> heros, Session session){
        for(JsonHero current: heros){
            try {
                Hero hero = this.facade.getHero(current.getCode());
            }
            catch(NoResultException ex){
                CardClass classe = this.facade.getClasse(current.getCodeClass());
                Hero newHero = new Hero(current.getName(), current.getCode(), classe);
                newHero.setDbfId(current.getDbfId());
                session.save(newHero);
            }
        }
    }

    protected void initCardTags(ArrayList<CardTag> tags, Session session){
        for(CardTag current: tags){
            try {
                CardTag c = this.facade.getTag(current.getCode());
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
    }
}
