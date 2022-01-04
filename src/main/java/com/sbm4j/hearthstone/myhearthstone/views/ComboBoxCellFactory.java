package com.sbm4j.hearthstone.myhearthstone.views;

import com.sbm4j.hearthstone.myhearthstone.model.CodedEntity;
import com.sbm4j.hearthstone.myhearthstone.services.images.ImageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.FileNotFoundException;

public class ComboBoxCellFactory<T extends CodedEntity> implements Callback<ListView<T>, ListCell<T>> {

    public static ImageManager imageManager;


    @Override
    public ListCell<T> call(ListView<T> param) {
        return new ComboBoxCell<T>(true);
    }


    public static <T extends CodedEntity> ComboBoxCell<T> buildCell(){
        return new ComboBoxCell<T>(false);
    }

    public static <T extends CodedEntity> ComboBoxCellFactory<T> buildFactory(ImageManager imageManager){
         if(ComboBoxCellFactory.imageManager == null){
             ComboBoxCellFactory.imageManager = imageManager;
         }
         return new ComboBoxCellFactory<T>();
    }


    public static class ComboBoxCell<T extends CodedEntity> extends ListCell<T>{

        protected boolean showName;

        public ComboBoxCell(boolean showName){
            this.showName = showName;
        }

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty){
                try {
                    Image img = imageManager.getIconImage(item.getClass(), item.getCode());
                    ImageView imgView = new ImageView(img);
                    imgView.setFitWidth(35);
                    imgView.setPreserveRatio(true);

                    if (this.showName) {
                        Label label = buildLabel(item.getName());

                        HBox box = new HBox();
                        box.getChildren().addAll(imgView, label);
                        setGraphic(box);
                    } else {
                        setGraphic(imgView);
                    }
                }
                catch(FileNotFoundException ex){
                    setGraphic(buildLabel(item.getName()));
                }
            }

        }

        protected Label buildLabel(String name){
            Label label = new Label(name);
            label.setAlignment(Pos.BASELINE_CENTER);
            label.setPrefHeight(35);
            label.setPadding(new Insets(10));
            return label;
        }
    }
}
