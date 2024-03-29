package client.ui.stages;

import client.ServerConnector;
import client.ui.panel.GameRoomPlay;
import client.ui.panel.RoomInviteDialog;
import client.ui.utils.Utils;
import exceptions.ClientSocketConnectionException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import messages.dto.GameView;
import messages.dto.RoomView;
import messages.toClient.ToClientMessage;
import messages.toClient.responses.*;
import messages.toServer.requests.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Stage for main room.
 */
public class RoomPanelStage extends GameStage {

    /**
     * Map to associate rooms' ID's with games.
     */
    private final Map<Integer, GameRoomPlay> gameRoomPlays = new HashMap<>();

    /**
     * Title of stage.
     */
    public static String ROOM_PANEL_STAGE_TITLE = "Choose game";

    /**
     * List for storing room views to display in UI.
     */
    ObservableList<RoomView> rooms = FXCollections.observableArrayList();

    /**
     * Constructor.
     * @param primaryStage Primary stage.
     * @param serverConnector Reference to server connector.
     * @param changeStageHandler Reference change stage handler.
     */
    public RoomPanelStage(
            Stage primaryStage,
            ServerConnector serverConnector,
            Consumer<GameStageType> changeStageHandler
    ) {
        super(primaryStage, serverConnector, changeStageHandler);
    }

    /**
     * Procedure to do upon stage opening.
     */
    @Override
    public void onOpen() {
        primaryStage.setTitle(ROOM_PANEL_STAGE_TITLE);
        primaryStage.setOnCloseRequest(e -> logout());
        createView();
        sendGameDetailsRequest();
    }

    /**
     * Sends LogoutRequest to server.
     */
    private void logout() {
        try {
            serverConnector.sendMessage(new LogoutRequest());
        } catch (ClientSocketConnectionException e) {
            Utils.showError("Failed to logout!");
        }
    }

