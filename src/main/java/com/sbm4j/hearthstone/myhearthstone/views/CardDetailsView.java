package com.sbm4j.hearthstone.myhearthstone.views;


import com.sbm4j.hearthstone.myhearthstone.model.CardTag;
import com.sbm4j.hearthstone.myhearthstone.model.DeckListItem;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.CardDetailsViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.controlsfx.control.ListSelectionView;


import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Optional;
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
    protected ListView<CardTag> availableTagsList;

    @FXML
    protected ListView<CardTag> associatedTagsList;


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

        this.availableTagsList.itemsProperty().bindBidirectional(this.viewModel.getAvailableTagsProperty());
        this.associatedTagsList.itemsProperty().bindBidirectional(this.viewModel.getAssociatedTagsProperty());
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

    public void createNewUserTagCallback(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouveau tag utilisateur");
        dialog.setHeaderText("Créer un tag utilisateur");
        dialog.setContentText("Entrez le nom du nouveau tag: ");
        dialog.getDialogPane().getStylesheets().add(Dialogs.getCss());

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String tagName = result.get();
            ParamCommand command = this.viewModel.getCreateNewUserTagCommand();
            command.putParameter("name", tagName);
            command.execute();
        }
    }

    public void deleteUserTagCallback(){
        CardTag selectedTag = this.availableTagsList.getSelectionModel().getSelectedItem();
        if(selectedTag != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Supprimer un tag utilisateur");
            alert.setHeaderText("");
            alert.setContentText("Voulez-vous supprimer le tag " + selectedTag.getName() + "?");
            alert.getDialogPane().getStylesheets().add(Dialogs.getCss());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                ParamCommand command = this.viewModel.getDeleteUserTagCommand();
                command.putParameter("tag", selectedTag);
                command.execute();
            }
        }
    }

    public void addTagCallback(){
        CardTag selectedTag = this.availableTagsList.getSelectionModel().getSelectedItem();
        if(selectedTag != null){
            ParamCommand command = this.viewModel.getAddUserTagCommand();
            command.putParameter("tag", selectedTag);
            command.execute();
        }
    }

    public void removeTagCallback(){
        CardTag selectedTag = this.associatedTagsList.getSelectionModel().getSelectedItem();
        if(selectedTag != null){
            ParamCommand command = this.viewModel.getRemoveUserTagCommand();
            command.putParameter("tag", selectedTag);
            command.execute();
        }
    }

    public void removeAllTagsCallback(){
        ParamCommand command = this.viewModel.getRemoveAllUserTagCommand();
        command.execute();
    }
}
