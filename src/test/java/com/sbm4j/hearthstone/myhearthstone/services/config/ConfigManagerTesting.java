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
        URL res = this.getClass().getClassLoader().getResource("cards.json");
        return new File(res.getFile());
    }

    @Override
    public File getCollectionJsonFile() {
        URL res = this.getClass().getClassLoader().getResource("hsreplayCollection.json");
        return new File(res.getFile());
    }


    @Override
    public File getImageDirectory() {
        if(this.dataRoot != null){
            return super.getImageDirectory();
        }
        else {
            URL res = this.getClass().getClassLoader().getResource("images");
            return new File(res.getFile());
        }
    }
}
