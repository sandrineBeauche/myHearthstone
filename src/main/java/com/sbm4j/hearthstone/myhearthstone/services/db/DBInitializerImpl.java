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
            performDataFile(file, false);
        }
        else{
            logger.info(file.getAbsolutePath() + " does not exists. Database cannot be initialized");
        }
    }

    @Override
    public void updateDB() throws IOException {
        logger.info("Update database...");
        File file = this.config.getJsonGameData();

        if(file.exists()) {
            logger.info("... with file " + file.getAbsolutePath());
            performDataFile(file, true);
        }
        else{
            logger.info(file.getAbsolutePath() + " does not exists. Database cannot be initialized");
        }
    }


    protected void performDataFile(File file, boolean update) throws IOException{
        FileReader reader = new FileReader(file, StandardCharsets.UTF_8);

        Gson gson = new Gson();
        DBInitiator initiator = gson.fromJson(reader, DBInitiator.class);

        Session session = this.db.getSession();
        session.beginTransaction();
        this.initRarity(initiator.getRarity(), session, update);
        this.initCardClass(initiator.getClasses(), session, update);
        this.initCardSet(initiator.getExtensions(), session, update);
        this.initCardTags(initiator.getTags(), session, update);
        this.initHeros(initiator.getHeros(), session, update);
        session.getTransaction().commit();
        this.db.closeSession();
    }

    protected void initRarity(ArrayList<Rarity> rarity, Session session, boolean update){
        for(Rarity current: rarity){
            try {
                Rarity r = this.facade.getRarity(current.getCode());
                if(update){
                    r.updateFromJson(current);
                    session.update(r);
                }
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
    }


    protected void initCardClass(ArrayList<CardClass> classes, Session session, boolean update){
        for(CardClass current: classes) {
            try {
                CardClass c = this.facade.getClasse(current.getCode());
                if(update){
                    c.updateFromJson(current);
                    session.update(c);
                }
            } catch (NoResultException ex) {
                session.save(current);
            }
        }
    }

    protected void initCardSet(ArrayList<CardSet> sets, Session session, boolean update){
        for(CardSet current: sets){
            try {
                CardSet c = this.facade.getSet(current.getCode());
                if(update){
                    c.updateFromJson(current);
                    session.update(c);
                }
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
    }


    protected void initHeros(ArrayList<JsonHero> heros, Session session, boolean update){
        for(JsonHero current: heros){
            try {
                Hero hero = this.facade.getHero(current.getCode());
                if(update){
                    hero.updateFromJson(current, this.facade);
                    session.update(hero);
                }
            }
            catch(NoResultException ex){
                CardClass classe = this.facade.getClasse(current.getCodeClass());
                Hero newHero = new Hero(current.getName(), current.getCode(), classe);
                newHero.setDbfId(current.getDbfId());
                session.save(newHero);
            }
        }
    }

    protected void initCardTags(ArrayList<CardTag> tags, Session session, boolean update){
        for(CardTag current: tags){
            try {
                CardTag c = this.facade.getTag(current.getCode());
                if(update){
                    c.updateFromJson(current);
                    session.update(c);
                }
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
    }
}
