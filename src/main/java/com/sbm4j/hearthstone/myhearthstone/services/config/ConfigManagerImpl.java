package com.sbm4j.hearthstone.myhearthstone.services.config;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import javafx.scene.image.Image;

import java.io.File;

public class ConfigManagerImpl implements ConfigManager {

    private static String bigImagesDirectoryName = "bigCards";

    private static String smallImagesDirectoryName = "smallCards";

    private static String tileImagesDirectoryName = "tilesCards";

    private static String thumbnailsDirectoryName = "thumbsCards";

    public ConfigManagerImpl(){}


    protected File dataRoot;

    @Inject
    public void init(){
        this.dataRoot = new File("/home/sandrine/progs/myHearthstone");
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
        return new File(this.dataRoot, bigImagesDirectoryName);
    }

    @Override
    public File getSmallImagesDir() {
        return new File(this.dataRoot, smallImagesDirectoryName);
    }

    @Override
    public File getTileImagesDir() {
        return new File(this.dataRoot, tileImagesDirectoryName);
    }

    @Override
    public File getThumbsImagesDir() {
        return new File(this.dataRoot, thumbnailsDirectoryName);
    }

    @Override
    public Image getAlternateCardImage() {
        return new Image(CardImageManager.class.getResourceAsStream("backCardClassic.jpg"));
    }

    @Override
    public Boolean getInitDB() {
        return true;
    }
}
