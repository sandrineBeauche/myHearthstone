package com.sbm4j.hearthstone.myhearthstone.services;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.controlsfx.dialog.ProgressDialog;

import java.io.File;

public class ImportCatalogAction implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
        try {
            DBManager dbManager = new DBManager();
            dbManager.initDB();

            File imageDir = new File("/home/sandrine/progs/myHearthstone");
            CardImageManager imageManager = new CardImageManager(imageDir);

            File jsonFile= new File("/home/sandrine/progs/myHearthstone/cardsTest.json");
            JSONCardImporter importer = new JSONCardImporter(dbManager, imageManager, jsonFile);

            ProgressDialog dialog = new ProgressDialog(importer);
            dialog.setTitle("Importer les données Hearthstone");
            dialog.setHeaderText("Importation des cartes hearthstone");
            dialog.setWidth(600);

            new Thread(importer).start();
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
