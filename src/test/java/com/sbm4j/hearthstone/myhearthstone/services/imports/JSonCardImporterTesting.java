package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.imports.JSONCardImporter;

import java.io.File;

public class JSonCardImporterTesting extends JSONCardImporter {

    public JSonCardImporterTesting() {
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
