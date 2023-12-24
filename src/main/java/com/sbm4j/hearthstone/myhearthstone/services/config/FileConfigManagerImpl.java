package com.sbm4j.hearthstone.myhearthstone.services.config;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.BattleAccount;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.utils.ResourceUtil;
import javafx.scene.image.Image;

import java.io.*;
import java.util.Properties;

public class FileConfigManagerImpl implements ConfigManager{

    public FileConfigManagerImpl(){}

    protected File dataRoot;

    protected File appRoot;

    protected Properties properties;

    @Inject
    public void init() throws IOException {
        this.appRoot = ResourceUtil.getAppRoot();
        FileInputStream reader = new FileInputStream(new File(appRoot, "config.properties"));

        this.properties = new Properties();
        this.properties.load(reader);

        String dataDirectoryName = this.properties.getProperty("data", "data");
        if(dataDirectoryName.contains(":")){
            this.dataRoot = new File(dataDirectoryName);
        }
        else{
            this.dataRoot = new File(this.appRoot, dataDirectoryName);
        }
    }

    @Override
    public File getDataDirectory() {
        return this.dataRoot;
    }

    @Override
    public String getConnectionUrl() {
        String protocol = this.properties.getProperty("connection.url.protocol", "jdbc:hsqldb:file");
        String database = this.properties.getProperty("connection.url.database", "myHearthstoneDB");
        File dbFile = new File(this.dataRoot, database);
        String result = protocol + ":" + dbFile.toString();
        return result;
    }

    @Override
    public File getJsonGameData() {
        String json = this.properties.getProperty("json.gamedata", "gameData.json");
        return new File(this.dataRoot, json);
    }

    @Override
    public File getImageDirectory() {
        return this.dataRoot;
    }

    @Override
    public File getCatalogJsonFile() {
        String json = this.properties.getProperty("json.catalog", "cards.json");
        return new File(this.dataRoot, json);
    }

    @Override
    public File getCollectionJsonFile() {
        String json = this.properties.getProperty("json.collection", "hsreplayCollection.json");
        return new File(this.dataRoot, json);
    }

    @Override
    public File getBigImagesDir() {
        String dir = this.properties.getProperty("images.cards.big", "cards/bigCards");
        return new File(this.getImageDirectory(), dir);
    }

    @Override
    public File getSmallImagesDir() {
        String dir = this.properties.getProperty("images.cards.small", "cards/smallCards");
        return new File(this.getImageDirectory(), dir);
    }

    @Override
    public File getTileImagesDir() {
        String dir = this.properties.getProperty("images.cards.tile", "cards/tilesCards");
        return new File(this.getImageDirectory(), dir);
    }

    @Override
    public File getThumbsImagesDir() {
        String dir = this.properties.getProperty("images.cards.thumbs", "cards/thumbsCards");
        return new File(this.getImageDirectory(), dir);
    }

    @Override
    public File getChromiumContextPath() {
        return new File(this.getDataDirectory(), this.properties.getProperty("chromium.context", "chromium"));
    }

    @Override
    public Image getAlternateCardImage() {
        return new Image(CardImageManager.class.getResourceAsStream("backCardClassic.jpg"));
    }

    @Override
    public Boolean getInitDB() {
        String stringResult = this.properties.getProperty("initDB", "true");
        return Boolean.parseBoolean(stringResult);
    }

    @Override
    public String getCardCatalogUrl() {
        return this.properties.getProperty("card.catalog.url",
                "https://api.hearthstonejson.com/v1/latest/frFR/cards.collectible.json");
    }

    @Override
    public Boolean getDownloadCardCatalog() {
        String stringResult = this.properties.getProperty("card.catalog.download", "true");
        return Boolean.parseBoolean(stringResult);
    }

    @Override
    public Boolean getDownloadCardCollection() {
        String stringResult = this.properties.getProperty("card.collection.download", "false");
        return Boolean.parseBoolean(stringResult);
    }


    @Override
    public BattleAccount getCurrentBattleAccount() {
        String filename = this.properties.getProperty("battleAccount", "battleAccount.json");
        File accountFile =  new File(this.dataRoot,filename);
        try {
            FileReader reader = new FileReader(accountFile);
            Gson gson = new Gson();
            BattleAccount account =  gson.fromJson(reader, BattleAccount.class);
            return account;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
