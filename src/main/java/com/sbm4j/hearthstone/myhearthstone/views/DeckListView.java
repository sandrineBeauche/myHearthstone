package com.sbm4j.hearthstone.myhearthstone.views;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.DeckListViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeckListView implements FxmlView<DeckListViewModel>, Initializable {

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
    }


    public void duplicateDeckCallback(){
        this.viewModel.getDuplicateDeckCommand().execute();
    }

    public void deleteDeckCallback(){
        this.viewModel.getDeleteCommand().execute();
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
        }
    }
}
