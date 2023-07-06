package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import javafx.fxml.Initializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class AbstractTabViewModel {

    protected boolean refreshed = false;

    protected static Logger logger = LogManager.getLogger();

    public boolean isRefreshed() {
        return refreshed;
    }

    public void setRefreshed(boolean refreshed) {
        this.refreshed = refreshed;
    }
}
