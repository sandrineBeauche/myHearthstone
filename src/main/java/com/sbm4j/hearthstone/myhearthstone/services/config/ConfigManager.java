package com.sbm4j.hearthstone.myhearthstone.services.config;

import com.sbm4j.hearthstone.myhearthstone.model.BattleAccount;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;

public interface ConfigManager {
    File getDataDirectory();

    String getConnectionUrl();

    File getJsonGameData();

    File getImageDirectory();

    File getCatalogJsonFile();

    File getCollectionJsonFile();

    File getBigImagesDir();

    File getSmallImagesDir();

    File getTileImagesDir();

    File getThumbsImagesDir();

    File getChromiumContextPath();

    Image getAlternateCardImage();

    Boolean getInitDB();

    String getCardCatalogUrl();

    Boolean getDownloadCardCatalog();

    Boolean getDownloadCardCollection();

    BattleAccount getCurrentBattleAccount();
}
