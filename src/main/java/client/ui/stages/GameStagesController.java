package client.ui.stages;

import client.ServerConnector;
import javafx.stage.Stage;
import messages.toClient.ToClientMessage;

public class GameStagesController {

    private LoginStage loginStage;

    private RegisterStage registerStage;

    private RoomPanelStage roomPanelStage;

    private GameStageType currentGameStage;

    public GameStagesController(Stage primaryStage, ServerConnector serverConnector) {
        this.loginStage = new LoginStage(primaryStage, serverConnector, this::fromLoginStageChange);
        this.registerStage = new RegisterStage(primaryStage, serverConnector, this::fromRegisterStageChange);
        this.roomPanelStage = new RoomPanelStage(primaryStage, serverConnector, this::fromRoomPanelStageChange);
    }

    public void open(GameStageType gameStageType) {
        this.currentGameStage = gameStageType;
        if (gameStageType == GameStageType.LOGIN) {
            loginStage.onOpen();
        }
    }

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

    private void fromRoomPanelStageChange(GameStageType gameStageType) {
        this.currentGameStage = gameStageType;
        throw new RuntimeException(

        );
    }

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
