package com.sbm4j.hearthstone.myhearthstone.services.imports;

import com.sbm4j.hearthstone.myhearthstone.model.json.JsonUserData;

import java.io.File;
import java.io.FileNotFoundException;

public interface ImportCollectionAction {

    JsonUserData parseUserData(File jsonFile) throws FileNotFoundException;
}
