package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardSetDetail;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ExtensionsViewModel implements ViewModel, Initializable {

    @Inject
    protected DBFacade dbFacade;

    @Inject
    private NotificationCenter notificationCenter;

    protected Logger logger = LogManager.getLogger();

    protected boolean refreshed = false;

    /* items property */
    private ObjectProperty<ObservableList<CardSetDetail>> items = new SimpleObjectProperty<ObservableList<CardSetDetail>>();
    public ObjectProperty<ObservableList<CardSetDetail>> getItemsProperty() {return this.items;}
    public ObservableList<CardSetDetail> getItems(){return this.items.get();}
    public void setItems(ObservableList<CardSetDetail> value){this.items.set(value);}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.items.set(FXCollections.observableArrayList());

        this.notificationCenter.subscribe(MenuConnexionViewModel.COLLECTION_UPDATED, (key, payload) -> {
            setRefreshed(false);
        });
    }

    public boolean isRefreshed() {
        return refreshed;
    }

    public void setRefreshed(boolean refreshed) {
        this.refreshed = refreshed;
    }

    public void refresh(){
        if(!this.refreshed){
            List<CardSetDetail> cardSets = this.dbFacade.getCardSetDetailList();
            this.items.get().addAll(cardSets);
            this.refreshed = true;
        }
    }
}
