package com.sbm4j.hearthstone.myhearthstone.services.db;

import org.hibernate.Session;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public interface DBManager {
    void createRegistry();

    void createFactory() throws Exception;

    Session getSession();

    void closeSession();
}
