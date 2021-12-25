package com.sbm4j.hearthstone.myhearthstone.services;

import com.sbm4j.hearthstone.myhearthstone.services.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.DBManager;
import com.sbm4j.hearthstone.myhearthstone.services.JSONCardImporter;

import java.io.File;

public class JSonCardImporterTesting extends JSONCardImporter {

    public JSonCardImporterTesting(DBManager dbManager, CardImageManager imageManager) {
        super(dbManager, imageManager);
    }

    public JSonCardImporterTesting(DBManager dbManager, CardImageManager imageManager, File jsonFile) {
        super(dbManager, imageManager, jsonFile);
    }

    @Override
    protected void updateProgress(long workDone, long max) {
        logger.info("UI -> done " + workDone + " of " + max);
    }

    @Override
    protected void updateMessage(String message) {
        logger.info("UI -> " + message);
    }
}
