package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.sbm4j.hearthstone.myhearthstone.model.json.JsonCard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;

public interface ImportCatalogAction extends EventHandler<ActionEvent> {

    enum CardStatus {
        NEW_CARD,
        MODIFIED_CARD,
        UPTODATE_CARD
    }

    ArrayList<JsonCard> parseCards(File jsonFile) throws IOException;

    ArrayList<JsonCard> parseCards(Reader jsonReader, boolean cutJson) throws IOException;

    HashSet<String> verifyTags(File data) throws IOException;

    void importCards(File jsonFile) throws IOException;

    void addCardDetail(JsonCard jsonCard);

    void updateCardDetail(JsonCard jsonCard);

    CardStatus cardDetailStatus(JsonCard jsonCard);

    ImportCardReport getReport();
}
