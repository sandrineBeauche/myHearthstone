package com.sbm4j.hearthstone.myhearthstone.services;

import com.google.gson.Gson;
import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;
import com.sbm4j.hearthstone.myhearthstone.model.json.DBInitiator;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;


public class DBManager {

    protected StandardServiceRegistry registry;

    protected SessionFactory sessionFactory;

    protected Session currentSession;

    protected static Logger logger = LogManager.getLogger();


    public DBManager() throws Exception{
        this.createRegistry();
        this.createFactory();
    }

    protected void createRegistry(){
        this.registry = new StandardServiceRegistryBuilder().configure().build();
    }

    protected void createFactory()throws Exception{
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
            throw e;
        }
    }

    public void initDB() throws FileNotFoundException, URISyntaxException {
        File file = new File("/home/sandrine/progs/myHearthstone/gameData.json");
        if(file.exists()){
            FileReader reader = new FileReader(file);

            Gson gson = new Gson();
            DBInitiator initiator = gson.fromJson(reader, DBInitiator.class);
            this.initRarity(initiator.getRarity());
            this.initCardClass(initiator.getClasses());
            this.initCardSet(initiator.getExtensions());
            this.initCardTags(initiator.getTags());
        }
    }

    protected void initRarity(ArrayList<Rarity> rarity){
        Session session = this.getSession();
        session.beginTransaction();
        for(Rarity current: rarity){
            try {
                Rarity r = this.getRarity(current.getCode());
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
        session.getTransaction().commit();
        this.closeSession();
    }


    protected void initCardClass(ArrayList<CardClass> classes){
        Session session = this.getSession();
        session.beginTransaction();
        for(CardClass current: classes){
            try {
                CardClass c = this.getClasse(current.getCode());
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
        session.getTransaction().commit();
        this.closeSession();
    }

    protected void initCardSet(ArrayList<CardSet> sets){
        Session session = this.getSession();
        session.beginTransaction();
        for(CardSet current: sets){
            try {
                CardSet c = this.getSet(current.getCode());
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
        session.getTransaction().commit();
        this.closeSession();
    }

    protected void initCardTags(ArrayList<CardTag> tags){
        Session session = this.getSession();
        session.beginTransaction();
        for(CardTag current: tags){
            try {
                CardTag c = this.getTag(current.getCode());
            }
            catch (NoResultException ex){
                session.save(current);
            }
        }
        session.getTransaction().commit();
        this.closeSession();
    }


    public Session getSession(){
        if(this.currentSession == null){
            this.currentSession = this.sessionFactory.openSession();
        }
        return this.currentSession;
    }

    public void closeSession(){
        if(this.currentSession != null) {
            this.currentSession.close();
            this.currentSession = null;
        }
    }


    public Rarity getRarity(String code) throws NoResultException {
        Session session = this.getSession();
        TypedQuery<Rarity> typedQuery = session.createNamedQuery("rarity_from_code", Rarity.class);
        return typedQuery.setParameter("code", code).getSingleResult();
    }

    public CardClass getClasse(String code) throws NoResultException {
        Session session = this.getSession();
        TypedQuery<CardClass> typedQuery = session.createNamedQuery("class_from_code", CardClass.class);
        return typedQuery.setParameter("code", code).getSingleResult();
    }

    public CardSet getSet(String code) throws NoResultException{
        Session session = this.getSession();
        TypedQuery<CardSet> typedQuery = session.createNamedQuery("cardSet_from_code", CardSet.class);
        return typedQuery.setParameter("code", code).getSingleResult();
    }

    public CardTag getTag(String code){
        Session session = this.getSession();
        TypedQuery<CardTag> typedQuery = session.createNamedQuery("tag_from_code", CardTag.class);
        return typedQuery.setParameter("code", code).getSingleResult();
    }




}
