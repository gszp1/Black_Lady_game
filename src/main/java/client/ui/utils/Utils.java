package client.ui.utils;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;


/**
 * Utils functions for client app.
 */
public class Utils {

    /**
     * Shows informational alert.
     * @param content Alert contents.
     */
    public static void showInfo(String content) {
        Platform.runLater(() -> {
            showAlertForSeconds("Information", content, 5, Alert.AlertType.INFORMATION);
        });
    }

    /**
     * Shows error alert box.
     * @param content Alert box message.
     */
    public static void showError(String content) {
        Platform.runLater(() -> {
            showAlertForSeconds("Error", content, 5, Alert.AlertType.ERROR);
        });
    }

    /**
     * Shows alert box with given contents.
     * @param title Alert title.
     * @param content Alert message content.
     * @param seconds Time of displaying alert.
     * @param type Alert type.
     */
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
