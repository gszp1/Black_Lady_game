package client;

import client.ui.stages.GameStageType;
import client.ui.stages.GameStagesController;
import exceptions.ClientSocketConnectionException;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import messages.toServer.requests.LogoutRequest;


/**
 * Main class for client application.
 */
public class GameClient extends Application {

    /**
     * Connector with server.
     */
    private ServerConnector serverConnector = null;

    /**
     * Controller for managing program stages.
     */
    private GameStagesController gameStagesController;

    /**
     * Flag stating that user logged in or not.
     */
    private boolean loggedIn = false;

    /**
     * Entry point for Client application.
     * @param args Program arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The entry point of the application. Initializes the primary stage, establishes a connection
     * with the server, and sets up the necessary components for handling game stages.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     */
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
                LogoutRequest logoutRequest = new LogoutRequest();
                try {
                    serverConnector.sendMessage(logoutRequest);
                } catch (ClientSocketConnectionException e) {
                    System.out.println("Failed to send logout request.");
                }
            }
        });

        gameStagesController.open(GameStageType.LOGIN);
        primaryStage.show();
    }

    /**
     * Initializes ServerConnector.
     * @return Boolean for initialization status.
     */
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

    /**
     * Shows alert box with given content for 3 seconds.
     * @param content Alert message.
     */
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

