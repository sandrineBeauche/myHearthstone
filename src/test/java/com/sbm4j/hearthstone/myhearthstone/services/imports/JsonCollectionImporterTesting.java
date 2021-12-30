package com.sbm4j.hearthstone.myhearthstone.services.imports;

public class JsonCollectionImporterTesting extends JSONCollectionImporter{
    @Override
    protected void updateProgress(long workDone, long max) {
        logger.info("UI -> done " + workDone + " of " + max);
    }

    @Override
    protected void updateMessage(String message) {
        logger.info("UI -> " + message);
    }
}
