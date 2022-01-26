package com.sbm4j.hearthstone.myhearthstone.services.db;

import org.hibernate.Session;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public interface DBManager {
    void createRegistry();

    void createFactory() throws Exception;

    Session getSession();

    void closeSession();

    boolean hasFunctionInDatabase(String functionName) throws SQLException;
}
