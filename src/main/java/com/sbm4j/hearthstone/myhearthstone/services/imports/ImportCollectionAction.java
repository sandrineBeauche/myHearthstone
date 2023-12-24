package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.sbm4j.hearthstone.myhearthstone.model.json.JsonUserData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.FileNotFoundException;

public interface ImportCollectionAction extends EventHandler<ActionEvent> {

    JsonUserData parseUserData(File jsonFile) throws FileNotFoundException;

    void importCollection(File jsonFile) throws FileNotFoundException;

    void importCollection(String jsonString);
}
