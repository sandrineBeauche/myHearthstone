package com.sbm4j.hearthstone.myhearthstone.services.db;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;
import com.sbm4j.hearthstone.myhearthstone.model.json.DBInitiator;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
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
import java.net.URISyntaxException;
import java.util.ArrayList;


public class DBManagerImpl implements DBManager {

    protected StandardServiceRegistry registry;

    protected SessionFactory sessionFactory;

    protected Session currentSession;

    protected static Logger logger = LogManager.getLogger();

    @Inject
    protected ConfigManager config;

    public DBManagerImpl(){

    }

    @Inject
    public void init() throws Exception {
        this.createRegistry();
        this.createFactory();
    }

    @Override
    public void createRegistry(){
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().configure();
        String connectUrl = this.config.getConnectionUrl();
        builder.applySetting("hibernate.connection.url", connectUrl);
        builder.applySetting("connection.url", connectUrl);
        this.registry = builder.build();
    }

    @Override
    public void createFactory()throws Exception{
        try {
            sessionFactory = new MetadataSources(this.registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
            throw e;
        }
    }


    @Override
    public Session getSession(){
        if(this.currentSession == null){
            this.currentSession = this.sessionFactory.openSession();
        }
        return this.currentSession;
    }

    @Override
    public void closeSession(){
        if(this.currentSession != null) {
            this.currentSession.close();
            this.currentSession = null;
        }
    }







}
