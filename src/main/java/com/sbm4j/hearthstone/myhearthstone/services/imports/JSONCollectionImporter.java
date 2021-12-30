package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonUserData;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class JSONCollectionImporter extends Task<ImportCollectionReport> implements ImportCollectionAction{

    protected static Logger logger = LogManager.getLogger();

    @Inject
    protected DBManager dbManager;

    @Inject
    protected ConfigManager config;

    @Inject
    protected DBFacade dbFacade;

    @Override
    public JsonUserData parseUserData(File jsonFile) throws FileNotFoundException {
        Reader reader = new FileReader(jsonFile);
        Gson gson = new Gson();
        JsonUserData data = gson.fromJson(reader, JsonUserData.class);
        return data;
    }

    @Override
    protected ImportCollectionReport call() throws Exception {
        return null;
    }
}
