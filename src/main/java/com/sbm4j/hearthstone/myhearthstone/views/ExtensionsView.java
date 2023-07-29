package com.sbm4j.hearthstone.myhearthstone.views;

import com.google.inject.Inject;
import com.sbm4j.hearthstone.myhearthstone.model.CardSetDetail;
import com.sbm4j.hearthstone.myhearthstone.model.DeckCardListItem;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManagerImpl;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.ExtensionsViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExtensionsView implements FxmlView<ExtensionsViewModel>, Initializable {

    @FXML
    protected TitledPane titledPane;

    @FXML
    protected TableView<CardSetDetail> cardSetList;

    @FXML
    protected TableColumn<CardSetDetail, String> logoCol;

    @FXML
    protected TableColumn<CardSetDetail, String> iconeCol;

    @FXML
    protected TableColumn<CardSetDetail, String> nameCol;

    @FXML
    protected TableColumn<CardSetDetail, String> commonCol;

    @FXML
    protected TableColumn<CardSetDetail, String> rareCol;

    @FXML
    protected TableColumn<CardSetDetail, String> epicCol;

    @FXML
    protected TableColumn<CardSetDetail, String> legendaryCol;

    @FXML
    protected TableColumn<CardSetDetail, String> totalCol;

    @FXML
    protected TableColumn<CardSetDetail, Boolean> standardCol;

    @InjectViewModel
    protected ExtensionsViewModel viewModel;

    @Inject
    protected ImageManager imageManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.viewModel.initialize(location, resources);

        this.cardSetList.itemsProperty().bindBidirectional(this.viewModel.getItemsProperty());

        this.logoCol.setCellValueFactory(new PropertyValueFactory<CardSetDetail, String>("code"));
        this.iconeCol.setCellValueFactory(new PropertyValueFactory<CardSetDetail, String>("code"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<CardSetDetail, String>("name"));
        this.standardCol.setCellValueFactory(new PropertyValueFactory<CardSetDetail, Boolean>("standard"));
        this.commonCol.setCellValueFactory(new PropertyValueFactory<CardSetDetail, String>("common"));
        this.rareCol.setCellValueFactory(new PropertyValueFactory<CardSetDetail, String>("rare"));
        this.epicCol.setCellValueFactory(new PropertyValueFactory<CardSetDetail, String>("epic"));
        this.legendaryCol.setCellValueFactory(new PropertyValueFactory<CardSetDetail, String>("legendary"));
        this.totalCol.setCellValueFactory(new PropertyValueFactory<CardSetDetail, String>("total"));

        this.logoCol.setCellFactory(param -> {return new CardSetLogoCell();});
        this.iconeCol.setCellFactory(param -> {return new CardSetIconCell();});
        this.nameCol.setCellFactory(param -> {return CellBuilder.buildStringCell();});
        this.standardCol.setCellFactory(param -> {return new StandardCardCell();});
        this.commonCol.setCellFactory(param -> {return CellBuilder.buildStringCell();});
        this.rareCol.setCellFactory(param -> {return CellBuilder.buildStringCell();});
        this.epicCol.setCellFactory(param -> {return CellBuilder.buildStringCell();});
        this.legendaryCol.setCellFactory(param -> {return CellBuilder.buildStringCell();});
        this.totalCol.setCellFactory(param -> {return CellBuilder.buildStringCell();});

        this.titledPane.setVisible(false);
    }


    public class CardSetLogoCell extends TableCell<CardSetDetail, String> {

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty){
                try {
                    Image img = imageManager.getCardSetLogo(item);
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(180);
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

    public class CardSetIconCell extends TableCell<CardSetDetail, String> {

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty){
                try {
                    Image img = imageManager.getCardSetIcon(item);
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(40);
                    imgView.setPreserveRatio(true);
                    setGraphic(imgView);
                    setAlignment(Pos.CENTER);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            else{
                setGraphic(null);
            }
        }
    }

    protected class StandardCardCell extends TableCell<CardSetDetail, Boolean> {

        public StandardCardCell(){
            this.setPadding(new Insets(5, 0, 0, 15));
        }

        @Override
        public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty && item){
                ImageView imgView = imageManager.getImageViewFromResource(ImageManagerImpl.class,
                        "standard-badge.png");
                imgView.setFitWidth(80);
                imgView.setPreserveRatio(true);
                setGraphic(imgView);
                setAlignment(Pos.CENTER);
            }
            else{
                setGraphic(null);
            }
        }
    }

    public ExtensionsViewModel getViewModel() {
        return viewModel;
    }
}
