package com.sbm4j.hearthstone.myhearthstone.views;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.*;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.CardCatalogViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.IndexedCheckModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class CardCatalogView implements FxmlView<CardCatalogViewModel>, Initializable {

    @FXML
    protected GridView<CardCatalogItem> cardGridView;

    @FXML
    protected ComboBox<CardClass> cmbCardClass;

    @FXML
    protected ComboBox<CardSet> cmbCardSet;

    @FXML
    protected ComboBox<Rarity> cmbRarity;

    @FXML
    protected ComboBox<ManaOption> cmbMana;

    @FXML
    protected ToggleButton tglbCollection;

    @FXML
    protected CheckComboBox<CardTag> cmbTags;

    @FXML
    protected Label lblNbCards;


    @InjectViewModel
    protected CardCatalogViewModel viewModel;

    @Inject
    protected CardImageManager cardImageManager;

    @Inject
    protected ImageManager imageManager;

    @Inject
    protected ConfigManager configManager;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.viewModel.initialize(location, resources);

        this.cardGridView.itemsProperty().bindBidirectional(viewModel.getCollectionProperty());
        this.cmbCardClass.itemsProperty().bindBidirectional(viewModel.getAvailableClassesProperty());
        this.cmbCardClass.valueProperty().bindBidirectional(viewModel.getSelectedClassProperty());
        this.cmbCardSet.itemsProperty().bindBidirectional(viewModel.getAvailableSetsProperty());
        this.cmbCardSet.valueProperty().bindBidirectional(viewModel.getSelectedSetProperty());
        this.cmbRarity.itemsProperty().bindBidirectional(viewModel.getAvailableRarityProperty());
        this.cmbRarity.valueProperty().bindBidirectional(viewModel.getSelectedRarityProperty());
        this.cmbMana.itemsProperty().bindBidirectional(viewModel.getAvailableManaProperty());
        this.cmbMana.valueProperty().bindBidirectional(viewModel.getSelectedManaProperty());
        this.tglbCollection.selectedProperty().bindBidirectional(viewModel.getSelectedInCollectionProperty());
        IndexedCheckModel<CardTag> model = this.cmbTags.checkModelProperty().get();
        this.viewModel.setCheckModelTags(model);
        this.cmbTags.checkModelProperty().bindBidirectional((viewModel.getCheckModelTagsProperty()));
        this.cmbTags.getItems().addAll(this.viewModel.getAvailableTags());
        this.viewModel.getAvailableTags().addListener((ListChangeListener<? super CardTag>) c -> {
            cmbTags.getItems().clear();
            cmbTags.getItems().addAll(c.getList());
        });
        this.viewModel.getCheckModelTagsProperty().addListener((observable, oldValue, newValue) -> {
            this.viewModel.refreshCatalog();
        });
        this.lblNbCards.textProperty().bindBidirectional(viewModel.getNbCardsTxtProperty());


        this.cmbCardClass.setCellFactory(ComboBoxCellFactory.<CardClass>buildFactory(this.imageManager));
        this.cmbCardClass.setButtonCell(ComboBoxCellFactory.<CardClass>buildCell());
        this.cmbCardSet.setCellFactory(ComboBoxCellFactory.<CardSet>buildFactory(this.imageManager));
        this.cmbCardSet.setButtonCell(ComboBoxCellFactory.<CardSet>buildCell());
        this.cmbRarity.setCellFactory(ComboBoxCellFactory.<Rarity>buildFactory(this.imageManager));
        this.cmbRarity.setButtonCell(ComboBoxCellFactory.<Rarity>buildCell());
        this.cmbMana.setCellFactory(ComboBoxCellFactory.<ManaOption>buildFactory(this.imageManager));
        this.cmbMana.setButtonCell(ComboBoxCellFactory.<ManaOption>buildCell());

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("collection.png");
        ImageView img = new ImageView(new Image(in));
        img.setFitWidth(35);
        img.setPreserveRatio(true);
        this.tglbCollection.setGraphic(img);

        this.cardGridView.setCellFactory(new Callback<GridView<CardCatalogItem>, GridCell<CardCatalogItem>>() {
            @Override
            public GridCell<CardCatalogItem> call(GridView<CardCatalogItem> param) {
                return new CardCell();
            }
        });
    }


    protected class CardToolTip extends Tooltip {

        protected CardCatalogItem item;

        protected GridPane grid;

        public CardToolTip(CardCatalogItem item){
            this.item = item;
            this.setOnShowing(param -> onShowingHandler());
        }

        protected void onShowingHandler(){
            if(this.grid == null) {
                try {
                    File fCard = cardImageManager.getBigCardImageAsFile(this.item.id());
                    ImageView imageCard = new ImageView(new Image(new FileInputStream(fCard)));
                    imageCard.setFitHeight(500);
                    imageCard.setPreserveRatio(true);

                    CardDetail details = viewModel.getDetails(item);

                    Image setImg = imageManager.getCardSetLogo(details.getCardSet().getCode());
                    ImageView setImgView = new ImageView(setImg);
                    setImgView.setFitWidth(250);
                    setImgView.setPreserveRatio(true);


                    this.grid = new GridPane();
                    this.grid.add(imageCard, 0, 0, 1, 2);
                    this.grid.add(setImgView, 1, 0);
                    setGraphic(this.grid);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void onCriteriaChange(){
        this.cardImageManager.clearThumbsCache();
        this.cardImageManager.clearBigsCache();
        this.viewModel.refreshCatalog();
    }

    public void resetTagsFilter(){
        this.viewModel.resetTags();
    }

    public void importCatalogCommand(){
        ParamCommand command = this.viewModel.getImportCatalogCommand();
        Dialogs.showProgressDialog(command, "Importer le catalogue de cartes Hearthstone");
        this.viewModel.refreshCatalog();
    }

    public void importCollectionCommand(){
        ParamCommand command = this.viewModel.getImportCollectionCommand();
        Dialogs.showProgressDialog(command, "Importer la collection de l'utilisateur");
        this.viewModel.refreshCatalog();
    }

    public class CardCell extends GridCell<CardCatalogItem>{

        public CardCell(){
            super();
            this.setOnDragDetected(dragEventHandler);
            this.setOnMouseClicked(mouseClickedEventHandler);
        }

        @Override
        protected void updateItem(CardCatalogItem item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty) {
                Image img = cardImageManager.getThumbnailCardImage(item.id(), true);
                ImageView imgView = new ImageView(img);
                if(item.nbCards() == 0){
                    imgView.setOpacity(0.5);
                }
                setGraphic(imgView);
                this.setTooltip(new CardToolTip(item));
            }
        }
    }

    protected EventHandler<MouseEvent> dragEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            CardCell source = (CardCell) event.getSource();
            System.out.println("drag handled from " + source.getItem().name());
            Dragboard db = source.startDragAndDrop(TransferMode.ANY);
            Image img = cardImageManager.getTileCardImage(source.getItem().id(), false);
            db.setDragView(img);

            ClipboardContent content = new ClipboardContent();
            content.putString(Integer.toString(source.getItem().dbfId()));
            db.setContent(content);

            event.consume();
        }
    };

    protected EventHandler<Event> contextMenuEventHandler = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            CardCell source = (CardCell) event.getSource();
            System.out.println("context menu handled from " + source.getItem().name());
        }
    };

    protected EventHandler<MouseEvent> mouseClickedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(event.getClickCount() == 2) {
                CardCell source = (CardCell) event.getSource();
                System.out.println("mouse double-clicked handled from " + source.getItem().name());
            }
            else{
                if(event.getButton() == MouseButton.SECONDARY){
                    CardCell source = (CardCell) event.getSource();
                    System.out.println("context menu handled from " + source.getItem().name());
                }
            }
        }
    };


}
