package com.sbm4j.hearthstone.myhearthstone.views;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.model.Hero;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.DeckEditViewModel;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.DeckListViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeckListView implements FxmlView<DeckListViewModel>, Initializable {

    @FXML
    protected TitledPane titledPane;

    @FXML
    protected TableView<DeckListItem> decksTable;

    @FXML
    protected TableColumn<DeckListItem, String> heroColumn;

    @FXML
    protected TableColumn<DeckListItem, String> nameColumn;

    @FXML
    protected TableColumn<DeckListItem, Integer> nbCardsColumn;

    @FXML
    protected TableColumn<DeckListItem, Integer> collectionCardsColumn;

    @FXML
    protected TableColumn<DeckListItem, Integer> standardCardsColumn;

    @FXML
    protected TableColumn<DeckListItem, String> tagsColumn;

    @FXML
    protected TableColumn<DeckListItem, String> summaryColumn;

    @FXML
    protected Button cmdNewButton;

    @FXML
    protected Button cmdEditButton;

    @FXML
    protected Button cmdDuplicateButton;

    @FXML
    protected Button cmdImportButton;

    @FXML
    protected Button cmdExportButton;

    @FXML
    protected Button cmdDeleteButton;


    @InjectViewModel
    protected DeckListViewModel viewModel;

    @Inject
    protected ImageManager imageManager;

    @Inject
    private NotificationCenter notificationCenter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.viewModel.initialize(location, resources);

        this.decksTable.itemsProperty().bindBidirectional(viewModel.getItemsProperty());
        TableView.TableViewSelectionModel selectionModel = this.decksTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.setCellSelectionEnabled(false);
        this.viewModel.setSelectionModel(selectionModel);
        this.decksTable.selectionModelProperty().bindBidirectional(viewModel.selectionModelProperty());

        this.heroColumn.setCellValueFactory(new PropertyValueFactory<DeckListItem,String>("heroCode"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<DeckListItem, String>("name"));
        this.nbCardsColumn.setCellValueFactory(new PropertyValueFactory<DeckListItem, Integer>("nbCards"));
        this.collectionCardsColumn.setCellValueFactory(new PropertyValueFactory<DeckListItem, Integer>("nbCardsInCollection"));
        this.standardCardsColumn.setCellValueFactory(new PropertyValueFactory<DeckListItem, Integer>("nbStandardCards"));
        this.tagsColumn.setCellValueFactory(new PropertyValueFactory<DeckListItem, String>("tags"));
        this.summaryColumn.setCellValueFactory(new PropertyValueFactory<DeckListItem, String>("summary"));

        this.heroColumn.setCellFactory(param -> {return new HeroIconCell();});
        this.nameColumn.setCellFactory(param -> {return CellBuilder.<DeckListItem, String>buildStringCell();});
        this.nbCardsColumn.setCellFactory(param -> {return CellBuilder.<DeckListItem, Integer>buildIntegerCell();});
        this.collectionCardsColumn.setCellFactory(param -> {return CellBuilder.<DeckListItem, Integer>buildIntegerCell();});
        this.standardCardsColumn.setCellFactory(param -> {return CellBuilder.<DeckListItem, Integer>buildIntegerCell();});
        this.tagsColumn.setCellFactory(param -> {return CellBuilder.<DeckListItem, String>buildStringCell();});
        this.summaryColumn.setCellFactory(param -> {return CellBuilder.<DeckListItem, String>buildStringCell();});

        this.notificationCenter.subscribe(DeckListViewModel.REFRESH_LIST, (key, payload) -> {
            decksTable.refresh();
        });
    }

    public void createNewDeckCallback(){
        List<Hero> heroes = this.viewModel.getAvailableHeros();
        Optional<Pair<String, Hero>> params = Dialogs.newDeckDialog(heroes, imageManager);
        params.ifPresent(values -> {
            ParamCommand command = this.viewModel.getCreateNewDeckCommand();
            command.putParameter("name", values.getKey());
            command.putParameter("hero", values.getValue());
            command.execute();
        });
    }

    public void editDeckCallback(){
        this.titledPane.toBack();
        DeckListItem deck = this.viewModel.getSelectionModel().getSelectedItem();
        this.notificationCenter.publish(DeckEditViewModel.SHOW_DECK, deck);
    }

    public void duplicateDeckCallback(){
        DeckListItem selected = this.viewModel.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog(selected.getName() + "(1)");
        dialog.setTitle("Dupliquer un deck");
        dialog.setHeaderText("Dupliquer le deck " + selected.getName());
        dialog.setContentText("Entrez le nom du nouveau deck: ");
        dialog.getDialogPane().getStylesheets().add(Dialogs.getCss());

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newName = result.get();
            ParamCommand command = this.viewModel.getDuplicateDeckCommand();
            command.putParameter("name", newName);
            command.putParameter("deckName", selected.getName());
            command.putParameter("deckId", selected.getDeckId());
            command.execute();
        }
    }

    public void importDeckCallback(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Importer un deck");
        dialog.setHeaderText("Importer un deck depuis le press-papier");
        dialog.setContentText("Entrez le nom du nouveau deck: ");
        dialog.getDialogPane().getStylesheets().add(Dialogs.getCss());

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            if(clipboard.hasString()){
                String newDeckName = result.get();
                ParamCommand command = this.viewModel.getImportDeckstringCommand();
                command.putParameter("deckstring", clipboard.getString());
                command.putParameter("deckName", newDeckName);
                command.execute();
            }
        }
    }

    public void exportDeckCallback(){
        ParamCommand command = this.viewModel.getExportDeckstringCommand();
        command.execute();
    }

    public void deleteDeckCallback(){
        DeckListItem selected = this.viewModel.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression d'un deck");
        alert.setContentText("Voulez-vous vraiment supprimer le deck " + selected.getName());
        alert.getDialogPane().getStylesheets().add(Dialogs.getCss());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            this.viewModel.getDeleteCommand().execute();
        }
    }

    public class HeroIconCell extends TableCell<DeckListItem, String> {

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty){
                try {
                    Image img = imageManager.getHeroIcon(item);
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(40);
                    imgView.setPreserveRatio(true);
                    setGraphic(imgView);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                setGraphic(null);
            }
        }
    }
}
