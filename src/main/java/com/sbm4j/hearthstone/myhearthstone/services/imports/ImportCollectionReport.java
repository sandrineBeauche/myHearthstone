package com.sbm4j.hearthstone.myhearthstone.services.imports;

import java.util.HashMap;

public class ImportCollectionReport {

    protected HashMap<Integer, String> errors = new HashMap<Integer, String>();

    protected int nbUpdated = 0;

    public void addError(int dbfId, String message){
        this.errors.put(dbfId, message);
    }

    public void incrNbUpdated(){
        this.nbUpdated++;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        this.errors.forEach((key, value) -> {
            builder.append(key + ":" + value + "\n");
        });
        return builder.toString();
    }
}
