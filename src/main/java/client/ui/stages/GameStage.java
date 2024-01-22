package client.ui.stages;

import client.ServerConnector;
import javafx.stage.Stage;
import messages.toClient.ToClientMessage;

import java.util.function.Consumer;

/**
 * Abstract method declaring stages behaviour.
 */
public abstract class GameStage {

    /**
     * Primary stage.
     */
    protected final Stage primaryStage;

    /**
     * Reference to client-server connector.
     */
    protected final ServerConnector serverConnector;

    /**
     * Handler for changing application stages.
     */
    protected final Consumer<GameStageType> changeStageHandler;

    /**
     * Constructor.
     * @param primaryStage Primary stage.
     * @param serverConnector Reference to server connector.
     * @param changeStageHandler Reference to change state handler.
     */
    public GameStage(Stage primaryStage, ServerConnector serverConnector, Consumer<GameStageType> changeStageHandler) {
        this.primaryStage = primaryStage;
        this.serverConnector = serverConnector;
        this.changeStageHandler = changeStageHandler;
    }

    /**
     * Procedure performed on opening stage.
     */
    public abstract void onOpen();

    /**
     * Procedure for handling received message.
     * @param message Received message.
     */
    public abstract void handleMessage(ToClientMessage message);
}