    /**
     * Creates GUI for main room panel.
     */
    public void createView() {
        TableView<RoomView> table = new TableView<>(rooms);

        TableColumn<RoomView, Number> roomIdCol = new TableColumn<>("Room ID");
        roomIdCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        TableColumn<RoomView, String> isUserInRoomCol = new TableColumn<>("Is Member");
        isUserInRoomCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isUserInRoom() ? "YES" : "NO")
        );

        TableColumn<RoomView, Void> joinCol = new TableColumn<>("Join");
        joinCol.setCellFactory(col -> new TableCell<RoomView, Void>() {
            private final Button joinButton = new Button("Join");

            {
                joinButton.setOnAction(e -> {
                    RoomView room = (RoomView) getTableView().getItems().get(getIndex());
                    sendJoinRoomRequest(room.getRoomId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    RoomView room = (RoomView) getTableView().getItems().get(getIndex());
                    if (room.getNumberOfParticipants() < 4 && !room.isUserInRoom()) {
                        setGraphic(joinButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        // Number of Participants Column
        TableColumn<RoomView, Number> participantsCol = new TableColumn<>("Participants");
        participantsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfParticipants"));

        // Room Creator Email Column
        TableColumn<RoomView, String> creatorCol = new TableColumn<>("Creator Email");
        creatorCol.setCellValueFactory(new PropertyValueFactory<>("roomCreatorEmail"));

        // Delete Button Column
        TableColumn<RoomView, Void> deleteCol = new TableColumn<>("Delete");
        deleteCol.setCellFactory(col -> new TableCell<RoomView, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(e -> {
                    RoomView room = getTableView().getItems().get(getIndex());
                    sendDeleteGameRequest(room.getRoomId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    RoomView room = (RoomView) getTableView().getItems().get(getIndex());
                    if (room.isUserCreator()) {
                        setGraphic(deleteButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        table.getColumns().addAll(roomIdCol, isUserInRoomCol, joinCol, participantsCol, creatorCol, deleteCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(800);

        Button createRoomButton = new Button("Create Room");
        createRoomButton.setOnAction(e -> {
            sendCreateGameRequest();
        });

        HBox hbox = new HBox(15, table, createRoomButton);
        HBox.setHgrow(table, Priority.ALWAYS);
        HBox.setHgrow(createRoomButton, Priority.NEVER);
        Scene scene = new Scene(hbox, 1000, 600);
        this.primaryStage.setScene(scene);
    }

    /**
     * Sends join room request.
     * @param roomId Room's ID.
     */
    private void sendJoinRoomRequest(int roomId) {
        try {
            serverConnector.sendMessage(new JoinRoomRequest(roomId));
        } catch (ClientSocketConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends delete room request.
     * @param roomId Room's ID.
     */
    private void sendDeleteGameRequest(int roomId) {
        try {
            serverConnector.sendMessage(new DeleteRoomRequest(roomId));
        } catch (ClientSocketConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends create room request.
     */
    private void sendCreateGameRequest() {
        try {
            serverConnector.sendMessage(new CreateRoomRequest());
        } catch (ClientSocketConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends request for game details.
     */
    private void sendGameDetailsRequest() {
        try {
            serverConnector.sendMessage(new GameDetailsRequest());
        } catch (ClientSocketConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Message handler.
     * @param message Received message.
     */
    @Override
    public void handleMessage(ToClientMessage message) {
        if (message instanceof GameDetailsResponse) {
            GameDetailsResponse gameDetailsResponse = (GameDetailsResponse) message;
            rooms.setAll(gameDetailsResponse.getGameView().getRooms());
            getRoomsToClose(gameDetailsResponse.getGameView()).forEach(this::closeRoom);
            getRoomsToOpen(gameDetailsResponse.getGameView()).forEach(this::openRoom);
        } else if (message instanceof DeleteRoomResponse) {
            DeleteRoomResponse deleteRoomResponse = (DeleteRoomResponse) message;
            System.out.println(deleteRoomResponse.getDetails());
        } else if (message instanceof JoinRoomResponse) {
            JoinRoomResponse joinRoomResponse = (JoinRoomResponse) message;
            if (joinRoomResponse.isSuccess()) Utils.showInfo(joinRoomResponse.getDetails());
            else Utils.showError(joinRoomResponse.getDetails());
        } else if (message instanceof RoomDetailsResponse) {
            RoomDetailsResponse roomDetailsResponse = (RoomDetailsResponse) message;
            int roomId = roomDetailsResponse.getRoomDetails().getRoomId();
            gameRoomPlays.get(roomId).handleRoomDetailsResponse(roomDetailsResponse);
        } else if (message instanceof RoomInviteToReceiverRequest) {
            RoomInviteToReceiverRequest roomInviteToReceiverRequest = (RoomInviteToReceiverRequest) message;
            Platform.runLater(() -> {
                new RoomInviteDialog(
                        roomInviteToReceiverRequest.getRoomId(),
                        roomInviteToReceiverRequest.getReceiverEmail(),
                        roomInviteToReceiverRequest.getSenderEmail(),
                        serverConnector
                ).open();
            });
        } else if (message instanceof InviteUserResponse) {
            final InviteUserResponse inviteUserResponse = (InviteUserResponse) message;
            if (inviteUserResponse.isSuccess()) {
                Utils.showInfo(inviteUserResponse.getDescription());
            } else {
                Utils.showError(inviteUserResponse.getDescription());
            }
        }
    }

    /**
     * Get UI rooms that need closing.
     * @param gameView Rooms.
     * @return Set of rooms ids to close.
     */
    private Set<Integer> getRoomsToClose(GameView gameView) {
        Set<Integer> actualParticipatedRooms = getActualParticipatedRooms(gameView);
        Set<Integer> roomsToLeave = new HashSet<>(getUiOpenRooms());
        roomsToLeave.removeAll(actualParticipatedRooms);
        return roomsToLeave;
    }

    /**
     * Gets rooms that need to be opened in UI.
     * @param gameView Rooms.
     * @return IDs of rooms that need opening.
     */
    private Set<Integer> getRoomsToOpen(GameView gameView) {
        Set<Integer> openUiRooms = getUiOpenRooms();
        Set<Integer> roomsToOpen = new HashSet<>(getActualParticipatedRooms(gameView));
        roomsToOpen.removeAll(openUiRooms);
        return roomsToOpen;
    }

    /**
     * Gets IDs of rooms opened in UI.
     * @return Set of rooms IDs.
     */
    private Set<Integer> getUiOpenRooms() {
        return gameRoomPlays.values().stream()
                .map(GameRoomPlay::getRoomId)
                .collect(Collectors.toSet());
    }

    /**
     * Gets IDs of rooms which user joined.
     * @param gameView Rooms.
     * @return Set of rooms IDs.
     */
    private Set<Integer> getActualParticipatedRooms(GameView gameView) {
        return gameView.getRooms().stream()
                .filter(RoomView::isUserInRoom)
                .map(RoomView::getRoomId)
                .collect(Collectors.toSet());
    }

    /**
     * Opens new game room window.
     * @param roomId Room ID.
     */
    private void openRoom(int roomId) {
        GameRoomPlay gameRoomPlay = new GameRoomPlay(roomId, serverConnector);
        Platform.runLater(gameRoomPlay::open);
        gameRoomPlays.put(roomId, gameRoomPlay);
    }

    /**
     * Closes room in UI.
     * @param roomId Room ID.
     */
    private void closeRoom(int roomId) {
        GameRoomPlay roomToClose = gameRoomPlays.get(roomId);
        Platform.runLater(roomToClose::close);
        gameRoomPlays.remove(roomId);
    }
}
