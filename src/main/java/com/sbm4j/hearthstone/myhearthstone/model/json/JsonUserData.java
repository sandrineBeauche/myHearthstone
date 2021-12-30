package com.sbm4j.hearthstone.myhearthstone.model.json;

import java.util.HashMap;

public class JsonUserData {

    private HashMap<Integer, Integer[]> collection;

    private String lastModified;

    public HashMap<Integer, Integer[]> getCollection() {
        return collection;
    }

    public void setCollection(HashMap<Integer, Integer[]> collection) {
        this.collection = collection;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
