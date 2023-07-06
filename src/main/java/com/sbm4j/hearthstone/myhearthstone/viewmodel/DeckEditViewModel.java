package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.cardClasses.CardClassImageLoader;
import com.sbm4j.hearthstone.myhearthstone.services.notifications.Notificator;
import com.sbm4j.hearthstone.myhearthstone.views.Dialogs;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class DeckEditViewModel implements ViewModel, Initializable {

    public final static String SHOW_DECK = "SHOW_DECK";

    public final static String BACK = "BACK";

    /* title property */
    private StringProperty title = new SimpleStringProperty();
    public StringProperty getTitleProperty(){return this.title;}
    public String getTitle(){return this.title.get();}
    public void setTitle(String value){this.title.set(value);}


    @Inject
    protected DBFacade dbFacade;
    public DBFacade getDbFacade(){return this.dbFacade;}

    @Inject
    protected DBManager dbManager;
    public DBManager getDbManager(){return this.dbManager;}

    @Inject
    protected ImageManager imageManager;
    public ImageManager getImageManager(){return this.imageManager;}

    @Inject
    protected Notificator notificator;
    public Notificator getNotificator(){return this.notificator;}

    @Inject
    private NotificationCenter notificationCenter;

    protected DeckEditGeneraltabViewModel generalTab;
    public DeckEditGeneraltabViewModel getGeneralTab(){return this.generalTab;}

    protected DeckEditCardsListTabViewModel cardsListTab;
    public DeckEditCardsListTabViewModel getCardsListTab(){return this.cardsListTab;}

    protected DeckEditStatsTabViewModel statsTab;
    public DeckEditStatsTabViewModel getStatsTab(){ return this.statsTab;}

    protected DeckEditNotesTabViewModel notesTab;
    public DeckEditNotesTabViewModel getNotesTab(){return this.notesTab;}

    protected Deck currentDeck;

    protected DeckListItem currentDeckItem;

    protected static Logger logger = LogManager.getLogger();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.generalTab = new DeckEditGeneraltabViewModel(this);
        this.cardsListTab = new DeckEditCardsListTabViewModel(this);
        this.statsTab = new DeckEditStatsTabViewModel(this);
        this.notesTab = new DeckEditNotesTabViewModel(this);

        this.notificationCenter.subscribe(SHOW_DECK, (key, payload) -> {
            Object[] params = payload.clone();
            DeckListItem deckItem = (DeckListItem) params[0];
            showDeck(deckItem);
        });
    }

    public void showDeck(DeckListItem deckItem){
        Session session = this.dbManager.getSession();
        this.currentDeck = session.get(Deck.class, deckItem.getDeckId());
        this.currentDeckItem = deckItem;
        if(this.currentDeck != null){
            this.generalTab.setDeck(this.currentDeck, this.currentDeckItem);
            this.cardsListTab.setDeck(this.currentDeck, this.currentDeckItem);
            this.statsTab.setDeck(this.currentDeck, this.currentDeckItem);
            this.notesTab.setDeck(this.currentDeck, this.currentDeckItem);

            String title = "Deck " + this.currentDeck.getName();
            this.setTitle(title);

            this.publish(SHOW_DECK, title);
        }
    }



    public void backHandler(){
        this.saveDeck();
        this.publish(BACK);
        this.notificationCenter.publish(DeckListViewModel.REFRESH_LIST);
    }

    protected void saveDeck(){
        boolean isDirty = false;
        isDirty = isDirty || this.generalTab.saveDeck();
        isDirty = isDirty || this.notesTab.saveDeck();


        if(isDirty){
            Session session = this.dbManager.getSession();
            session.beginTransaction();
            session.update(this.currentDeck);
            session.getTransaction().commit();
        }
        this.currentDeckItem.setName(this.currentDeck.getName());
        this.currentDeckItem.setSummary(this.currentDeck.getSummary());

        List<String> tags = this.statsTab.getTagList();
        String tagsString = String.join(",", tags);
        this.currentDeckItem.setTags(tagsString);
    }



    public void showCardDetails(DeckCardListItem card){
        this.notificationCenter.publish(CardDetailsViewModel.SHOW_CARD, card.getDbfId());
    }
}
