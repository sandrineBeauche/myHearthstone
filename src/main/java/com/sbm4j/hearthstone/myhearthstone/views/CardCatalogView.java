package com.sbm4j.hearthstone.myhearthstone.views;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardSet;
import com.sbm4j.hearthstone.myhearthstone.model.Rarity;
import com.sbm4j.hearthstone.myhearthstone.services.config.ConfigManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.CardImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.CardCatalogViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

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
    protected Button cmdImportCatalog;

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


    public void onCriteriaChange(){

    }

    public void importCatalogCommand(){
        this.viewModel.getImportCatalogCommand().execute();
    }

    public class CardCell extends GridCell<CardCatalogItem>{

        @Override
        protected void updateItem(CardCatalogItem item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty) {
                Image image = null;
                try {
                    image = cardImageManager.getThumbnailCardImage(item.getId(), true);
                } catch (FileNotFoundException e) {
                    image = configManager.getAlternateCardImage();
                }
                setGraphic(new ImageView(image));
            }
        }
    }

}
