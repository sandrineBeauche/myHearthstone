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
import com.sbm4j.hearthstone.myhearthstone.utils.ResourceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.internal.SessionImpl;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        if(!hasFunctionInDatabase("GROUP_CONCAT_DISTINCT")) {
            this.addGroupConcatDistinctFunction();
        }
    }

    public boolean hasFunctionInDatabase(String functionName) throws SQLException {
        SessionImpl session = (SessionImpl) this.getSession();
        Connection conn = session.connection();
        DatabaseMetaData metadata = conn.getMetaData();

        ResultSet result = metadata.getFunctions(null, null, functionName);

        boolean validRow = result.first();
        boolean found = false;
        while(validRow && !found) {
            String funcName = result.getString("FUNCTION_NAME");
            found = funcName.equals(functionName);
            if(!found) {
                validRow = result.next();
            }
        }
        return found;
    }

    public void addGroupConcatDistinctFunction() throws IOException {
        String aggFuncSql = ResourceUtil.getResourceContent("group_concat_distinct.sql");

        try {
            Session session = this.getSession();

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
