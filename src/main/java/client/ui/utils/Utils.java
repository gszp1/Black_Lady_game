package client.ui.utils;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;

public class Utils {

    public static void showInfo(String content) {
        Platform.runLater(() -> {
            showAlertForSeconds("Information", content, 5, Alert.AlertType.INFORMATION);
        });
    }

    public static void showError(String content) {
        Platform.runLater(() -> {
            showAlertForSeconds("Error", content, 5, Alert.AlertType.ERROR);
        });
    }

    private static void showAlertForSeconds(String title, String content, int seconds, Alert.AlertType type) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);

        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        delay.setOnFinished(e -> {
            if (alert.isShowing()) {
                Platform.runLater(alert::close);
            }
        });
        delay.play();
        alert.showAndWait();
    }
}
