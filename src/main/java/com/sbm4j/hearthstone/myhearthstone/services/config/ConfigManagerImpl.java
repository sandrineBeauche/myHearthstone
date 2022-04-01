package com.sbm4j.hearthstone.myhearthstone.services.config;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import javafx.scene.image.Image;

import java.io.*;

public class ConfigManagerImpl implements ConfigManager {

    private static String bigImagesDirectoryName = "cards/bigCards";

    private static String smallImagesDirectoryName = "cards/smallCards";

    private static String tileImagesDirectoryName = "cards/tilesCards";

    private static String thumbnailsDirectoryName = "cards/thumbsCards";


    public ConfigManagerImpl(){}


    protected File dataRoot;


    @Inject
    public void init(){
        String path = System.getProperty("user.dir");
        if(path.contains("bin")) {
            this.dataRoot = new File(new File(path).getParentFile(), "data");
        }
        else {
            this.dataRoot = new File(path, "data");
        }
        if(!this.dataRoot.exists()){
            this.dataRoot.mkdir();
        }
    }

    @Override
    public File getDataDirectory() {
        return this.dataRoot;
    }

    @Override
    public String getConnectionUrl() {
        File dbFile = new File(this.dataRoot, "myHearthstoneDB");
        String result = "jdbc:hsqldb:file:" + dbFile.toString();
        return result;
    }

    @Override
    public File getJsonGameData() {
        return new File(this.dataRoot, "gameData.json");
    }

    @Override
    public File getImageDirectory() {
        return this.dataRoot;
    }

    @Override
    public File getCatalogJsonFile() {
        return new File(this.dataRoot,"cards.json");
    }

    @Override
    public File getCollectionJsonFile() {
        return new File(this.dataRoot,"hsreplayCollection.json");
    }


    @Override
    public File getBigImagesDir() {
        return new File(this.getImageDirectory(), bigImagesDirectoryName);
    }

    @Override
    public File getSmallImagesDir() {
        return new File(this.getImageDirectory(), smallImagesDirectoryName);
    }

    @Override
    public File getTileImagesDir() {
        return new File(this.getImageDirectory(), tileImagesDirectoryName);
    }

    @Override
    public File getThumbsImagesDir() {
        return new File(this.getImageDirectory(), thumbnailsDirectoryName);
    }

    @Override
    public Image getAlternateCardImage() {
        return new Image(CardImageManager.class.getResourceAsStream("backCardClassic.jpg"));
    }

    @Override
    public Boolean getInitDB() {
        return true;
    }

    @Override
    public String getCardCatalogUrl() {
        return "https://api.hearthstonejson.com/v1/latest/frFR/cards.collectible.json";
    }

    @Override
    public Boolean getDownloadCardCatalog() {
        return true;
    }

    @Override
    public Boolean getDownloadCardCollection() {
        return true;
    }

}
