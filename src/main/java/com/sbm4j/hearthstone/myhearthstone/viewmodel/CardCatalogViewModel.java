package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.views.CardCatalogItem;
import com.sbm4j.hearthstone.myhearthstone.views.ManaOption;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CardCatalogViewModel implements ViewModel, Initializable {

    @Inject
    protected DBFacade dbFacade;


    /* Collection property */
    private ObjectProperty<ObservableList<CardCatalogItem>> collection = new SimpleObjectProperty<ObservableList<CardCatalogItem>>();
    public ObjectProperty<ObservableList<CardCatalogItem>> getCollectionProperty(){return this.collection;}
    public ObservableList<CardCatalogItem> getCollection(){return this.collection.get();}
    public void setCollection(ObservableList<CardCatalogItem> col){this.collection.set(col);}

    /* SelectedClass property */
    private ObjectProperty<CardClass> selectedClass = new SimpleObjectProperty<CardClass>();
    public ObjectProperty<CardClass> getSelectedClassProperty(){return this.selectedClass; }
    public CardClass getSelectedClass(){ return this.selectedClass.get(); }
    public void setSelectedCardClass(CardClass value){this.selectedClass.set(value);}


    /* AvailableClasses property */
    private ObjectProperty<ObservableList<CardClass>> availableClasses = new SimpleObjectProperty<ObservableList<CardClass>>();
    public ObjectProperty<ObservableList<CardClass>> getAvailableClassesProperty(){return this.availableClasses;}
    public ObservableList<CardClass> getAvailableClasses(){return this.availableClasses.get();}
    public void setAvailableClasses(ObservableList<CardClass> value){this.availableClasses.set(value);}


    /* SelectedSet property */
    private ObjectProperty<CardSet> selectedSet = new SimpleObjectProperty<CardSet>();
    public ObjectProperty<CardSet> getSelectedSetProperty(){return this.selectedSet;}
    public CardSet getSelectedSet(){return this.selectedSet.get();}
    public void setSelectedSet(CardSet value){this.selectedSet.set(value);}

    /* AvailableSets property */
    private ObjectProperty<ObservableList<CardSet>> availableSets = new SimpleObjectProperty<ObservableList<CardSet>>();
    public ObjectProperty<ObservableList<CardSet>> getAvailableSetsProperty(){return this.availableSets;}
    public ObservableList<CardSet> getAvailableSets(){return this.availableSets.get();}
    public void setAvailableSets(ObservableList<CardSet> value){this.availableSets.set(value);}


    /* AvailableRarity property */
    private ObjectProperty<ObservableList<Rarity>> availableRarity = new SimpleObjectProperty<ObservableList<Rarity>>();
    public ObjectProperty<ObservableList<Rarity>> getAvailableRarityProperty() {return this.availableRarity;}
    public ObservableList<Rarity> getAvailableRarity(){return this.availableRarity.get();}
    public void setAvailableRarity(ObservableList<Rarity> value){ this.availableRarity.set(value);}

    /* SelectedRarity property */
    private ObjectProperty<Rarity> selectedRarity = new SimpleObjectProperty<Rarity>();
    public ObjectProperty<Rarity> getSelectedRarityProperty() { return this.selectedRarity;}
    public Rarity getSelectedRarity(){ return this.selectedRarity.get(); }
    public void setSelectedRarity(Rarity value){ this.selectedRarity.set(value);}


    /* AvailableMana property */
    private ObjectProperty<ObservableList<ManaOption>> availableMana = new SimpleObjectProperty<ObservableList<ManaOption>>();
    public ObjectProperty<ObservableList<ManaOption>> getAvailableManaProperty() {return this.availableMana;}
    public ObservableList<ManaOption> getAvaiManaOptions(){return this.availableMana.get();}
    public void setAvailableMana(ObservableList<ManaOption> value){ this.availableMana.set(value);}

    /* SelectedMana property */
    private ObjectProperty<ManaOption> selectedMana = new SimpleObjectProperty<ManaOption>();
    public ObjectProperty<ManaOption> getSelectedManaProperty(){return this.selectedMana;}
    public ManaOption getSelectedMana(){return this.selectedMana.get();}
    public void setSelectedMana(ManaOption value){this.selectedMana.set(value);}


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<CardClass> classes = this.dbFacade.getClasses();
        this.availableClasses.set(FXCollections.observableArrayList(classes));
        this.setSelectedCardClass(classes.get(0));

        List<CardSet> sets = this.dbFacade.getSets();
        this.availableSets.set(FXCollections.observableArrayList(sets));
        this.setSelectedSet(sets.get(0));

        List<Rarity> rarities = this.dbFacade.getRarities();
        this.availableRarity.set(FXCollections.observableArrayList(rarities));
        this.setSelectedRarity(rarities.get(0));

        ManaOption [] manas = {
                new ManaOption("ALL", "Toutes les cartes", -1),
                new ManaOption("0", "mana = 0", 0),
                new ManaOption("1", "mana = 1", 1),
                new ManaOption("2", "mana = 2", 2),
                new ManaOption("3", "mana = 3", 3),
                new ManaOption("4", "mana = 4", 4),
                new ManaOption("5", "mana = 5", 5),
                new ManaOption("6", "mana = 6", 6),
                new ManaOption("7", "mana >= 7", 7),
        };
        this.availableMana.set(FXCollections.observableArrayList(manas));
        this.setSelectedMana(manas[0]);

        List<CardCatalogItem> items = this.dbFacade.getCatalog();
        this.collection.set(FXCollections.observableArrayList(items));



    }
}
