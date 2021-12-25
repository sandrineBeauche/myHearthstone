package com.sbm4j.hearthstone.myhearthstone.services;

import com.sbm4j.hearthstone.myhearthstone.services.DBManager;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DBManagerTesting extends DBManager {

    public DBManagerTesting() throws Exception {
        super();
    }

    @Override
    protected void createRegistry() {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().configure();
        builder.applySetting("hibernate.connection.url", "jdbc:hsqldb:mem:db1");
        builder.applySetting("connection.url", "jdbc:hsqldb:mem:db1");
        this.registry = builder.build();
    }
}
