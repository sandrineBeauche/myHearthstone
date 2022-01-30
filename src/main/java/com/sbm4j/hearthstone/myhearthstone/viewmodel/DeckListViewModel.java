package com.sbm4j.hearthstone.myhearthstone.viewmodel;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.Deck;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.services.db.DBFacade;
import com.sbm4j.hearthstone.myhearthstone.utils.NotificationsUtil;
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
import javafx.scene.control.TextInputDialog;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeckListViewModel implements ViewModel, Initializable {

    @Inject
    protected DBFacade dbFacade;

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

    public Command getDuplicateDeckCommand(){return this.duplicateDeckCommand;}
    protected Command duplicateDeckCommand = new DelegateCommand(() -> new Action(){
        @Override
        protected void action() throws Exception {
            duplicateDeck();
        }
    });
    protected void duplicateDeck(){
        DeckListItem selected = this.getSelectionModel().getSelectedItem();

        TextInputDialog dialog = new TextInputDialog(selected.getName() + "(1)");
        dialog.setTitle("Dupliquer un deck");
        dialog.setHeaderText("Dupliquer le deck " + selected.getName());
        dialog.setContentText("Entrez le nom du nouveau deck: ");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String newName = result.get();
            Deck newDeck = this.dbFacade.duplicateDeck(selected.getDeckId(), newName);
            if(newDeck != null){
                NotificationsUtil.showInfoNotification("Dupliquer un deck",
                        "Duplication effectuée avec succès",
                        "Le deck " + selected.getName() + " a été dupliqué avec succès avec le nom " + newName);
                refreshDeckList();
            }
            else{
                NotificationsUtil.showErrorNotification("Dupliquer un deck",
                        "Oups! Il y a des erreurs",
                        "Erreur de la duplication du deck " + selected.getName() + ". Veuillez consulter les logs pour plus d'informations");
            }
        }
    }


    public Command getDeleteCommand(){return this.deleteDeckCommand;}
    protected Command deleteDeckCommand = new DelegateCommand(() -> new Action(){
        @Override
        protected void action() throws Exception {
            deleteDeck();
        }
    });
    protected void deleteDeck(){
        DeckListItem selected = this.getSelectionModel().getSelectedItem();
        String deckName = selected.getName();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression d'un deck");
        alert.setContentText("Voulez-vous vraiment supprimer le deck " + selected.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            boolean resultDelete = this.dbFacade.deleteDeck(selected.getDeckId());
            if(resultDelete){
                NotificationsUtil.showInfoNotification("Supprimer un deck",
                        "Suppression effectuée avec succès",
                        "Le deck " + deckName + " a été supprimé avec succès.");
                refreshDeckList();
            }
            else {
                NotificationsUtil.showErrorNotification("Supprimer un deck",
                        "Oups! Il y a des erreurs",
                        "Erreur de la suppression du deck " + deckName + ". Veuillez consulter les logs pour plus d'informations");
            }
        }
    }


    protected void refreshDeckList(){
        List<DeckListItem> decks = this.dbFacade.getDeckList();
        this.getItems().clear();
        this.getItems().addAll(decks);
    }
}
