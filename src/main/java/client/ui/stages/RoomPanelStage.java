package client.ui.stages;

import client.ServerConnector;
import exceptions.ClientSocketConnectionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import messages.dto.RoomView;
import messages.toClient.ToClientMessage;
import messages.toClient.responses.GameDetailsResponse;
import messages.toServer.requests.GameDetailsRequest;
import utils.model.Room;

import java.util.function.Consumer;

public class RoomPanelStage extends GameStage {

    ObservableList<RoomView> rooms = FXCollections.observableArrayList();

    public RoomPanelStage(
            Stage primaryStage,
            ServerConnector serverConnector,
            Consumer<GameStageType> changeStageHandler
    ) {
        super(primaryStage, serverConnector, changeStageHandler);
    }

    @Override
    public void onOpen() {
        createView();
        sendGameDetailsRequest();
    }

    public void createView() {
        TableView<RoomView> table = new TableView<>(rooms);

        // Room ID Column
        TableColumn<RoomView, Number> roomIdCol = new TableColumn<>("Room ID");
        roomIdCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));

        // Join Button Column
        TableColumn<RoomView, Void> joinCol = new TableColumn<>("Join");
        joinCol.setCellFactory(col -> new TableCell<RoomView, Void>() {
            private final Button joinButton = new Button("Join");

            {
                joinButton.setOnAction(e -> {
                    RoomView room = (RoomView) getTableView().getItems().get(getIndex());
                    // Join room logic
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
                    // Delete room logic
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

        table.getColumns().addAll(roomIdCol, joinCol, participantsCol, creatorCol, deleteCol);
        table.setMaxWidth(700);

        Button createRoomButton = new Button("Create Room");
        createRoomButton.setOnAction(e -> {
            // Create room logic
        });

        HBox hbox = new HBox(15, table, createRoomButton);
        Scene scene = new Scene(hbox, 1000, 600);
        this.primaryStage.setScene(scene);
    }

    private void sendGameDetailsRequest() {
        try {
            serverConnector.sendMessage(new GameDetailsRequest());
        } catch (ClientSocketConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleMessage(ToClientMessage message) {
        GameDetailsResponse gameDetailsResponse = (GameDetailsResponse) message;
        rooms.addAll(gameDetailsResponse.getGameView().getRooms());
    }
}
