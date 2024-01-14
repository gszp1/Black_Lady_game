package client.ui.stages;

import client.ServerConnector;
import javafx.stage.Stage;
import messages.toClient.ToClientMessage;

import java.util.function.Consumer;

public abstract class GameStage {

    protected final Stage primaryStage;

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
