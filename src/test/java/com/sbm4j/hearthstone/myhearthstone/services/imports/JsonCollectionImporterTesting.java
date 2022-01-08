package com.sbm4j.hearthstone.myhearthstone.services.imports;

public class JsonCollectionImporterTesting extends JSONCollectionImporter{

    protected long total;

    protected long current;

    @Override
    protected void updateProgress(long workDone, long max) {
        this.total = max;
        this.current = workDone;
        logger.info("UI -> done " + workDone + " of " + max);
    }

    @Override
    protected void updateMessage(String message) {
        logger.info("UI -> " + message);
    }


}
