package com.sbm4j.hearthstone.myhearthstone.services.config;

import java.io.File;
import java.net.URL;

public class ConfigManagerTesting extends ConfigManagerImpl{

    protected boolean initDB = false;

    public ConfigManagerTesting(File rootFile){
        this.dataRoot = rootFile;
    }

    public ConfigManagerTesting(File rootFile, boolean initDB){
        this.dataRoot = rootFile;
        this.initDB = initDB;
    }


    @Override
    public String getConnectionUrl() {
        return "jdbc:hsqldb:mem:db1";
    }

    @Override
    public Boolean getInitDB() {
        return this.initDB;
    }

    @Override
    public File getJsonGameData() {
        URL res = this.getClass().getClassLoader().getResource("gameData.json");
        return new File(res.getFile());
    }

    @Override
    public File getCatalogJsonFile() {
        URL res = this.getClass().getClassLoader().getResource("cardsTest.json");
        return new File(res.getFile());
    }
}
