package com.sbm4j.hearthstone.myhearthstone.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.Service;
import org.hibernate.service.ServiceRegistry;

public class DBManager {

    protected StandardServiceRegistry registry;

    protected SessionFactory sessionFactory;

    public DBManager() throws Exception{
        this.createRegistry();
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

    protected void createRegistry(){
        this.registry = new StandardServiceRegistryBuilder().configure().build();
    }

    public Session createSession(){
        Session session = this.sessionFactory.openSession();
        return session;
    }
}
