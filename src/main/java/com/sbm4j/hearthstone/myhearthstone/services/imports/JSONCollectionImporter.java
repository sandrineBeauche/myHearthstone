package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardUserData;
import com.sbm4j.hearthstone.myhearthstone.model.json.JsonUserData;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;

import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.ProgressDialog;
import org.hibernate.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;

public class JSONCollectionImporter extends Task<ImportCollectionReport> implements ImportCollectionAction{

    protected static Logger logger = LogManager.getLogger();

    @Inject
    protected DBManager dbManager;

    @Inject
    protected ConfigManager config;

    protected long totalWork;

    protected long workDone;

    protected ImportCollectionReport report = new ImportCollectionReport();


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
        this.workDone++;
        this.updateProgress(this.workDone, this.totalWork);
    }


    @Override
    protected ImportCollectionReport call() throws Exception {
        File jsonFile = this.config.getCollectionJsonFile();
        if(jsonFile != null){
            this.importCollection(jsonFile);
        }
        return this.report;
    }

    protected void showReportNotification(){
        if(this.report.errors.size() == 0){
            this.showOkReportNotification();
        }
        else{
            logger.error(this.report.toString());
            this.showErrorReportNotification();
        }
    }

    protected void showOkReportNotification(){
        String title = "Importation de la collection utilisateur";
        String text = this.report.nbUpdated + " cartes mises à jour";
        try {
            Notifications.create().title(title).text(text).showInformation();
        }
        catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText("Importation effectuée avec succès!");
            alert.setContentText(text);
            alert.showAndWait();
        }
    }

    protected void showErrorReportNotification(){
        String title = "Importation du catalogue de cartes";
        String text = this.report.toString();
        try {
            Notifications.create().title(title).text(text).showError();
        }
        catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText("Erreurs lors de l'importation");
            alert.setContentText(text);
            alert.setResizable(true);
            alert.setWidth(500);
            alert.showAndWait();
        }
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
            this.showReportNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
