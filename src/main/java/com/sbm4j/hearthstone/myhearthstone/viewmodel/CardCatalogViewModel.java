package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.services.db.CatalogCriteria;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.imports.ImportCatalogAction;
import com.sbm4j.hearthstone.myhearthstone.services.imports.ImportCollectionAction;
import com.sbm4j.hearthstone.myhearthstone.services.imports.JSONCardImporter;
import com.sbm4j.hearthstone.myhearthstone.views.Dialogs;
import com.sbm4j.hearthstone.myhearthstone.views.ManaOption;
import com.sbm4j.hearthstone.myhearthstone.views.ParamCommand;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.IndexedCheckModel;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CardCatalogViewModel implements ViewModel, Initializable {

    @Inject
    protected DBFacade dbFacade;

    @Inject
    protected Injector injector;

    protected Logger logger = LogManager.getLogger();

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

    /* SelectedInCollection property */
    private BooleanProperty selectedInCollection = new SimpleBooleanProperty();
    public BooleanProperty getSelectedInCollectionProperty(){return this.selectedInCollection;}
    public Boolean getSelectedInCollection(){return this.selectedInCollection.get();}
    public void setSelectedInCollection(Boolean value){this.selectedInCollection.set(value);}

    /* AvailableTags property */
    private ObjectProperty<ObservableList<CardTag>> availableTags = new SimpleObjectProperty<ObservableList<CardTag>>();
    public ObjectProperty<ObservableList<CardTag>> getAvailableTagsProperty(){ return this.availableTags; }
    public ObservableList<CardTag> getAvailableTags(){ return this.availableTags.get();}
    public void setAvailableTags(ObservableList<CardTag> value){ this.availableTags.set(value);}


    /* CheckModelTags property */
    private ObjectProperty<IndexedCheckModel<CardTag>> checkModelTags = new SimpleObjectProperty<IndexedCheckModel<CardTag>>();
    public ObjectProperty<IndexedCheckModel<CardTag>> getCheckModelTagsProperty(){return this.checkModelTags;}
    public IndexedCheckModel<CardTag> getCheckModelTags(){return this.checkModelTags.get();}
    public void setCheckModelTags(IndexedCheckModel<CardTag> value){this.checkModelTags.set(value);}


    /* nbCardsTxt property */
    private StringProperty nbCardsTxt = new SimpleStringProperty();
    public StringProperty getNbCardsTxtProperty(){return this.nbCardsTxt;}
    public String getNbCardsTxt(){return this.nbCardsTxt.get();}
    public void setNbCardsTxt(String value){this.nbCardsTxt.set(value);}


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<CardClass> classes = this.dbFacade.getClasses(false);
        this.availableClasses.set(FXCollections.observableArrayList(classes));
        if(classes.size() > 0) {
            this.setSelectedCardClass(classes.get(0));
        }

        List<CardSet> sets = this.dbFacade.getSets(true);
        this.availableSets.set(FXCollections.observableArrayList(sets));
        if(sets.size() > 0) {
            this.setSelectedSet(sets.get(0));
        }

        List<Rarity> rarities = this.dbFacade.getRarities(true);
        this.availableRarity.set(FXCollections.observableArrayList(rarities));
        if(rarities.size() > 0) {
            this.setSelectedRarity(rarities.get(0));
        }

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

        List<CardTag> tags = this.dbFacade.getTags();
        this.availableTags.set(FXCollections.observableArrayList(tags));

        this.setSelectedInCollection(true);

        CatalogCriteria criteria = new CatalogCriteria(
                this.selectedClass.get(),
                this.selectedSet.get(),
                this.selectedRarity.get(), manas[0], this.getSelectedInCollection(), null);
        List<CardCatalogItem> items = this.dbFacade.getCatalog(criteria);
        this.collection.set(FXCollections.observableArrayList(items));
        this.setNbCardsTxt(items.size() + " cartes");
    }


    protected ParamCommand importCatalogCommand = new ParamCommand(
            "Importer le catalogue",
            () -> (Action) this.injector.getInstance(ImportCatalogAction.class), true);
    public ParamCommand getImportCatalogCommand(){ return this.importCatalogCommand;}


    protected ParamCommand importCollectionCommand = new ParamCommand(
            "Importer la collection utilisateur",
            () -> (Action) this.injector.getInstance(ImportCollectionAction.class), true);
    public ParamCommand getImportCollectionCommand(){ return this.importCollectionCommand;}

    public void showCardDetails(CardCatalogItem item){
        CardDetail details = this.getDetails(item);
        try {
            Dialogs.showCardDetailDialog(details);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }


    public void refreshCatalog(){
        CatalogCriteria criteria = new CatalogCriteria(
                this.getSelectedClass(),
                this.getSelectedSet(),
                this.getSelectedRarity(),
                this.getSelectedMana(),
                this.getSelectedInCollection(),
                this.checkModelTags.get().getCheckedItems()
        );
        List<CardCatalogItem> items = this.dbFacade.getCatalog(criteria);
        ObservableList<CardCatalogItem> coll = this.getCollection();
        coll.clear();
        coll.addAll(items);
        this.setNbCardsTxt(items.size() + " cartes");
    }

    public void resetTags(){
        this.getCheckModelTags().clearChecks();
        this.refreshCatalog();
    }

    public CardDetail getDetails(CardCatalogItem item){
        return this.dbFacade.getCardFromDbfId(item.dbfId());
    }
}
