package com.sbm4j.hearthstone.myhearthstone.views;


import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardCatalogItem;
import com.sbm4j.hearthstone.myhearthstone.model.DeckCardListItem;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.model.TagStat;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.DeckEditViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
    protected Label nbCardsLabel;

    @FXML
    protected TabPane tabPane;

    @FXML
    protected BarChart<String, Number> manaCurveChart;

    @FXML
    protected TableView<TagStat> statsTagsList;

    @FXML
    protected TableColumn<Pair<String, Integer>, String> tagNameCol;

    @FXML
    protected TableColumn<Pair<String, Integer>, Integer> nbTagCardCol;

    @FXML
    protected ImageView smallStandardBadge;

    @FXML
    protected ImageView smallValidIcon;

    @FXML
    protected ImageView deckClassIcon;


    @InjectViewModel
    protected DeckEditViewModel viewModel;


    @Inject
    protected CardImageManager cardImageManager;

    @Inject
    protected ImageManager imageManager;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.viewModel.initialize(location, resources);

        this.installBindings();
        this.initCardListColumns();
        this.initTagStatsListColumns();

        TableView.TableViewSelectionModel<DeckCardListItem> selectionModel = this.cardList.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.setCellSelectionEnabled(false);

        this.installHandlers();
    }

    protected void installBindings(){
        this.titledPane.textProperty().bindBidirectional(this.viewModel.getTitleProperty());
        this.nameField.textProperty().bindBidirectional(this.viewModel.getNameProperty());
        this.summaryField.textProperty().bindBidirectional(this.viewModel.getSummaryProperty());
        this.heroImage.imageProperty().bindBidirectional(this.viewModel.getHeroImgProperty());
        this.deckClassIcon.imageProperty().bindBidirectional(this.viewModel.getDeckClassIconProperty());
        this.cardList.itemsProperty().bindBidirectional(this.viewModel.getCardsListProperty());
        this.statsTagsList.itemsProperty().bindBidirectional(this.viewModel.getStatsTagsListProperty());
        this.nbCardsLabel.textProperty().bindBidirectional(this.viewModel.getNbCardsTotalProperty());

        XYChart.Series<String, Number> manaCurveSeries = new XYChart.Series<>();
        manaCurveSeries.setName("mana");
        manaCurveSeries.dataProperty().bindBidirectional(this.viewModel.getCurveManaDataProperty());
        manaCurveChart.getData().add(manaCurveSeries);

        this.smallStandardBadge.visibleProperty().bindBidirectional(this.viewModel.getIsStandardProperty());
        this.smallValidIcon.visibleProperty().bindBidirectional(this.viewModel.getIsValidProperty());
    }

    protected void initCardListColumns(){
        this.cardList_RarityCol.setCellValueFactory(new PropertyValueFactory("rarityCode"));
        this.cardList_ManaCol.setCellValueFactory(new PropertyValueFactory("mana"));
        this.cardList_ImageCol.setCellValueFactory(new PropertyValueFactory("id"));
        this.cardList_NameCol.setCellValueFactory(new PropertyValueFactory("name"));
        this.cardList_nbCardsCol.setCellValueFactory(new PropertyValueFactory("nbCards"));
        this.cardList_nbCollectionCol.setCellValueFactory(new PropertyValueFactory("nbCardsInCollection"));
        this.cardList_standardCol.setCellValueFactory(new PropertyValueFactory("standard"));
        this.cardList_tagsCol.setCellValueFactory(new PropertyValueFactory("tags"));
        this.cardList_extCol.setCellValueFactory(new PropertyValueFactory("setCode"));

        this.cardList_RarityCol.setCellFactory(param -> new ColorRectangleCell());
        this.cardList_ManaCol.setCellFactory(param -> CellBuilder.buildIntegerCell());
        this.cardList_ImageCol.setCellFactory(param -> new CardTileCell());
        this.cardList_NameCol.setCellFactory(param -> CellBuilder.buildStringCell());
        this.cardList_nbCardsCol.setCellFactory(param -> CellBuilder.buildIntegerCell());
        this.cardList_nbCollectionCol.setCellFactory(param -> new CollectionCardCell());
        this.cardList_standardCol.setCellFactory(param -> new StandardCardCell());
        this.cardList_tagsCol.setCellFactory(param -> CellBuilder.buildStringCell());
        this.cardList_extCol.setCellFactory(param -> new SetIconCell());
    }

    protected void initTagStatsListColumns(){
        this.tagNameCol.setCellValueFactory(new PropertyValueFactory("tag"));
        this.nbTagCardCol.setCellValueFactory(new PropertyValueFactory("value"));

        this.tagNameCol.setCellFactory(param -> CellBuilder.buildStringCell());
        this.nbTagCardCol.setCellFactory(param -> CellBuilder.buildIntegerCell());
    }

    protected void installHandlers(){
        this.cardList.setOnDragOver(event -> dragOverEventHandler(event));
        this.cardList.setOnDragDropped(event -> dragDroppedEventHandler(event));

        this.tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            refreshCurrentTab(newValue.intValue());
        });

        viewModel.subscribe(DeckEditViewModel.SHOW_DECK, (key, payload) -> {
            String deckName = (String) payload[0];
            this.titledPane.setText(deckName);
            if(this.tabPane.getSelectionModel().getSelectedIndex() == 0){
                this.refreshGeneralTab();
            }
            else {
                this.tabPane.getSelectionModel().select(0);
            }
        });

        viewModel.subscribe(DeckEditViewModel.BACK, (key, payload) -> {
            titledPane.toBack();
        });
    }

    protected void refreshCurrentTab(int index){
        switch(index){
            case 0 -> refreshGeneralTab();
            case 1 -> refreshCardsListTab();
            case 2 -> refreshStatsTab();
        }
    }

    protected void refreshGeneralTab(){
        if(!this.viewModel.getRefreshed()[0]) {
            this.viewModel.refreshGeneralTab();
        }
    }

    protected void refreshCardsListTab(){
        if(this.viewModel.getRefreshed()[1]){
            this.cardList.refresh();
        }
        else{
            this.viewModel.refreshCardListTab();
        }
    }

    protected void refreshStatsTab(){
        if(this.viewModel.getRefreshed()[2]){
            this.statsTagsList.refresh();
            List<XYChart.Data<String, Number>> data = this.viewModel.getCurveManaData().stream().toList();
            this.viewModel.getCurveManaData().clear();
            this.viewModel.getCurveManaData().addAll(data);
        }
        else{
            this.viewModel.refreshStatsTab();
        }
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


    protected class CardListImageTooltip extends Tooltip{

        protected String cardId;

        protected ImageView image;

        public CardListImageTooltip(String cardId){
            super();
            this.cardId = cardId;
            this.setOnShowing(param -> this.onShowingHandler());
        }

        protected void onShowingHandler(){
            if(this.image == null){
                File f = cardImageManager.getBigCardImageAsFile(this.cardId);
                try {
                    this.image = new ImageView(new Image(new FileInputStream(f)));
                    this.image.setFitHeight(500);
                    this.image.setPreserveRatio(true);
                    setGraphic(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    protected class CardListContextMenu extends ContextMenu {

        protected DeckCardListItem card;

        public CardListContextMenu(DeckCardListItem card){
            this.card = card;

            MenuItem menuItem1 = new MenuItem("Détails");
            menuItem1.setOnAction(event -> {
                viewModel.showCardDetails(this.card);
            });

            this.getItems().add(menuItem1);
        }
    }


    protected class StandardCardCell extends TableCell<DeckCardListItem, Boolean> {

        public StandardCardCell(){
            this.setPadding(new Insets(5, 0, 0, 15));
        }

        @Override
        public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty && item){
                ImageView imgView = imageManager.getImageViewFromResource(ImageManagerImpl.class,
                        "standard-badge.png");
                imgView.setFitWidth(60);
                imgView.setPreserveRatio(true);
                setGraphic(imgView);
            }
            else{
                setGraphic(null);
            }
        }
    }

    protected class CollectionCardCell extends TableCell<DeckCardListItem, Integer>{

        public CollectionCardCell(){
            this.setPadding(new Insets(5, 0, 0, 10));
        }

        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty){
                DeckCardListItem currentItem = this.getTableRow().getItem();
                if(currentItem != null) {
                    if (currentItem.getNbCards() <= item) {
                        ImageView img = imageManager.getImageViewFromResource(ImageManager.class, "collection.png");
                        img.setFitWidth(30);
                        img.setPreserveRatio(true);
                        setGraphic(img);
                    } else {
                        Label lbl = new Label(item.toString());
                        setGraphic(lbl);
                    }
                }
            }
            else{
                setGraphic(null);
            }
        }
    }

    protected class CardTileCell extends TableCell<DeckCardListItem, String> {
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
                this.setTooltip(new CardListImageTooltip(item));
                this.setContextMenu(new CardListContextMenu(this.getTableRow().getItem()));
            }
            else{
                this.setGraphic(null);
                this.setTooltip(null);
            }
        }
    }

    protected class SetIconCell extends TableCell<DeckCardListItem, String> {

        public SetIconCell(){
            this.setPadding(new Insets(5, 0, 0, 5));
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty) {
                try {
                    Image img = imageManager.getCardSetIcon(item);
                    ImageView imgView = new ImageView(img);
                    String tooltipString = viewModel.getExtensionTooltips(item);
                    Tooltip tooltip = new Tooltip(tooltipString);
                    this.setTooltip(tooltip);
                    setGraphic(imgView);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                setGraphic(null);
                setTooltip(null);
            }
        }
    }

    protected class ColorRectangleCell extends TableCell<DeckCardListItem, String> {
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
                this.setTooltip(new Tooltip(viewModel.getRarityTooltips(item)));
            }
            else{
                setGraphic(null);
                setTooltip(null);
            }
        }
    }

    public void incrSelectedCard(){
        DeckCardListItem selected = this.cardList.getSelectionModel().getSelectedItem();
        this.viewModel.incrSelectedCard(selected);
        this.cardList.refresh();
    }

    public void decrSelectedCard(){
        DeckCardListItem selected = this.cardList.getSelectionModel().getSelectedItem();
        this.viewModel.decrSelectedCard(selected);
        this.cardList.refresh();
    }

    public void deleteSelectedCard(){
        DeckCardListItem selected = this.cardList.getSelectionModel().getSelectedItem();
        this.viewModel.deleteSelectedCard(selected);
    }

    public void backAction(){
        this.viewModel.backHandler();
    }
}
