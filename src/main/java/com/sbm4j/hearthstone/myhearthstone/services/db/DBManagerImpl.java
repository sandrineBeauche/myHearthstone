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
        this.addGroupConcatDistinctFunction();
    }

    protected void addGroupConcatDistinctFunction(){
        String aggFuncSql = "CREATE AGGREGATE FUNCTION group_concat_distinct " +
                "(IN val VARCHAR(100), IN flag BOOLEAN, INOUT buffer VARCHAR(1000), INOUT counter INT) " +
                    "RETURNS VARCHAR(1000) " +
                    "CONTAINS SQL " +
                    "BEGIN ATOMIC " +
                        "IF FLAG THEN " +
                            "RETURN BUFFER; " +
                        "ELSE " +
                            "IF val IS NULL THEN RETURN NULL; END IF; " +
                            "IF buffer IS NULL THEN SET BUFFER = ''; END IF; " +
                            "IF counter IS NULL THEN SET COUNTER = 0; END IF; " +
                            "IF (LOCATE(val, buffer) = 0 OR counter = 0) THEN " +
                                "IF counter > 0 THEN SET buffer = buffer || ','; END IF; " +
                                "SET buffer = buffer + val; " +
                            "END IF;" +
                            "SET counter = counter + 1; " +
                            "RETURN NULL; " +
                        "END IF; " +
                    "END";

        Session session = this.getSession();
        try {
            logger.info("Add the aggregate function group_concat_distinct");
            session.beginTransaction();
            session.createNativeQuery(aggFuncSql).executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception ex){
            logger.warn("Aggregate Function group_concat_distinct already exists in the database");
        }
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
