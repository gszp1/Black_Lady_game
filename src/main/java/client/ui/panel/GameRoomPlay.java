package client.ui.panel;

import client.ServerConnector;
import client.ui.utils.Utils;
import exceptions.ClientRoomJoinException;
import exceptions.ClientSocketConnectionException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Data;
import javafx.scene.image.ImageView;
import messages.dto.RoomDetails;
import messages.dto.UserView;
import messages.toClient.ToClientMessage;
import messages.toClient.responses.RoomDetailsResponse;
import messages.toServer.requests.InviteUserRequest;
import messages.toServer.requests.LeaveRoomRequest;
import messages.toServer.requests.RoomDetailsRequest;


@Data
public class GameRoomPlay {

    private final int roomId;

    private final ServerConnector serverConnector;

    private ObservableList<UserView> userList = FXCollections.observableArrayList();

    boolean started = false;

    private Stage stage;

    public void open() {
        HBox root = new HBox(20);

        VBox gamePanel = new VBox(10);
        setupGamePanel(gamePanel);

        VBox scoreAndControls = new VBox(10);
        setupScoreAndControls(scoreAndControls);

        root.getChildren().addAll(gamePanel, scoreAndControls);

        Scene primaryScene = new Scene(root, 1200, 1000);

        Stage gamePlayWindow = new Stage();
        gamePlayWindow.setTitle(getTitle(roomId));
        gamePlayWindow.setScene(primaryScene);
        gamePlayWindow.show();
        gamePlayWindow.setOnCloseRequest(e -> {
            sendLeaveGameRequest();
        });
        this.stage = gamePlayWindow;
        sendRoomDetailsRequest();
    }

    private void setupGamePanel(VBox gamePanel) {
        Text cardDisplay = new Text("Player's cards here");
        final ImageView imageView = new ImageView("cards/AceCard_Clubs.png");
        gamePanel.getChildren().addAll(cardDisplay, imageView);
    }

    private void setupScoreAndControls(VBox scoreAndControls) {
        // Game status
        Text gameStatus = new Text("Game Status: Not Started");
        scoreAndControls.getChildren().add(gameStatus);

        // Score table
        TableView<String> scoreTable = new TableView<>();
        setupScoreTable(scoreTable);
        scoreAndControls.getChildren().add(scoreTable);

        // User invitation
        HBox hBox = new HBox(10);
        ComboBox<UserView> userDropdown = new ComboBox<>(userList);
        userDropdown.setPromptText("Invite User");
        userDropdown.setCellFactory(lv -> createUserViewListCell());
        userDropdown.setButtonCell(createUserViewListCell());

        final Button inviteUserButton = new Button("Invite");
        inviteUserButton.setOnAction(e -> {
            final UserView userView = userDropdown.getValue();
            if (userView != null){
                sendInviteUserRequest(userView);
            }
        });

        hBox.getChildren().addAll(userDropdown, inviteUserButton);

        // Leave game button
        Button leaveGameButton = new Button("Leave Game");
        leaveGameButton.setOnAction(e -> {
            sendLeaveGameRequest();
        });

        scoreAndControls.getChildren().addAll(hBox, leaveGameButton);
    }

    private ListCell<UserView> createUserViewListCell() {
        return new ListCell<UserView>() {
            @Override
            protected void updateItem(UserView userView, boolean empty) {
                super.updateItem(userView, empty);
                if (empty || userView == null) {
                    setText(null);
                } else {
                    setText(userView.getEmail());
                }
            }
        };
    }

    private void setupScoreTable(TableView<String> scoreTable) {
        // Set up the score table with dummy data for illustration
        TableColumn<String, String> playerColumn = new TableColumn<>("Player");
        TableColumn<String, String> scoreColumn = new TableColumn<>("Score");
        scoreTable.getColumns().addAll(playerColumn, scoreColumn);

        // Dummy data
        ObservableList<String> data = FXCollections.observableArrayList("Player 1", "Player 2", "Player 3", "Player 4");
        scoreTable.setItems(data);
    }

    private void sendInviteUserRequest(UserView userView) {
        try {
            serverConnector.sendMessage(new InviteUserRequest(userView.getEmail(), roomId));
        } catch (ClientSocketConnectionException e) {
            Utils.showError("Failed to send Invitation Request.");
        }
    }

    private void sendRoomDetailsRequest() {
        try {
            serverConnector.sendMessage(new RoomDetailsRequest(roomId));
        } catch (ClientSocketConnectionException e) {
            Utils.showError("Failed to send RoomDetailsRequest.");
        }
    }

    private void sendLeaveGameRequest() {
        try {
            serverConnector.sendMessage(new LeaveRoomRequest(roomId));
        } catch (ClientSocketConnectionException e) {
            Utils.showError("Failed to send Leave Room Request");
        }
    }

    public void handleRoomDetailsResponse(RoomDetailsResponse message) {
        System.out.println("Handling room details response ");
        System.out.println(message.getRoomDetails().isMyRoom());
        System.out.println(message);
        Platform.runLater(() -> {
            userList.setAll(message.getRoomDetails().getNonInvitedUsers());
        });
    }

    public void close() {
        stage.close();
    }

    private String getTitle(int roomId) {
        return String.format("Hearts Game - Room %s", roomId);
    }
}
