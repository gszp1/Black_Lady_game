package client.ui.panel;

import cards.Card;
import client.ServerConnector;
import client.ui.utils.Utils;
import exceptions.ClientSocketConnectionException;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Data;
import javafx.scene.image.ImageView;
import messages.dto.ChatEntryView;
import messages.dto.RoomDetails;
import messages.dto.UserView;
import messages.toClient.responses.RoomDetailsResponse;
import messages.toServer.requests.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Data
public class GameRoomPlay {

    private final int roomId;

    private final Label gameStatusLabel = new Label();

    private final ServerConnector serverConnector;

    private ObservableList<UserView> userList = FXCollections.observableArrayList();

    boolean started = false;

    private BooleanProperty isReadyToStart = new SimpleBooleanProperty(false);

    private BooleanProperty isOwnerOfStartedGame = new SimpleBooleanProperty(false);

    private BooleanProperty isMyRoom = new SimpleBooleanProperty(false);

    ObservableList<Score> scores = FXCollections.observableArrayList();

    private ObservableList<ChatEntryView> chat = FXCollections.observableArrayList();

    private ObservableList<Card> myCards = FXCollections.observableArrayList();

    private GridPane cardsGridPane = new GridPane();

    private Stage stage;

    private Label myPlayerLabel = new Label("Player - me");

    private Label leftPlayerLabel = new Label("Player - left");

    private Label topPlayerLabel = new Label("Player - top");

    private Label rightPlayerLabel = new Label("Player - right");

    final Button skipPlayButton = new Button("Skip round");

