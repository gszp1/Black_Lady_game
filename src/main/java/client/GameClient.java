package client;

import client.ui.stages.GameStageType;
import client.ui.stages.GameStagesController;
import client.ui.stages.LoginStage;
import exceptions.ClientSocketConnectionException;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import messages.toServer.requests.LogoutRequest;

public class GameClient extends Application {

    private ServerConnector serverConnector = null;

    private boolean registrationWindowOpened = false;

    private Label loginNotificationLabel;

    private Label registerNotificationLabel;

    private LoginStage loginStage;

    private GameStagesController gameStagesController;

    private boolean loggedIn = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        if (!establishConnectionWithServer()) {
            return;
        }
        serverConnector.start();

        gameStagesController = new GameStagesController(primaryStage, serverConnector);
        serverConnector.setMessageHandler((message) -> gameStagesController.handleMessage(message));

        primaryStage.setOnCloseRequest(event -> {
            if (loggedIn) {
                LogoutRequest logoutRequest = new LogoutRequest("", "");
                try {
                    serverConnector.sendMessage(logoutRequest);
                } catch (ClientSocketConnectionException e) {
                    System.out.println("Failed to send logout request.");
                }
            }
        });

        gameStagesController.open(GameStageType.LOGIN);
        // Show the Stage
        primaryStage.show();
    }

    private boolean establishConnectionWithServer() {
        try {
            if (serverConnector == null) {
                serverConnector = new ServerConnector(this);
            }
        } catch (ClientSocketConnectionException e) {
            showAlert(e.getErrorCause());
            return false;
        }
        return true;
    }

    public static void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(content);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            Platform.runLater(alert::hide);
        });
        alert.show();
        delay.play();
    }
}

