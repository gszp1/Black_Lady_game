package client.ui.stages;

import client.ServerConnector;
import javafx.stage.Stage;
import messages.toClient.ToClientMessage;

/**
 * Controller for handling program stages.
 */
public class GameStagesController {

    /**
     * Login stage.
     */
    private LoginStage loginStage;

    /**
     * Register stage.
     */
    private RegisterStage registerStage;

    /**
     * Room stage.
     */
    private RoomPanelStage roomPanelStage;

    /**
     * Current stage.
     */
    private GameStageType currentGameStage;

    /**
     * Constructor, sets up stages.
     * @param primaryStage Reference to primary stage.
     * @param serverConnector Reference to server connector.
     */
    public GameStagesController(Stage primaryStage, ServerConnector serverConnector) {
        this.loginStage = new LoginStage(primaryStage, serverConnector, this::fromLoginStageChange);
        this.registerStage = new RegisterStage(primaryStage, serverConnector, this::fromRegisterStageChange);
        this.roomPanelStage = new RoomPanelStage(primaryStage, serverConnector, this::fromRoomPanelStageChange);
    }

    /**
     * Opens given game stage.
     * @param gameStageType Game stage.
     */
    public void open(GameStageType gameStageType) {
        this.currentGameStage = gameStageType;
        if (gameStageType == GameStageType.LOGIN) {
            loginStage.onOpen();
        }
    }

    /**
     * Changes current game stage type to given one.
     * @param gameStageType Game stage.
     */
    private void fromLoginStageChange(GameStageType gameStageType) {
        this.currentGameStage = gameStageType;
        if (gameStageType == GameStageType.REGISTER) {
            registerStage.onOpen();
        } else if (gameStageType == GameStageType.ROOM_PANEL) {
            roomPanelStage.onOpen();
        } else {
            throw new RuntimeException(
                    String.format("Invalid state transition in login stage %s", gameStageType.toString())
            );
        }
    }

    /**
     * Changes current stage from register to given one.
     * @param gameStageType Next stage.
     */
    private void fromRegisterStageChange(GameStageType gameStageType) {
        this.currentGameStage = gameStageType;
        if (gameStageType == GameStageType.LOGIN) {
            loginStage.onOpen();
        } else {
            throw new RuntimeException(
                    String.format("Invalid state transition in register stage %s", gameStageType.toString())
            );
        }
    }

    /**
     * Changes current stage from room stage to given one.
     * @param gameStageType Next stage.
     */
    private void fromRoomPanelStageChange(GameStageType gameStageType) {
        this.currentGameStage = gameStageType;
        throw new RuntimeException();
    }

    /**
     * Calls message handler for active stage.
     * @param message Received message.
     */
    public void handleMessage(ToClientMessage message) {
        switch(currentGameStage){
            case LOGIN:
                loginStage.handleMessage(message);
                break;
            case REGISTER:
                registerStage.handleMessage(message);
                break;
            case ROOM_PANEL:
                roomPanelStage.handleMessage(message);
                break;
        }
    }
}
