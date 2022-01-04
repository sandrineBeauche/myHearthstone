package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardUserData;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonUserData;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;

import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.Session;

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


    @Override
    public JsonUserData parseUserData(File jsonFile) throws FileNotFoundException {
        Reader reader = new FileReader(jsonFile);
        Gson gson = new Gson();
        JsonUserData data = gson.fromJson(reader, JsonUserData.class);
        return data;
    }

    @Override
    public void importCollection(File jsonFile) throws FileNotFoundException {
        JsonUserData data = this.parseUserData(jsonFile);

        this.updateProgress(0, data.getCollection().size());
        data.getCollection().forEach((key, values) ->{
            this.importCollectionItem(key, values);
            this.updateProgress(this.getWorkDone() + 1, this.getTotalWork());
        });
    }


    protected void importCollectionItem(Integer key, Integer[] values){
        Session session = this.dbManager.getSession();
        CardUserData data = session.get(CardUserData.class, key);
        if(data != null){
            data.setNbCards(values[0]);
            data.setNbGolden(values[1]);
            data.setNbTotalCards(values[0] + values[1]);

            session.beginTransaction();
            session.update(data);
            session.getTransaction().commit();
        }
        else{
            CardUserData newData = new CardUserData();
            newData.setDbfId(key);
            newData.setNbCards(values[0]);
            newData.setNbGolden(values[1]);
            newData.setNbTotalCards(values[0] + values[1]);

            session.beginTransaction();
            session.update(newData);
            session.getTransaction().commit();
        }
        this.dbManager.closeSession();
    }


    @Override
    protected ImportCollectionReport call() throws Exception {
        File jsonFile = this.config.getCollectionJsonFile();
        if(jsonFile != null){
            this.importCollection(jsonFile);
        }
        return null;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Importer la collection utilisateur");
            dialog.setHeaderText("Importation de la collection utilisateur");
            dialog.setWidth(600);

            new Thread(this).start();
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
