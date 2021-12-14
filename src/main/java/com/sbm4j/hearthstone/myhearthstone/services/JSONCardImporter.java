package com.sbm4j.hearthstone.myhearthstone.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JSONCardImporter {

    protected static Logger logger = LogManager.getLogger();

    public ArrayList<JsonCard> importCards(File jsonFile) throws IOException {
        FileReader reader = new FileReader(jsonFile);
        return this.importCards(reader, true);
    }

    public ArrayList<JsonCard> importCards(Reader jsonReader, boolean cutJson) throws IOException {
        ArrayList<JsonCard> result = null;
        Gson gson = new Gson();

        if(cutJson){
            result = new ArrayList<JsonCard>();
            char c = (char) jsonReader.read();
            StringBuilder builder = null;
            boolean inString = false;

            while(c != '\uFFFF'){
                if(builder != null){
                    builder.append(c);
                    if(c == '"'){
                        inString = !inString;
                    }
                    if(c == '}' && !inString){
                        String json = builder.toString();
                        logger.info("parse from Json: " + json);
                        JsonCard card = gson.fromJson(json, JsonCard.class);
                        card.setJsonDesc(json);
                        result.add(card);
                        builder = null;
                    }
                }
                else{
                    if(c == '{'){
                        builder = new StringBuilder();
                        builder.append(c);
                    }
                }
                c = (char) jsonReader.read();
            }
        }
        else{
            Type cardCollectionType = new TypeToken<ArrayList<JsonCard>>(){}.getType();
            result = gson.fromJson(jsonReader, cardCollectionType);
        }

        return result;
    }

}
