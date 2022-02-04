package com.sbm4j.hearthstone.myhearthstone.views;


import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.DeckCardListItem;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.DeckEditViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeckEditView implements FxmlView<DeckEditViewModel>, Initializable {

    @FXML
    protected TitledPane titledPane;

    @FXML
    protected TextField nameField;

    @FXML
    protected TextArea summaryField;

    @FXML
    protected ImageView heroImage;

    @FXML
    protected TableView<DeckCardListItem> cardList;

    @FXML
    protected TableColumn<DeckCardListItem, String> cardList_RarityCol;

    @FXML
    protected TableColumn<DeckCardListItem, Integer> cardList_ManaCol;

    @FXML
    protected TableColumn<DeckCardListItem, String> cardList_ImageCol;

    @FXML
    protected TableColumn<DeckCardListItem, String> cardList_NameCol;

    @FXML
    protected TableColumn<DeckCardListItem, Integer> cardList_nbCardsCol;

    @FXML
    protected TableColumn<DeckCardListItem, Integer> cardList_nbCollectionCol;

    @FXML
    protected TableColumn<DeckCardListItem, Boolean> cardList_standardCol;

    @FXML
    protected TableColumn<DeckCardListItem, String> cardList_tagsCol;

    @FXML
    protected TableColumn<DeckCardListItem, String> cardList_extCol;

    @FXML
    protected TabPane tabPane;

    @FXML
    protected BarChart<String, Number> manaCurveChart;

    @FXML
    protected TableView<Pair<String, Integer>> statsTagsList;

    @FXML
    protected TableColumn<Pair<String, Integer>, String> tagNameCol;

    @FXML
    protected TableColumn<Pair<String, Integer>, Integer> nbTagCardCol;


    @InjectViewModel
    protected DeckEditViewModel viewModel;

    @Inject
    protected CardImageManager cardImageManager;

    @Inject
    protected ImageManager imageManager;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.viewModel.setSelectedCardModel(this.cardList);
        SingleSelectionModel<Tab> tabSelectionModel = this.tabPane.getSelectionModel();
        this.viewModel.setTabSelectionModel(tabSelectionModel);
        this.viewModel.initialize(location, resources);

        this.titledPane.textProperty().bindBidirectional(this.viewModel.getTitleProperty());
        this.nameField.textProperty().bindBidirectional(this.viewModel.getNameProperty());
        this.summaryField.textProperty().bindBidirectional(this.viewModel.getSummaryProperty());
        this.heroImage.imageProperty().bindBidirectional(this.viewModel.getHeroImgProperty());
        this.cardList.itemsProperty().bindBidirectional(this.viewModel.getCardsListProperty());
        this.cardList.selectionModelProperty().bindBidirectional(this.viewModel.getSelectedCardModelProperty());
        this.tabPane.selectionModelProperty().bindBidirectional(this.viewModel.getTabSelectionModelProperty());
        this.statsTagsList.itemsProperty().bindBidirectional(this.viewModel.getStatsTagsListProperty());

        this.cardList_RarityCol.setCellValueFactory(new PropertyValueFactory<DeckCardListItem, String>("rarityCode"));
        this.cardList_ManaCol.setCellValueFactory(new PropertyValueFactory<DeckCardListItem, Integer>("mana"));
        this.cardList_ImageCol.setCellValueFactory(new PropertyValueFactory<DeckCardListItem, String>("id"));
        this.cardList_NameCol.setCellValueFactory(new PropertyValueFactory<DeckCardListItem, String>("name"));
        this.cardList_nbCardsCol.setCellValueFactory(new PropertyValueFactory<DeckCardListItem, Integer>("nbCards"));
        this.cardList_nbCollectionCol.setCellValueFactory(new PropertyValueFactory<DeckCardListItem, Integer>("nbCardsInCollection"));
        this.cardList_standardCol.setCellValueFactory(new PropertyValueFactory<DeckCardListItem, Boolean>("standard"));
        this.cardList_tagsCol.setCellValueFactory(new PropertyValueFactory<DeckCardListItem, String>("tags"));
        this.cardList_extCol.setCellValueFactory(new PropertyValueFactory<DeckCardListItem, String>("setCode"));

        this.tagNameCol.setCellValueFactory(new PropertyValueFactory("key"));
        this.nbTagCardCol.setCellValueFactory(new PropertyValueFactory("value"));

        this.cardList_RarityCol.setCellFactory(param -> {return new ColorRectangleCell();});
        this.cardList_ManaCol.setCellFactory(param -> {return CellBuilder.<DeckCardListItem, Integer>buildIntegerCell();});
        this.cardList_ImageCol.setCellFactory(param -> {return new CardTileCell();});
        this.cardList_NameCol.setCellFactory(param -> {return CellBuilder.<DeckCardListItem, String>buildStringCell();});
        this.cardList_nbCardsCol.setCellFactory(param -> {return CellBuilder.<DeckCardListItem, Integer>buildIntegerCell();});
        this.cardList_nbCollectionCol.setCellFactory(param -> {return CellBuilder.<DeckCardListItem, Integer>buildIntegerCell();});
        this.cardList_standardCol.setCellFactory(CheckBoxTableCell.forTableColumn(this.cardList_standardCol));
        this.cardList_tagsCol.setCellFactory(param -> {return CellBuilder.<DeckCardListItem, String>buildStringCell();});
        this.cardList_extCol.setCellFactory(param -> {return new SetIconCell();});

        this.tagNameCol.setCellFactory(param -> CellBuilder.buildStringCell());
        this.nbTagCardCol.setCellFactory(param -> CellBuilder.buildIntegerCell());


        XYChart.Series<String, Number> manaCurveSeries = new XYChart.Series<>();
        manaCurveSeries.setName("mana");
        manaCurveSeries.dataProperty().bindBidirectional(this.viewModel.getCurveManaDataProperty());
        manaCurveChart.getData().add(manaCurveSeries);

        this.cardList.setOnDragOver(event -> dragOverEventHandler(event));
        this.cardList.setOnDragDropped(event -> dragDroppedEventHandler(event));
    }


    protected void dragOverEventHandler(DragEvent event){
        if(event.getGestureSource().getClass() == CardCatalogView.CardCell.class){
            event.acceptTransferModes(TransferMode.COPY);
        }
    }

    protected void dragDroppedEventHandler(DragEvent event){
        if(event.getGestureSource().getClass() == CardCatalogView.CardCell.class) {
            Dragboard db = event.getDragboard();
            int dbfId = Integer.valueOf(db.getString());
            this.viewModel.addCardFromDbfId(dbfId);
            this.cardList.refresh();
        }
    }

    public class CardTileCell extends TableCell<DeckCardListItem, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty) {
                Image img = cardImageManager.getTileCardImage(item, false);
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(30);
                imgView.setPreserveRatio(true);

                DeckCardListItem currentItem = this.getTableRow().getItem();
                if(currentItem != null && currentItem.getNbCards() > currentItem.getNbCardsInCollection()){
                    imgView.setOpacity(0.5);
                }

                setGraphic(imgView);
            }
        }
    }

    public class SetIconCell extends TableCell<DeckCardListItem, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty) {
                try {
                    Image img = imageManager.getCardSetIcon(item);
                    ImageView imgView = new ImageView(img);
                    setGraphic(imgView);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public class ColorRectangleCell extends TableCell<DeckCardListItem, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty){
                Rectangle rect = new Rectangle();
                rect.setHeight(30);
                rect.setWidth(10);
                switch (item){
                    case "COMMON" -> rect.setFill(Color.GHOSTWHITE);
                    case "RARE" -> rect.setFill(Color.BLUE);
                    case "EPIC" -> rect.setFill(Color.PURPLE);
                    case "LEGENDARY" -> rect.setFill(Color.ORANGE);
                }
                setGraphic(rect);
            }
        }
    }

    public void incrSelectedCard(){
        this.viewModel.incrSelectedCard();
        this.cardList.refresh();
    }

    public void decrSelectedCard(){
        this.viewModel.decrSelectedCard();
        this.cardList.refresh();
    }

    public void deleteSelectedCard(){
        this.viewModel.deleteSelectedCard();
        this.cardList.refresh();
    }
}
