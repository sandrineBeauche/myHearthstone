package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.Deck;
import com.sbm4j.hearthstone.myhearthstone.model.DeckCardListItem;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DeckEditViewModel implements ViewModel, Initializable {

    /* title property */
    private StringProperty title = new SimpleStringProperty();
    public StringProperty getTitleProperty(){return this.title;}
    public String getTitle(){return this.title.get();}
    public void setTitle(String value){this.title.set(value);}

    /* name property */
    private StringProperty name = new SimpleStringProperty();
    public StringProperty getNameProperty(){return this.name;}
    public String getName(){return this.name.get();}
    public void setName(String value){this.name.set(value);}


    /* summary property */
    private StringProperty summary = new SimpleStringProperty();
    public StringProperty getSummaryProperty(){return this.summary;}
    public String getSummary(){return this.summary.get();}
    public void setSummary(String value){this.summary.set(value);}

    /* heroImg property */
    private ObjectProperty<Image> heroImg = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getHeroImgProperty(){return this.heroImg;}
    public Image getHeroImg(){return this.heroImg.get();}
    public void setHeroImg(Image value){this.heroImg.set(value);}


    /* cardsList property */
    private ObjectProperty<ObservableList<DeckCardListItem>> cardsList = new SimpleObjectProperty<ObservableList<DeckCardListItem>>();
    public ObjectProperty<ObservableList<DeckCardListItem>> getCardsListProperty(){return this.cardsList;}
    public ObservableList<DeckCardListItem> getCardsList(){return this.cardsList.get();}
    public void setCardsList(ObservableList<DeckCardListItem> value){this.cardsList.set(value);}

    /* selectedCardModel property */
    private ObjectProperty<TableView.TableViewSelectionModel<DeckCardListItem>> selectedCardModel = new SimpleObjectProperty<TableView.TableViewSelectionModel<DeckCardListItem>>();
    public ObjectProperty<TableView.TableViewSelectionModel<DeckCardListItem>> getSelectedCardModelProperty(){return this.selectedCardModel;}
    public TableView.TableViewSelectionModel<DeckCardListItem> getSelectedCardModel(){return this.selectedCardModel.get();}
    public void setSelectedCardModel(TableView.TableViewSelectionModel<DeckCardListItem> value){this.selectedCardModel.set(value);}

    /* tabSelectionModel property */
    private ObjectProperty<SingleSelectionModel<Tab>> tabSelectionModel = new SimpleObjectProperty<SingleSelectionModel<Tab>>();
    public ObjectProperty<SingleSelectionModel<Tab>> getTabSelectionModelProperty(){return this.tabSelectionModel;}
    public SingleSelectionModel<Tab> getTabSelectionModel(){return this.tabSelectionModel.get();}
    public void setTabSelectionModel(SingleSelectionModel<Tab> value){this.tabSelectionModel.set(value);}

    /* curveManaData property */
    private ObjectProperty<ObservableList<XYChart.Data<String, Number>>> curveManaData = new SimpleObjectProperty<ObservableList<XYChart.Data<String, Number>>>();
    public ObjectProperty<ObservableList<XYChart.Data<String, Number>>> getCurveManaDataProperty(){return this.curveManaData;}
    public ObservableList<XYChart.Data<String, Number>> getCurveManaData(){return this.curveManaData.get();}
    public void setCurveManaData(ObservableList<XYChart.Data<String, Number>> value){this.curveManaData.set(value);}

    /* statsTagsList property */
    private ObjectProperty<ObservableList<Pair<String, Integer>>> statsTagsList = new SimpleObjectProperty<ObservableList<Pair<String, Integer>>>();
    public ObjectProperty<ObservableList<Pair<String, Integer>>> getStatsTagsListProperty(){return this.statsTagsList;}
    public ObservableList<Pair<String, Integer>> getStatsTagsList(){return this.statsTagsList.get();}
    public void setStatsTagsList(ObservableList<Pair<String, Integer>> value){this.statsTagsList.set(value);}


    @Inject
    protected DBFacade dbFacade;

    @Inject
    protected DBManager dbManager;

    @Inject
    protected ImageManager imageManager;


    protected Deck currentDeck;

    protected static Logger logger = LogManager.getLogger();

    protected Boolean [] refreshed;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.cardsList.set(FXCollections.observableArrayList());
        this.statsTagsList.set(FXCollections.observableArrayList());
        this.refreshed = new Boolean[]{false, false, false};
        this.getTabSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.refreshCurrentTab();
        });

        this.setCurveManaData(FXCollections.observableArrayList());
        for (int i = 0; i < 7; i++) {
            XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(Integer.toString(i), 0);
            this.getCurveManaData().add(data);
        }
        this.getCurveManaData().add(new XYChart.Data<String, Number>("7+", 0));
    }

    public void showDeck(DeckListItem deckItem){
        Session session = this.dbManager.getSession();
        this.currentDeck = session.get(Deck.class, deckItem.getDeckId());
        if(this.currentDeck != null){
            this.refreshed[0] = false;
            this.refreshed[1] = false;
            this.refreshed[2] = false;

            this.setTitle("Deck " + this.currentDeck.getName());
            this.getTabSelectionModel().select(0);
            this.refreshCurrentTab();
        }
    }

    public void refreshCurrentTab(){
        int selected = this.getTabSelectionModel().getSelectedIndex();
        if(this.currentDeck != null && !this.refreshed[selected]) {
            switch (selected){
                case 0 -> this.refreshGeneralTab();
                case 1 -> this.refreshCardListTab();
                case 2 -> this.refreshStatsTab();
            }
        }
    }


    protected void refreshGeneralTab() {
        this.refreshed[0] = true;
        this.setName(this.currentDeck.getName());
        this.setSummary(this.currentDeck.getSummary());

        String heroCode = this.currentDeck.getHero().getCode();
        try {
            Image heroImg = this.imageManager.getHeroPortrait(heroCode);
            this.setHeroImg(heroImg);
        } catch (FileNotFoundException e) {
            logger.error("Image file not found for hero " + heroCode);
        }
    }

    protected void refreshCardListTab(){
        this.refreshed[1] = true;
        List<DeckCardListItem> items = this.dbFacade.getDeckCardList(this.currentDeck);

        this.getCardsList().clear();
        this.getCardsList().addAll(items);
    }

    protected void refreshStatsTab(){
        this.refreshed[2] = true;
        Integer [] curveData = this.dbFacade.getManaCurveStats(this.currentDeck);

        for (int i = 0; i < curveData.length; i++) {
            this.getCurveManaData().get(i).setYValue(curveData[i]);
        }

        List<Pair<String, Integer>> stats = this.dbFacade.getTagsStats(this.currentDeck);
        this.getStatsTagsList().addAll(stats);
    }

    
}
