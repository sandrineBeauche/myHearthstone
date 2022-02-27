package com.sbm4j.hearthstone.myhearthstone.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.controlsfx.control.Notifications;

public class NotificationsUtil {

    public static void showInfoNotification(String title, String header, String content){
        Platform.runLater(() -> {
            try {
                Notifications.create().darkStyle().title(title).text(content).showInformation();
            }
            catch(NullPointerException ex){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.setContentText(content);
                alert.showAndWait();
            }
        });
    }

    public static void showErrorNotification(String title, String header, String content){
        Platform.runLater(() -> {
            try {
                Notifications.create().darkStyle().title(title).text(content).showError();
            }
            catch(NullPointerException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.setContentText(content);
                alert.setResizable(true);
                alert.setWidth(500);
                alert.showAndWait();
            }
        });

    }
}
