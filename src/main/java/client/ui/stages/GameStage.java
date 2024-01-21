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


    protected final Consumer<GameStageType> changeStageHandler;

    public GameStage(Stage primaryStage, ServerConnector serverConnector, Consumer<GameStageType> changeStageHandler) {
        this.primaryStage = primaryStage;
        this.serverConnector = serverConnector;
        this.changeStageHandler = changeStageHandler;
    }

    public abstract void onOpen();

    public abstract void handleMessage(ToClientMessage message);
}
