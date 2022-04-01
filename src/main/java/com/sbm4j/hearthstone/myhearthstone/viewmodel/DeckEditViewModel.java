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
    
    /* deckClassIcon property */
    private ObjectProperty<Image> deckClassIcon = new SimpleObjectProperty<Image>();
    public ObjectProperty<Image> getDeckClassIconProperty(){return this.deckClassIcon;}
    public Image getDeckClassIcon(){return this.deckClassIcon.get();}
    public void setDeckClassIcon(Image value){this.deckClassIcon.set(value);}


    /* cardsList property */
    private ObjectProperty<ObservableList<DeckCardListItem>> cardsList = new SimpleObjectProperty<ObservableList<DeckCardListItem>>();
    public ObjectProperty<ObservableList<DeckCardListItem>> getCardsListProperty(){return this.cardsList;}
    public ObservableList<DeckCardListItem> getCardsList(){return this.cardsList.get();}
    public void setCardsList(ObservableList<DeckCardListItem> value){this.cardsList.set(value);}

    /* nbCardsTotal property */
    private StringProperty nbCardsTotal = new SimpleStringProperty();
    public StringProperty getNbCardsTotalProperty(){return this.nbCardsTotal;}
    public String getNbCardsTotal(){return this.nbCardsTotal.get();}
    public void setNbCardsTotal(String value){this.nbCardsTotal.set(value);}


    /* curveManaData property */
    private ObjectProperty<ObservableList<XYChart.Data<String, Number>>> curveManaData = new SimpleObjectProperty<ObservableList<XYChart.Data<String, Number>>>();
    public ObjectProperty<ObservableList<XYChart.Data<String, Number>>> getCurveManaDataProperty(){return this.curveManaData;}
    public ObservableList<XYChart.Data<String, Number>> getCurveManaData(){return this.curveManaData.get();}
    public void setCurveManaData(ObservableList<XYChart.Data<String, Number>> value){this.curveManaData.set(value);}

    /* statsTagsList property */
    private ObjectProperty<ObservableList<TagStat>> statsTagsList = new SimpleObjectProperty<ObservableList<TagStat>>();
    public ObjectProperty<ObservableList<TagStat>> getStatsTagsListProperty(){return this.statsTagsList;}
    public ObservableList<TagStat> getStatsTagsList(){return this.statsTagsList.get();}
    public void setStatsTagsList(ObservableList<TagStat> value){this.statsTagsList.set(value);}

    /* isStandard property */
    private BooleanProperty isStandard = new SimpleBooleanProperty();
    public BooleanProperty getIsStandardProperty(){return this.isStandard;}
    public Boolean getIsStandard(){return this.isStandard.get();}
    public void setIsStandard(Boolean value){this.isStandard.set(value);}

    /* isValid property */
    private BooleanProperty isValid = new SimpleBooleanProperty();
    public BooleanProperty getIsValidProperty(){return this.isValid;}
    public Boolean getIsValid(){return this.isValid.get();}
    public void setIsValid(Boolean value){this.isValid.set(value);}

    @Inject
    protected DBFacade dbFacade;

    @Inject
    protected DBManager dbManager;

    @Inject
    protected ImageManager imageManager;

    @Inject
    protected Notificator notificator;

    @Inject
    private NotificationCenter notificationCenter;


    protected Deck currentDeck;

    protected DeckListItem currentDeckItem;

    protected static Logger logger = LogManager.getLogger();

    protected Boolean [] refreshed;
    public Boolean [] getRefreshed(){ return this.refreshed;}

    protected HashMap<String, String> extensionTooltips = new HashMap<>();

    protected HashMap<String, String> rarityTooltips = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.cardsList.set(FXCollections.observableArrayList());
        this.statsTagsList.set(FXCollections.observableArrayList());
        this.refreshed = new Boolean[]{false, false, false};

        this.setCurveManaData(FXCollections.observableArrayList());
        for (int i = 0; i < 7; i++) {
            XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(Integer.toString(i), 0);
            this.getCurveManaData().add(data);
        }
        this.getCurveManaData().add(new XYChart.Data<String, Number>("7+", 0));

        for(CardSet current: this.dbFacade.getSets(false)){
            this.extensionTooltips.put(current.getCode(), current.getName());
        }

        for(Rarity current: this.dbFacade.getRarities(false)){
            this.rarityTooltips.put(current.getCode(), current.getName());
        }

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
            this.refreshed[0] = false;
            this.refreshed[1] = false;
            this.refreshed[2] = false;

            String title = "Deck " + this.currentDeck.getName();
            this.setTitle(title);
            this.setIsStandard(deckItem.getNbCards() == deckItem.getNbStandardCards());
            this.setIsValid(deckItem.getNbCards() == 30 && deckItem.getNbCardsInCollection() == 30);
            this.publish(SHOW_DECK, title);
        }
    }


    public void refreshGeneralTab() {
        if(!refreshed[0]) {
            this.refreshed[0] = true;
            this.setName(this.currentDeck.getName());
            this.setSummary(this.currentDeck.getSummary());

            String heroCode = this.currentDeck.getHero().getCode();
            try {
                Image heroImg = this.imageManager.getHeroPortrait(heroCode);
                this.setHeroImg(heroImg);
                Image classeImg = CardClassImageLoader.getImage(this.currentDeck.getHero().getClasse().getCode());
                this.setDeckClassIcon(classeImg);
            } catch (FileNotFoundException e) {
                logger.error("Image file not found for hero " + heroCode);
            }
        }
    }

    public void refreshCardListTab(){
        if(!refreshed[1]) {
            this.refreshed[1] = true;
            List<DeckCardListItem> items = this.dbFacade.getDeckCardList(this.currentDeck);

            this.getCardsList().clear();
            this.getCardsList().addAll(items);
            this.refreshNbcards();

            this.refreshStatsTab();
        }
    }

    protected void refreshNbcards(){
        int nbCards = this.currentDeckItem.getNbCards();
        if(nbCards > 1){
            this.setNbCardsTotal(nbCards + " cartes");
        }
        else{
            this.setNbCardsTotal(nbCards + " carte");
        }
    }

    public void refreshStatsTab(){
        if(!this.refreshed[2]) {
            this.refreshed[2] = true;
            Integer[] curveData = this.dbFacade.getManaCurveStats(this.currentDeck);

            for (int i = 0; i < curveData.length; i++) {
                this.getCurveManaData().get(i).setYValue(curveData[i]);
            }

            this.getStatsTagsList().clear();
            List<TagStat> stats = this.dbFacade.getTagsStats(this.currentDeck);
            this.getStatsTagsList().addAll(stats);
        }
    }

    protected DeckCardListItem getItemFromCardList(int dbfId){
        return this.getCardsList().stream().filter(item -> item.getDbfId() == dbfId)
                .findAny().orElse(null);
    }

    protected DeckCardListItem updateCardListItem(DeckCardListItem oldItem, int dbfId, int deltaValue) {
        if(oldItem != null){
            int currentValue = oldItem.getNbCards();
            int newValue = currentValue + deltaValue;
            if(newValue <= 0){
                this.getCardsList().remove(oldItem);
                return null;
            }
            else{
                oldItem.setNbCards(currentValue + deltaValue);
                return oldItem;
            }
        }
        else{
            try{
                DeckCardListItem newCardItem = this.dbFacade.getDeckCardListItem(this.currentDeck, dbfId);
                this.getCardsList().add(newCardItem);
                return newCardItem;
            }
            catch(NoResultException ex){
                return null;
            }
        }
    }

    protected void updateManaCurve(DeckCardListItem item, int nbDelta){
        int index = item.getMana();
        if(index > 7){
            index = 7;
        }

        XYChart.Data<String, Number> data = this.getCurveManaData().get(index);
        int newValue = data.getYValue().intValue() + nbDelta;
        if(newValue < 0){
            newValue = 0;
        }
        data.setYValue(newValue);
    }

    protected void updateTagsStats(DeckCardListItem item, int nbDelta){
        String tagsString = item.getTags();
        if(tagsString != ""){
            String [] tags = tagsString.split(",");
            for(String currentTag: tags){
                TagStat stat = this.getStatsTagsList().stream().filter(t -> t.getTag().equals(currentTag)).findAny().orElse(null);
                if(stat == null){
                    TagStat newTagStat = new TagStat(currentTag, nbDelta);
                    this.getStatsTagsList().add(newTagStat);
                }
                else{
                    int newValue = stat.getValue() + nbDelta;
                    if(newValue > 0){
                        stat.setValue(newValue);
                    }
                    else{
                        this.getStatsTagsList().remove(stat);
                    }
                }
            }
        }
    }


    protected void updateDeckListItem(DeckCardListItem item, int nbDelta){
        this.currentDeckItem.setNbCards(this.currentDeckItem.getNbCards() + nbDelta);
        this.refreshNbcards();
        if(item.getNbCardsInCollection() >= item.getNbCards()){
            this.currentDeckItem.setNbCardsInCollection(this.currentDeckItem.getNbCardsInCollection() + nbDelta);
        }
        if(item.isStandard()){
            this.currentDeckItem.setNbStandardCards(this.currentDeckItem.getNbStandardCards() + nbDelta);
        }
    }


    public void addCardFromDbfId(int dbfId){
        logger.info("Add card with dbfId " + dbfId + " to deck " + this.currentDeck.getName() + "(" + this.currentDeck.getId() + ")");
        DeckCardListItem oldItem = this.getItemFromCardList(dbfId);

        boolean result = this.dbFacade.addCardToDeck(dbfId, this.currentDeck);
        if(result){
            DeckCardListItem newCardItem = this.updateCardListItem(oldItem, dbfId,1);
            this.updateManaCurve(newCardItem, 1);
            this.updateTagsStats(newCardItem, 1);
            this.updateDeckListItem(newCardItem,1);
            this.setIsStandard(this.currentDeckItem.isStandard());
            this.notificator.notifyAddCardToDeckSuccess(newCardItem.getName(), this.currentDeck.getName());
        }
        else{
            this.notificator.notifyAddCardToDeckError(dbfId, this.currentDeck.getName());
        }
    }

    public void removeCardFromDbfId(int dbfId, boolean all){
        logger.info("Remove card with dbfId " + dbfId + " from deck " + this.currentDeck.getName() + "(" + this.currentDeck.getId() + ")");
        DeckCardListItem oldItem = this.getItemFromCardList(dbfId);

        boolean result = this.dbFacade.removeCardFromDeck(dbfId, this.currentDeck, all);
        if(result){
            int delta = 0;
            if(all){
                delta = -oldItem.getNbCards();
            }
            else{
                delta = -1;
            }
            DeckCardListItem newCardItem = this.updateCardListItem(oldItem, dbfId, delta);
            String cardName = oldItem.getName();
            this.updateManaCurve(oldItem, delta);
            this.updateTagsStats(oldItem, delta);
            this.updateDeckListItem(oldItem, delta);
            this.setIsStandard(this.currentDeckItem.isStandard());
            this.notificator.notifyRemoveCardFromDeckSuccess(cardName, this.currentDeck.getName());
        }
        else{
            this.notificator.notifyRemoveCardFromDeckError(Integer.toString(dbfId), this.currentDeck.getName());
        }
    }

    public void incrSelectedCard(DeckCardListItem selected){
        if(selected != null) {
            int dbfId = selected.getDbfId();
            this.addCardFromDbfId(dbfId);
        }
    }

    public void decrSelectedCard(DeckCardListItem selected){
        if(selected != null){
            int dbfId = selected.getDbfId();
            this.removeCardFromDbfId(dbfId, false);
        }
    }

    public void deleteSelectedCard(DeckCardListItem selected){
        if(selected != null){
            int dbfId = selected.getDbfId();
            this.removeCardFromDbfId(dbfId, true);
        }
    }

    public void backHandler(){
        this.saveDeck();
        this.publish(BACK);
        this.notificationCenter.publish(DeckListViewModel.REFRESH_LIST);
    }

    protected void saveDeck(){
        boolean isDirty = false;
        if(!this.getName().equals(this.currentDeckItem.getName())){
            this.currentDeck.setName(this.getName());
            isDirty = true;
        }

        if(this.getSummary() == null){
            if(this.currentDeckItem.getSummary() != this.getSummary()){
                this.currentDeck.setSummary(this.getSummary());
                isDirty = true;
            }
        }
        else{
            if(!this.getSummary().equals(this.currentDeckItem.getSummary())){
                this.currentDeck.setSummary(this.getSummary());
                isDirty = true;
            }
        }

        if(isDirty){
            Session session = this.dbManager.getSession();
            session.beginTransaction();
            session.update(this.currentDeck);
            session.getTransaction().commit();
        }
        this.currentDeckItem.setName(this.currentDeck.getName());
        this.currentDeckItem.setSummary(this.currentDeck.getSummary());

        List<String> tags = this.getStatsTagsList().stream().map(t -> t.getTag()).toList();
        String tagsString = String.join(",", tags);
        this.currentDeckItem.setTags(tagsString);
    }

    public String getExtensionTooltips(String code){
        return this.extensionTooltips.get(code);
    }

    public String getRarityTooltips(String code){
        return this.rarityTooltips.get(code);
    }

    public void showCardDetails(DeckCardListItem card){
        CardDetail details = this.dbFacade.getCardFromDbfId(card.getDbfId());
        if(details != null){
            try {
                Dialogs.showCardDetailDialog(details);
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
