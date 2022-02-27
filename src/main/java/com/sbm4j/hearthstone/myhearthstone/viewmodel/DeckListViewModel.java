package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sbm4j.hearthstone.myhearthstone.model.Deck;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.model.Hero;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.services.imports.DeckStringExporter;
import com.sbm4j.hearthstone.myhearthstone.services.imports.DeckStringImporter;
import com.sbm4j.hearthstone.myhearthstone.services.imports.DeckStringImporterImpl;
import com.sbm4j.hearthstone.myhearthstone.utils.NotificationsUtil;
import com.sbm4j.hearthstone.myhearthstone.views.Dialogs;
import com.sbm4j.hearthstone.myhearthstone.views.ParamCommand;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeckListViewModel implements ViewModel, Initializable {

    public static final String REFRESH_LIST = "REFRESH_LIST";

    @Inject
    protected DBFacade dbFacade;

    @Inject
    protected Injector injector;

    protected Logger logger = LogManager.getLogger();

    /* items property */
    private ObjectProperty<ObservableList<DeckListItem>> items = new SimpleObjectProperty<ObservableList<DeckListItem>>();
    public ObjectProperty<ObservableList<DeckListItem>> getItemsProperty() {return this.items;}
    public ObservableList<DeckListItem> getItems(){return this.items.get();}
    public void setItems(ObservableList<DeckListItem> value){this.items.set(value);}

    /* selectionModel property */
    private ObjectProperty<TableView.TableViewSelectionModel<DeckListItem>> selectionModel =
            new SimpleObjectProperty<TableView.TableViewSelectionModel<DeckListItem>>();
    public ObjectProperty<TableView.TableViewSelectionModel<DeckListItem>> selectionModelProperty() {
        return selectionModel;
    }
    public TableView.TableViewSelectionModel<DeckListItem> getSelectionModel() {
        return selectionModel.get();
    }
    public void setSelectionModel(TableView.TableViewSelectionModel<DeckListItem> value){
        this.selectionModel.set(value);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.items.set(FXCollections.observableArrayList());
        List<DeckListItem> decks = this.dbFacade.getDeckList();
        if(decks != null){
            this.getItems().addAll(decks);
        }
    }


    public ParamCommand getDuplicateDeckCommand(){return this.duplicateDeckCommand;}
    protected ParamCommand duplicateDeckCommand = new ParamCommand("Dupliquer un deck",
            () -> new Action(){
        @Override
        protected void action() {
            HashMap<String, Object> params = duplicateDeckCommand.getParameters();

            String name = (String) params.get("name");
            String deckName = (String) params.get("deckName");
            int deckId = (int) params.get("deckId");
                try {
                    Deck newDeck = dbFacade.duplicateDeck(deckId, name);
                    DeckListItem item = dbFacade.getDeckListItem(newDeck.getId());
                    getItems().add(item);
                    duplicateDeckCommand.setNotificationMessage("Le deck " + deckName + " a été dupliqué avec succès avec le nom " + name);
                }
                catch(PersistenceException ex){
                    duplicateDeckCommand.setNotificationMessage("Il existe déjà un deck avec le nom " + name + ". ");
                    throw ex;
                }
                catch(Exception ex2){
                    duplicateDeckCommand.setNotificationMessage("Erreur lors de la duplication du deck  " + deckName + ". ");
                    throw ex2;
                }

        }
    });



    public ParamCommand getDeleteCommand(){return this.deleteDeckCommand;}
    protected ParamCommand deleteDeckCommand = new ParamCommand("Supprimer un deck",
            () -> new Action(){
        @Override
        protected void action() throws Exception {
            DeckListItem selected = getSelectionModel().getSelectedItem();
            String name = selected.getName();
            boolean resultDelete = dbFacade.deleteDeck(selected.getDeckId());
            if(resultDelete){
                getItems().remove(selected);
                deleteDeckCommand.setNotificationMessage("Le deck " + name + " a été supprimé avec succès.");
            }
            else {
                deleteDeckCommand.setNotificationMessage("Erreur de la suppression du deck " + name + ".");
                throw new Exception();
            }
        }
    });



    public ParamCommand getCreateNewDeckCommand(){return this.createNewDeckCommand;}
    protected ParamCommand createNewDeckCommand = new ParamCommand("Créer un deck", () -> new Action(){
        @Override
        protected void action() throws Exception {
            String name = (String) createNewDeckCommand.getParameters().get("name");
            Hero hero = (Hero) createNewDeckCommand.getParameters().get("hero");

            logger.info("Create a new deck " + name + " for hero " + hero.toString());
            Deck newDeck = dbFacade.createDeck(name, hero);
            if(newDeck != null) {
                DeckListItem item = new DeckListItem(newDeck.getId(), name, null, hero.getCode(), 0L, 0L, 0L, null);
                getItems().add(item);
                createNewDeckCommand.setNotificationMessage("Le deck " + name + " crée avec succès");
            }
            else{
                createNewDeckCommand.setNotificationMessage("Erreur lors de la création du deck " + name);
                throw new Exception();
            }
        }
    });

    public ParamCommand getImportDeckstringCommand(){return this.importDeckstringCommand;}
    protected ParamCommand importDeckstringCommand = new ParamCommand("Importer un deck",
            () -> new Action() {
                @Override
                protected void action() throws Exception {
                    HashMap<String, Object> parameters = importDeckstringCommand.getParameters();
                    String deckstring = (String) parameters.get("deckstring");
                    String deckName = (String) parameters.get("deckName");

                    DeckStringImporter importer = injector.getInstance(DeckStringImporter.class);
                    try {
                        DeckListItem newItem = importer.importDeckString(deckstring, deckName);
                        getItems().add(newItem);
                        importDeckstringCommand.setNotificationMessage("Le deck " + deckName + " a été importé avec succès");
                    }
                    catch(Exception ex){
                        importDeckstringCommand.setNotificationMessage("Erreur lors de l'importation du deck " + deckName);
                        throw ex;
                    }
                }
            });

    public ParamCommand getExportDeckstringCommand(){return this.exportDeckstringCommand;}
    protected ParamCommand exportDeckstringCommand = new ParamCommand("Exporter un deck",
            () -> new Action() {
                @Override
                protected void action() throws Exception {
                    DeckListItem selected = getSelectionModel().getSelectedItem();

                    DeckStringExporter exporter = injector.getInstance(DeckStringExporter.class);
                    try {
                        String deckstring = exporter.export(selected);
                        Clipboard clipboard = Clipboard.getSystemClipboard();
                        ClipboardContent content = new ClipboardContent();
                        content.putString(deckstring);
                        clipboard.setContent(content);
                        exportDeckstringCommand.setNotificationMessage("Le deck " + selected.getName() + " a été exporté avec succès dans le press-papier");
                    }
                    catch(Exception ex){
                        exportDeckstringCommand.setNotificationMessage("Erreur lors de l'exportation du deck " + selected.getName());
                        throw ex;
                    }
                }
            });


    public List<Hero> getAvailableHeros(){
        return dbFacade.getHeros();
    }




}
