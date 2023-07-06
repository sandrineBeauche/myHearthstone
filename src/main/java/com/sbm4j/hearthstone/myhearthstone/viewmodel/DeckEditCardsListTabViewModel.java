package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.List;

public class DeckEditCardsListTabViewModel extends AbstractDeckEditTabViewModel{

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


    protected DBFacade dbFacade;

    protected HashMap<String, String> extensionTooltips = new HashMap<>();

    protected HashMap<String, String> rarityTooltips = new HashMap<>();


    public DeckEditCardsListTabViewModel(DeckEditViewModel mainViewModel){
        super(mainViewModel);
        this.dbFacade = this.mainViewModel.getDbFacade();

        this.cardsList.set(FXCollections.observableArrayList());

        for(CardSet current: this.dbFacade.getSets(false)){
            this.extensionTooltips.put(current.getCode(), current.getName());
        }

        for(Rarity current: this.dbFacade.getRarities(false)){
            this.rarityTooltips.put(current.getCode(), current.getName());
        }
    }



    public String getExtensionTooltips(String code){
        return this.extensionTooltips.get(code);
    }

    public String getRarityTooltips(String code){
        return this.rarityTooltips.get(code);
    }

    public void refresh(){
        if(!this.refreshed) {
            this.refreshed = true;
            this.setIsStandard(currentDeckItem.getNbCards() == currentDeckItem.getNbStandardCards());
            this.setIsValid(currentDeckItem.getNbCards() == 30 && currentDeckItem.getNbCardsInCollection() == 30);

            List<DeckCardListItem> items = this.dbFacade.getDeckCardList(currentDeck);

            this.getCardsList().clear();
            this.getCardsList().addAll(items);
            this.refreshNbcards();

            this.mainViewModel.getStatsTab().refresh();
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
            this.updateDeckListItem(newCardItem,1);
            this.setIsStandard(this.currentDeckItem.isStandard());
            this.mainViewModel.getStatsTab().update(newCardItem, 1);
            this.mainViewModel.getNotificator().notifyAddCardToDeckSuccess(newCardItem.getName(), this.currentDeck.getName());
        }
        else{
            this.mainViewModel.getNotificator().notifyAddCardToDeckError(dbfId, this.currentDeck.getName());
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
            this.mainViewModel.getStatsTab().update(oldItem, delta);
            this.updateDeckListItem(oldItem, delta);
            this.setIsStandard(this.currentDeckItem.isStandard());
            this.mainViewModel.getNotificator().notifyRemoveCardFromDeckSuccess(cardName, this.currentDeck.getName());
        }
        else{
            this.mainViewModel.getNotificator().notifyRemoveCardFromDeckError(Integer.toString(dbfId), this.currentDeck.getName());
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
}
