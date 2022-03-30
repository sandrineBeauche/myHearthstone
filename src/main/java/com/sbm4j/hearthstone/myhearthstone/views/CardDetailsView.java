package com.sbm4j.hearthstone.myhearthstone.views;


import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.CardDetailsViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.controlsfx.control.ListSelectionView;


import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardDetailsView implements FxmlView<CardDetailsViewModel>, Initializable {

    @FXML
    protected ImageView cardImage;

    @FXML
    protected ImageView extensionImage;

    @FXML
    protected Label titleLabel;

    @FXML
    protected TableView<CardDetailsViewModel.CardDetailData> infosTable;

    @FXML
    protected TableColumn<CardDetailsViewModel.CardDetailData, String> dataNameColumn;

    @FXML
    protected TableColumn<CardDetailsViewModel.CardDetailData, String> dataValueColumn;

    @FXML
    protected Label nbCardLabel;

    @FXML
    protected ImageView cardClassImage;

    @FXML
    protected ImageView eliteImage;

    @FXML
    protected ImageView standardBadge;

    @FXML
    protected ListSelectionView<CardTag> tagSelectionView;

    @InjectViewModel
    protected CardDetailsViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.viewModel.initialize(location, resources);
        this.titleLabel.textProperty().bindBidirectional(this.viewModel.getCardNameProperty());
        this.cardImage.imageProperty().bindBidirectional(this.viewModel.getCardImageProperty());
        this.extensionImage.imageProperty().bindBidirectional(this.viewModel.getExtensionImageProperty());
        this.cardClassImage.imageProperty().bindBidirectional(this.viewModel.getCardClassImageProperty());
        this.eliteImage.visibleProperty().bindBidirectional(this.viewModel.getEliteProperty());
        this.standardBadge.visibleProperty().bindBidirectional(this.viewModel.getStandardProperty());
        this.infosTable.itemsProperty().bindBidirectional(this.viewModel.getInfosProperty());

        this.dataNameColumn.setCellValueFactory(new PropertyValueFactory("dataName"));
        this.dataValueColumn.setCellValueFactory(new PropertyValueFactory("dataValue"));

        this.dataNameColumn.setCellFactory(param -> new InfosCell(true));
        this.dataValueColumn.setCellFactory(param -> new InfosCell(false));


    }

    public class InfosCell extends TableCell<CardDetailsViewModel.CardDetailData, String> {

        protected boolean bold;

        public InfosCell(boolean bold){
            this.bold = bold;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty){
                Label label = new Label(item);
                label.setWrapText(true);
                if(this.bold){
                    Font font = label.getFont();
                    label.setFont(Font.font(font.getFamily(), FontWeight.BOLD, font.getSize()));
                }
                else{
                    label.setPrefWidth(400);
                    label.setPrefHeight(50);
                }
                setGraphic(label);
            }
        }
    }
}
