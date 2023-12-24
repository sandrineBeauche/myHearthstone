package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.BattleAccount;
import com.sbm4j.hearthstone.myhearthstone.model.CardUserData;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonUserData;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;

import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.services.download.DownloadManager;
import com.sbm4j.hearthstone.myhearthstone.views.Dialogs;
import de.saxsys.mvvmfx.utils.commands.Action;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.Session;

import java.io.*;
import java.util.HashMap;

public class JSONCollectionImporter extends Action implements ImportCollectionAction{

    protected static Logger logger = LogManager.getLogger();

    @Inject
    protected DBManager dbManager;

    @Inject
    protected DBFacade dbFacade;

    @Inject
    protected ConfigManager config;

    @Inject
    protected DownloadManager downloadManager;

    protected long totalWork;

    protected long workDone;

    protected ImportCollectionReport report = new ImportCollectionReport();


    @Override
    public JsonUserData parseUserData(File jsonFile) throws FileNotFoundException {
        Reader reader = new FileReader(jsonFile);
        Gson gson = new Gson();
        return gson.fromJson(reader, JsonUserData.class);
    }

    public JsonUserData parseUserData(String jsonString){
        Reader reader = new StringReader(jsonString);
        Gson gson = new Gson();
        return gson.fromJson(reader, JsonUserData.class);
    }

    @Override
    public void importCollection(File jsonFile) throws FileNotFoundException {
        JsonUserData data = this.parseUserData(jsonFile);
        importCollection(data);
    }

    public void importCollection(String jsonString){
        JsonUserData data = this.parseUserData(jsonString);
        importCollection(data);
    }

    protected void importCollection(JsonUserData data){
        this.totalWork = data.getCollection().size();
        this.workDone = 0;
        data.getCollection().forEach((key, values) ->{
            try {
                this.importCollectionItem(key, values);
                this.report.incrNbUpdated();
            }
            catch(Exception ex){
                this.report.addError(key, ex.getMessage());
            }
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
            logger.warn("The item with dbfId " + key + " does not exists in the database");
        }
        this.dbManager.closeSession();
        this.workDone++;
        this.updateProgress(this.workDone, this.totalWork);
    }


    @Override
    protected void action() throws Exception {
        File jsonFile = this.config.getCollectionJsonFile();
        if (jsonFile != null && jsonFile.exists() && !this.config.getDownloadCardCollection()) {
            this.importCollection(jsonFile);
        }
        else{
            BattleAccount account = dbFacade.getConnectedAccount();
            String json = this.downloadManager.downloadCollectionFile(account);
            this.importCollection(json);
        }


        if(this.report.errors.isEmpty()){
            this.updateMessage(this.report.nbUpdated + " cartes mises à jour");
        }
        else{
            this.updateMessage(this.report.toString());
            throw new Exception();
        }
    }



    @Override
    public void handle(ActionEvent event) {
        try {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Importer la collection utilisateur");
            dialog.setHeaderText("Importation de la collection utilisateur");
            dialog.setWidth(600);
            dialog.getDialogPane().getStylesheets().add(Dialogs.getCss());

            new Thread(this).start();
            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
