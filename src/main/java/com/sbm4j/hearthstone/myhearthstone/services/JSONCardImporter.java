package com.sbm4j.hearthstone.myhearthstone.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbm4j.hearthstone.myhearthstone.model.CardDetail;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JSONCardImporter {

    public ArrayList<CardDetail> importCards(Reader jsonReader){
        Gson gson = new Gson();
        Type cardCollectionType = new TypeToken<ArrayList<CardDetail>>(){}.getType();
        return gson.fromJson(jsonReader, cardCollectionType);
    }
}
