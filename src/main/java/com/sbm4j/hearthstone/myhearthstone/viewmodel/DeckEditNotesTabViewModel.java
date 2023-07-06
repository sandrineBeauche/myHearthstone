package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DeckEditNotesTabViewModel extends AbstractDeckEditTabViewModel{

    /* notes property */
    private StringProperty notes = new SimpleStringProperty();
    public StringProperty getNotesProperty(){return this.notes;}
    public String getNotes(){return this.notes.get();}
    public void setNotes(String value){this.notes.set(value);}

    public DeckEditNotesTabViewModel(DeckEditViewModel mainViewModel) {
        super(mainViewModel);
    }


    public void refresh(){
        if(!this.refreshed){
            this.refreshed = true;

            String notesValue = this.currentDeck.getNotes();
            this.setNotes(notesValue);
        }
    }

    public boolean saveDeck(){
        if(this.currentDeck.getNotes() == null){
            if(this.getNotes() != null){
                this.currentDeck.setNotes(this.getNotes());
            }
        }
        else{
            if(!this.currentDeck.getNotes().equals(this.getNotes())){
                this.currentDeck.setNotes(this.getNotes());
                return true;
            }
        }
        return false;
    }
}
