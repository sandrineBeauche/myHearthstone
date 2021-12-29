package com.sbm4j.hearthstone.myhearthstone.services.config;

import javafx.scene.image.Image;

import java.io.File;

public interface ConfigManager {
    File getDataDirectory();

    String getConnectionUrl();

    File getJsonGameData();

    File getImageDirectory();

    File getCatalogJsonFile();

    File getBigImagesDir();

    File getSmallImagesDir();

    File getTileImagesDir();

    File getThumbsImagesDir();

    Image getAlternateCardImage();

    Boolean getInitDB();
}