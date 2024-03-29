package com.sbm4j.hearthstone.myhearthstone.services.db;

import org.hibernate.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface DBInitializer {
    void initDB() throws IOException;

    void updateDB() throws IOException;
}