    public void open() {

        setDefaultPlayerLabelsStyle();
        HBox root = new HBox(20);

        VBox gamePanel = new VBox(10);
        HBox.setHgrow(gamePanel, Priority.ALWAYS);
        setupGamePanel(gamePanel);

        VBox scoreAndControls = new VBox(10);
        HBox.setHgrow(scoreAndControls, Priority.NEVER);
        setupScoreAndControls(scoreAndControls);

        root.getChildren().addAll(gameStatusLabel, gamePanel, scoreAndControls);

        Scene primaryScene = new Scene(root, 1750, 1000);

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

//
//        BorderPane borderPane = new BorderPane();
//        Text cardDisplay = new Text("Player's cards here");
//        final ImageView img1 = new ImageView("cards/AceCard_Diamonds.png");
//        img1.setFitWidth(100);
//        img1.setFitHeight(145);
//        final ImageView img2 = new ImageView("cards/AceCard_Hearts.png");
//        img2.setFitWidth(100);
//        img2.setFitHeight(145);
//        final ImageView img3 = new ImageView("cards/AceCard_Spades.png");
//        img3.setFitWidth(100);
//        img3.setFitHeight(145);
//        final ImageView imageView = new ImageView("cards/AceCard_Clubs.png");
//        imageView.setFitWidth(100);
//        imageView.setFitHeight(145);

//        borderPane.setBottom(img1);
////        borderPane.setCenter(img1);
//        borderPane.setLeft(img2);
//        borderPane.setTop(img3);
//        borderPane.setRight(imageView);
//        borderPane.setBottom();
//        gridPane.add(img1, 12, 2, 1, 1);
//        gridPane.add(img2, 13, 3, 1, 1);
//        gridPane.add(img3, 14, 4, 1, 1);
//        gridPane.add(imageView, 15, 5, 1, 1);


        gamePanel.getChildren().addAll(cardsGridPane, gridPane);
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
        startGameButton.setOnAction(e -> {
            sendStartGameRequest();
        });

        skipPlayButton.setStyle(getBigButtonStyle("green"));
        skipPlayButton.visibleProperty().bind(isOwnerOfStartedGame);
        skipPlayButton.setOnAction(e -> {
            try {
                serverConnector.sendMessage(new SkipPlayRequest(roomId));
            } catch (ClientSocketConnectionException ex) {
                Utils.showError("Failed to send skip play request");
            }
        });
        scoreAndControls.getChildren().addAll(invitationHbox, chat, leaveGameButton, startGameButton, skipPlayButton);
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

    private void sendStartGameRequest() {
        try {
            serverConnector.sendMessage(new StartGameRequest(roomId));
        } catch (ClientSocketConnectionException e) {
            Utils.showError("Failed to start game");
        }
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

        System.out.println("Cards on table:");
        System.out.println(message.getRoomDetails().getCardsOnTable());

        Platform.runLater(() -> {
            if (message.getRoomDetails().isMyTurn()) {
                gameStatusLabel.setText("Your turn!");
            } else {
                gameStatusLabel.setText("Not your turn");
            }
            gameStatusLabel.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold;");
            userList.setAll(message.getRoomDetails().getNonInvitedUsers());
            isMyRoom.set(message.getRoomDetails().isMyRoom());
            isReadyToStart.set(message.getRoomDetails().isReadyToStart());
            scores.setAll(getScores(message.getRoomDetails()));
            chat.setAll(message.getRoomDetails().getChat());
            myCards.setAll(message.getRoomDetails().getMyCards());
            isOwnerOfStartedGame.set(message.getRoomDetails().isMyRoom() && message.getRoomDetails().isStarted());
            if (message.getRoomDetails().isStarted()) {
                displayCards(
                        myCards,
                        message.getRoomDetails().isMyTurn(),
                        Optional.ofNullable(message.getRoomDetails().getFirstCardOnTable())
                );
                setPlayerLabels(message.getRoomDetails());
                displayCardsOnTable(message.getRoomDetails());
                displayLastTrick(message.getRoomDetails());
            } else if (message.getRoomDetails().isFinished()) {
                cardsGridPane.getChildren().clear();
                final Label finishedGameLabel = new Label("Game has finished");
                setNotPlayingLabelStyle(finishedGameLabel);
                cardsGridPane.add(finishedGameLabel, 7, 4);
            }
        });
    }

    private void displayCardsOnTable(RoomDetails roomDetails) {
        final List<String> relativeUserIdsOrder = getRelativeClockWisePlayersOrderUserIds(roomDetails);
        final Map<String, Card> cardsOnTable = roomDetails.getCardsOnTable();

        final String myUserId = relativeUserIdsOrder.get(0);
        final String leftUserId = relativeUserIdsOrder.get(1);
        final String topUserId = relativeUserIdsOrder.get(2);
        final String rightUserId = relativeUserIdsOrder.get(3);

        if (cardsOnTable.containsKey(myUserId)) {
            showMyPlayedCard(cardsOnTable.get(myUserId));
        }
        if (cardsOnTable.containsKey(leftUserId)) {
            showLeftPlayerCard(cardsOnTable.get(leftUserId));
        }
        if (cardsOnTable.containsKey(topUserId)) {
            showTopPlayerCard(cardsOnTable.get(topUserId));
        }
        if (cardsOnTable.containsKey(rightUserId)) {
            showRightPlayedCard(cardsOnTable.get(rightUserId));
        }
    }

    private void showMyPlayedCard(Card card) {
        cardsGridPane.add(getCardImage(card), 7, 4, 2, 1);
    }

    private void showLeftPlayerCard(Card card) {
        cardsGridPane.add(getCardImage(card), 6, 3);
    }

    private void showTopPlayerCard(Card card) {
        cardsGridPane.add(getCardImage(card), 7, 2, 2, 1);
    }

    private void showRightPlayedCard(Card card) {
        cardsGridPane.add(getCardImage(card), 8, 3);
    }

    private void displayCards(List<Card> cards, boolean isMyTurn, Optional<Card> firstCardOnTable) {
        cardsGridPane.getChildren().clear();
        for (int i = 0; i < cards.size(); i++) {
            final Card card = cards.get(i);
            final ImageView img = getCardImage(cards.get(i));
            if (isCardClickable(card, cards, isMyTurn, firstCardOnTable)) {
                img.setOnMouseClicked(event -> {
                    sendPlayCardRequest(card);
                });
            }
            cardsGridPane.add(getDummyImage(), i + 1, 0);
            cardsGridPane.add(img, i + 1, 6);
        }
        cardsGridPane.add(myPlayerLabel, 7, 5);
        fillLeftColumn();
        fillTopRow();
        fillRightColumn();
    }

    private boolean isCardClickable(Card card, List<Card> cards, boolean isMyTurn, Optional<Card> firstCardOnTable) {
        if (!isMyTurn) {
            return false;
        }
        if (!firstCardOnTable.isPresent()) {
            return true;
        }
        final boolean anyCardsMatchFirstCardColor = cards.stream()
                .map(Card::getCardSet)
                .anyMatch(firstCardOnTable.get().getCardSet()::equals);
        if (anyCardsMatchFirstCardColor) {
            return card.getCardSet().equals(firstCardOnTable.get().getCardSet());
        }
        return true;
    }

    private void fillLeftColumn() {
        for (int i = 0; i < 7; i++) {
            final ImageView image = Math.abs(3 - i) <= 1 ? getBackCardImage() : getDummyImage();
            cardsGridPane.add(image, 0, i);
        }
        cardsGridPane.add(leftPlayerLabel, 1, 3);
    }

    private void fillTopRow() {
        for (int i = 0; i < 15; i++) {
            final ImageView image = Math.abs(7 - i) <= 1 ? getBackCardImage() : getDummyImage();
            cardsGridPane.add(image, i, 0);
        }
        cardsGridPane.add(topPlayerLabel, 7, 1);
    }

    private void fillRightColumn() {
        for (int i = 0; i < 7; i++) {
            final ImageView image = Math.abs(3 - i) <= 1 ? getBackCardImage() : getDummyImage();
            cardsGridPane.add(image, 14, i);
        }
        cardsGridPane.add(rightPlayerLabel, 13, 3);
    }

    private void sendPlayCardRequest(Card card) {
        try {
            serverConnector.sendMessage(new PlayCardRequest(roomId, card));
        } catch (ClientSocketConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    private ImageView getSmallCardImage(Card card) {
        final ImageView imageView = new ImageView(
            String.format("cards/%s_%s.png", card.getCardType().getCardTypeName(), card.getCardSet().getTypeName())
        );
        imageView.setFitWidth(10);
        imageView.setFitHeight(14);
        return imageView;
    }

    private ImageView getCardImage(Card card) {
        final ImageView imageView = new ImageView(
          String.format("cards/%s_%s.png", card.getCardType().getCardTypeName(), card.getCardSet().getTypeName())
        );
        imageView.setFitWidth(90);
        imageView.setFitHeight(126);
        return imageView;
    }

    private ImageView getBackCardImage() {
        final ImageView emptyCard = new ImageView("cards/Hidden.png");
        emptyCard.setFitWidth(90);
        emptyCard.setFitHeight(126);
        return emptyCard;
    }

    private ImageView getDummyImage() {
        int width = 90;
        int height = 126;
        final WritableImage writableImage = new WritableImage(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writableImage.getPixelWriter().setColor(x, y, Color.rgb(244, 244, 244, 255.0 / 255));
            }
        }
        return new ImageView(writableImage);
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

    private void setPlayerLabels(RoomDetails roomDetails) {
        Function<String, String> getStyle = getStyleFunction(roomDetails);
        final List<String> relativeUserEmailsOrder = getRelativeClockWisePlayersOrderEmails(roomDetails);

        myPlayerLabel.setText(relativeUserEmailsOrder.get(0));
        myPlayerLabel.setStyle(getStyle.apply(relativeUserEmailsOrder.get(0)));

        leftPlayerLabel.setText(relativeUserEmailsOrder.get(1));
        leftPlayerLabel.setStyle(getStyle.apply(relativeUserEmailsOrder.get(1)));

        topPlayerLabel.setText(relativeUserEmailsOrder.get(2));
        topPlayerLabel.setStyle(getStyle.apply(relativeUserEmailsOrder.get(2)));

        rightPlayerLabel.setText(relativeUserEmailsOrder.get(3));
        rightPlayerLabel.setStyle(getStyle.apply(relativeUserEmailsOrder.get(3)));
    }

    private Function<String, String> getStyleFunction(RoomDetails roomDetails) {
        return (String email) -> {
            Optional<String> userId = roomDetails.getUserIdsToEmailsMapping().entrySet().stream()
                    .filter(idToEmail -> idToEmail.getValue().equals(email))
                    .map(Map.Entry::getKey)
                    .findFirst();
            if (userId.isPresent() && roomDetails.getCurrentPlayingUserId().equals(userId.get())) {
                return "-fx-background-color: green; -fx-font-weight: bold; -fx-border-style: solid; -fx-border-color: black;";
            }
            return "-fx-background-color: orange; -fx-font-weight: bold; -fx-border-style: solid; -fx-border-color: black;";
        };
    }

    private List<String> getRelativeClockWisePlayersOrderEmails(RoomDetails roomDetails) {
        final List<String> userIdsOrder = getRelativeClockWisePlayersOrderUserIds(roomDetails);
        final Map<String, String> userIdsToEmailMapping = roomDetails.getUserIdsToEmailsMapping();
        return userIdsOrder.stream().map(userIdsToEmailMapping::get).collect(Collectors.toList());
    }

    private List<String> getRelativeClockWisePlayersOrderUserIds(RoomDetails roomDetails) {
        final List<String> userIdsOrder = roomDetails.getUsersOrder();
        final String myUserId = roomDetails.getMyUserId();
        final List<String> userOrder = new ArrayList<>(userIdsOrder);
        Collections.rotate(userOrder, (-1) * userOrder.indexOf(myUserId));
        return userOrder;
    }

    private void setDefaultPlayerLabelsStyle() {
        setNotPlayingLabelStyle(myPlayerLabel);
        myPlayerLabel.setWrapText(true);
        setNotPlayingLabelStyle(leftPlayerLabel);
        leftPlayerLabel.setWrapText(true);
        setNotPlayingLabelStyle(topPlayerLabel);
        topPlayerLabel.setWrapText(true);
        setNotPlayingLabelStyle(rightPlayerLabel);
        rightPlayerLabel.setWrapText(true);
    }

    private void setPlayingLabelStyle(Label label) {
        label.setStyle("-fx-background-color: green; -fx-font-weight: bold; -fx-border-style: solid; -fx-border-color: black;");
    }

    private void setNotPlayingLabelStyle(Label label) {
        label.setStyle("-fx-background-color: orange; -fx-font-weight: bold; -fx-border-style: solid; -fx-border-color: black;");
    }

    private void displayLastTrick(RoomDetails roomDetails) {
        final Map<String, Card> trickCards = roomDetails.getLastTrick();
        final List<String> clockWiseUserIds = getRelativeClockWisePlayersOrderUserIds(roomDetails);
        if (clockWiseUserIds.size() != trickCards.size()) {
            return;
        }

        final ImageView bottomCard = getCardImage(trickCards.get(clockWiseUserIds.get(0)));
        final ImageView leftCard = getCardImage(trickCards.get(clockWiseUserIds.get(1)));
        final ImageView topCard = getCardImage(trickCards.get(clockWiseUserIds.get(2)));
        final ImageView rightCard = getCardImage(trickCards.get(clockWiseUserIds.get(3)));

        final Label lastTrickLabel = new Label("Last trick ->");
        lastTrickLabel.setWrapText(true);
        lastTrickLabel.setStyle("-fx-background-color: lightblue; -fx-font-weight: bold; -fx-border-style: solid; -fx-border-color: black;");

        cardsGridPane.add(bottomCard, 13, 1);
        cardsGridPane.add(leftCard, 12, 0);
        cardsGridPane.add(topCard, 13, 0);
        cardsGridPane.add(rightCard, 14, 0);
        cardsGridPane.add(lastTrickLabel, 11, 0);
    }

    @Data
    public class Score {
        final String email;
        final int score;
    }
}
