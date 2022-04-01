package com.sbm4j.hearthstone.myhearthstone.views;

import com.sbm4j.hearthstone.myhearthstone.HearthstoneApplication;
import com.sbm4j.hearthstone.myhearthstone.model.CardClass;
import com.sbm4j.hearthstone.myhearthstone.model.CardDetail;
import com.sbm4j.hearthstone.myhearthstone.model.CodedEntity;
import com.sbm4j.hearthstone.myhearthstone.model.Hero;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import com.sbm4j.hearthstone.myhearthstone.viewmodel.CardDetailsViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Pair;
import org.controlsfx.dialog.ProgressDialog;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

public class Dialogs {

    public static Optional<Pair<String, Hero>> newDeckDialog(List<Hero> availableHeros, ImageManager imageManager){
        Dialog<Pair<String, Hero>> dialog = new Dialog<>();
        dialog.setTitle("Création d'un deck");
        dialog.setHeaderText("Création d'un nouveau deck");


        // Set the button types.
        ButtonType createButtonType = new ButtonType("Créer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(getIcon());

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField deckName = new TextField();
        deckName.setPromptText("Username");
        ComboBox<Hero> hero = new ComboBox<Hero>(FXCollections.observableList(availableHeros));


        hero.setCellFactory(param -> new ComboBoxListCell<>(){
            @Override
            public void updateItem(Hero item, boolean empty) {
                super.updateItem(item, empty);
                if(!empty){
                    setGraphic(buildCell(item, imageManager));
                }
            }
        });
        hero.setButtonCell(new ComboBoxListCell<>(){
            @Override
            public void updateItem(Hero item, boolean empty) {
                super.updateItem(item, empty);
                if(!empty){
                    setGraphic(buildCell(item, imageManager));
                }
            }
        });
        hero.setPrefHeight(50);

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(deckName, 1, 0);
        grid.add(new Label("Hero:"), 0, 1);
        grid.add(hero, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node createButton = dialog.getDialogPane().lookupButton(createButtonType);
        createButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        deckName.textProperty().addListener((observable, oldValue, newValue) -> {
            createButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getStylesheets().add(Dialogs.getCss());

        // Request focus on the username field by default.
        Platform.runLater(() -> deckName.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                return new Pair<String, Hero>(deckName.getText(), hero.getSelectionModel().getSelectedItem());
            }
            return null;
        });

        Optional<Pair<String, Hero>> result = dialog.showAndWait();
        return result;
    }

    public static Node buildCell(Hero item, ImageManager imageManager){
        HBox box = new HBox();
        try{
            ImageView heroImg = new ImageView(imageManager.getHeroIcon(item.getCode()));
            heroImg.setFitWidth(35);
            heroImg.setPreserveRatio(true);

            ImageView classImg = new ImageView(imageManager.getIconImage(CardClass.class, item.getClasse().getCode()));
            classImg.setFitWidth(35);
            classImg.setPreserveRatio(true);

            box.getChildren().addAll(heroImg, classImg);
        }
        catch(FileNotFoundException ex){

        }
        return box;
    }

    public static String getCss(){
        return Dialogs.class.getResource("style.css").toExternalForm();
    }

    public static Image getIcon() {
        return new Image(HearthstoneApplication.class.getResourceAsStream("/classic.png"));
    }


    public static void showProgressDialog(ParamCommand command, String headerText){
        try {
            ProgressDialog dialog = new ProgressDialog(command);
            dialog.setTitle(command.getTitle());
            dialog.setHeaderText(headerText);
            dialog.setWidth(600);
            dialog.getDialogPane().getStylesheets().add(Dialogs.getCss());

            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(getIcon());

            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            command.execute();
            Optional<Void> result = dialog.showAndWait();
            if(!result.isPresent()){
                //command.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showCardDetailDialog(CardDetail card) throws FileNotFoundException {
        Dialog dialog = new Dialog();
        dialog.setTitle("Details carte hearthstone");
        dialog.setHeaderText("Carte " + card.getName());

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().getStylesheets().add(Dialogs.getCss());

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(getIcon());

        ViewTuple<CardDetailsView, CardDetailsViewModel> cardViewTuple = FluentViewLoader.fxmlView(CardDetailsView.class).load();
        dialog.getDialogPane().setContent(cardViewTuple.getView());

        cardViewTuple.getViewModel().showCard(card);
        dialog.showAndWait();
    }
}
