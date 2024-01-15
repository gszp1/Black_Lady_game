package client.ui.panel;

import cards.Card;
import client.ServerConnector;
import client.ui.utils.Utils;
import exceptions.ClientRoomJoinException;
import exceptions.ClientSocketConnectionException;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Builder;
import lombok.Data;
import javafx.scene.image.ImageView;
import messages.dto.ChatEntryView;
import messages.dto.RoomDetails;
import messages.dto.RoomView;
import messages.dto.UserView;
import messages.toClient.ToClientMessage;
import messages.toClient.responses.RoomDetailsResponse;
import messages.toServer.requests.InviteUserRequest;
import messages.toServer.requests.LeaveRoomRequest;
import messages.toServer.requests.RoomDetailsRequest;
import messages.toServer.requests.WriteChatRequest;
import utils.model.ChatEntry;

import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;


@Data
public class GameRoomPlay {

    private final int roomId;

    private final ServerConnector serverConnector;

    private ObservableList<UserView> userList = FXCollections.observableArrayList();

    boolean started = false;

    private BooleanProperty isReadyToStart = new SimpleBooleanProperty(false);

    private BooleanProperty isMyRoom = new SimpleBooleanProperty(false);

    ObservableList<Score> scores = FXCollections.observableArrayList();

    private ObservableList<ChatEntryView> chat = FXCollections.observableArrayList();

    private ObservableList<Card> myCards = FXCollections.observableArrayList();

    private Stage stage;

    public void open() {
        HBox root = new HBox(20);

        VBox gamePanel = new VBox(10);
        HBox.setHgrow(gamePanel, Priority.ALWAYS);
        setupGamePanel(gamePanel);

        VBox scoreAndControls = new VBox(10);
        HBox.setHgrow(scoreAndControls, Priority.NEVER);
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

        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);


        BorderPane borderPane = new BorderPane();
        Text cardDisplay = new Text("Player's cards here");
        final ImageView img1 = new ImageView("cards/AceCard_Diamonds.png");
        img1.setFitWidth(100);
        img1.setFitHeight(145);
        final ImageView img2 = new ImageView("cards/AceCard_Hearts.png");
        img2.setFitWidth(100);
        img2.setFitHeight(145);
        final ImageView img3 = new ImageView("cards/AceCard_Spades.png");
        img3.setFitWidth(100);
        img3.setFitHeight(145);
        final ImageView imageView = new ImageView("cards/AceCard_Clubs.png");
        imageView.setFitWidth(100);
        imageView.setFitHeight(145);

//        borderPane.setBottom(img1);
////        borderPane.setCenter(img1);
//        borderPane.setLeft(img2);
//        borderPane.setTop(img3);
//        borderPane.setRight(imageView);
//        borderPane.setBottom();
        gridPane.add(img1, 12, 2, 1, 1);
        gridPane.add(img2, 13, 3, 1, 1);
        gridPane.add(img3, 14, 4, 1, 1);
        gridPane.add(imageView, 15, 5, 1, 1);


        gamePanel.getChildren().addAll(cardDisplay, gridPane);
    }

    private void setupScoreAndControls(VBox scoreAndControls) {
        // Game status
        Text gameStatus = new Text("Game Status: Not Started");
        scoreAndControls.getChildren().add(gameStatus);

        // Score table
        TableView<Score> scoreTable = new TableView<>(scores);
        scoreTable.setMinWidth(200);
        scoreTable.setMaxHeight(150);
        setupScoreTable(scoreTable);
        scoreAndControls.getChildren().add(scoreTable);

        // User invitation
        HBox invitationHbox = new HBox(10);
        invitationHbox.visibleProperty().bind(isMyRoom);

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

        invitationHbox.getChildren().addAll(userDropdown, inviteUserButton);

        VBox chat = createChat();

        // Leave game button
        final Button leaveGameButton = new Button("Leave Game");
        leaveGameButton.setStyle(getBigButtonStyle("red"));
        leaveGameButton.setOnAction(e -> {
            sendLeaveGameRequest();
        });

        final Button startGameButton = new Button("Start Game");
        startGameButton.setStyle(getBigButtonStyle("green"));
        startGameButton.visibleProperty().bind(isReadyToStart);



        scoreAndControls.getChildren().addAll(invitationHbox, chat, leaveGameButton, startGameButton);
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

    private void setupScoreTable(TableView<Score> scoreTable) {
        // Set up the score table with dummy data for illustration
        TableColumn<Score, String> playerColumn = new TableColumn<>("Player");
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        playerColumn.prefWidthProperty().bind(scoreTable.widthProperty().multiply(0.7));

        TableColumn<Score, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.prefWidthProperty().bind(scoreTable.widthProperty().multiply(0.25));

        scoreTable.getColumns().addAll(playerColumn, scoreColumn);
    }

    private VBox createChat() {
        VBox vbox = new VBox(20);

        Text chatTitle = new Text("Chat:");
        chatTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        ListView<ChatEntryView> messages = new ListView<>(chat);
        messages.setCellFactory(param -> new ListCell<ChatEntryView>() {
            @Override
            protected void updateItem(ChatEntryView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    final HBox entry = new HBox();
                    final Text emailText = new Text(String.format("%s:  ", item.getEmail()));
                    emailText.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: black;");
                    entry.getChildren().addAll(
                            emailText,
                            new Text(item.getMessage())
                    );
                    setGraphic(entry);
                }
            }
        });

        HBox hbox = new HBox(10);
        final TextField input = new TextField();
        final Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String text = input.getText();
            if (!text.isEmpty()) {
                sendWriteChatRequest(text);
                input.clear();
            }
        });
        hbox.getChildren().addAll(input, sendButton);

        vbox.getChildren().addAll(chatTitle, messages, hbox);
        return vbox;
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

    private void sendWriteChatRequest(String message) {
        try {
            serverConnector.sendMessage(new WriteChatRequest(roomId, message));
        } catch (ClientSocketConnectionException e) {
            Utils.showError("Failed to send Chat message");
        }
    }

    public void handleRoomDetailsResponse(RoomDetailsResponse message) {
        System.out.println("Handling room details response ");
        System.out.println(message.getRoomDetails().isMyRoom());
        System.out.println(message);
        System.out.println(getScores(message.getRoomDetails()));
        Platform.runLater(() -> {
            userList.setAll(message.getRoomDetails().getNonInvitedUsers());
            isMyRoom.set(message.getRoomDetails().isMyRoom());
            isReadyToStart.set(message.getRoomDetails().isReadyToStart());
            scores.setAll(getScores(message.getRoomDetails()));
            chat.setAll(message.getRoomDetails().getChat());
            myCards.setAll(message.getRoomDetails().getMyCards());
        });
    }

    private List<Score> getScores(RoomDetails room) {
        return room.getScores().entrySet().stream()
                .map(entry -> new Score(entry.getKey().getEmail(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public void close() {
        stage.close();
    }

    private String getTitle(int roomId) {
        return String.format("Hearts Game - Room %s", roomId);
    }

    private String getBigButtonStyle(String color) {
        return String.format(
                "-fx-font-size: 20px; -fx-background-color: %s; -fx-padding: 20px; -fx-border-color: black; -fx-border-width: 2px; -fx-border-style: solid;",
                color
        );
    }

    @Data
    public class Score {
        final String email;
        final int score;
    }
}
