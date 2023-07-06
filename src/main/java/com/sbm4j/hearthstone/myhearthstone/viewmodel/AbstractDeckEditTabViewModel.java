package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.sbm4j.hearthstone.myhearthstone.model.Deck;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;

public class
AbstractDeckEditTabViewModel extends AbstractTabViewModel{

    protected Deck currentDeck;

    protected DeckListItem currentDeckItem;

    protected DeckEditViewModel mainViewModel;

    public AbstractDeckEditTabViewModel(DeckEditViewModel mainViewModel){
        this.mainViewModel = mainViewModel;
    }

    public void setDeck(Deck currentDeck, DeckListItem currentDeckItem){
        this.currentDeck = currentDeck;
        this.currentDeckItem = currentDeckItem;
        this.refreshed = false;
    }
}
