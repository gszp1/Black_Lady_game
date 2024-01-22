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

    /**
     * Room's ID.
     */
    private final int roomId;

    /**
     * Label for game status.
     */
    private final Label gameStatusLabel = new Label();

    /**
     * Reference to serverConnector.
     */
    private final ServerConnector serverConnector;

    /**
     * List of users.
     */
    private ObservableList<UserView> userList = FXCollections.observableArrayList();

    /**
     * Game started flag.
     */
    boolean started = false;

    /**
     * Is game ready to start.
     */
    private BooleanProperty isReadyToStart = new SimpleBooleanProperty(false);

    /**
     * Is user owner of started game.
     */
    private BooleanProperty isOwnerOfStartedGame = new SimpleBooleanProperty(false);

    /**
     * Is user owner of room.
     */
    private BooleanProperty isMyRoom = new SimpleBooleanProperty(false);

    /**
     * User's scores.
     */
    ObservableList<Score> scores = FXCollections.observableArrayList();

    /**
     * Chat.
     */
    private ObservableList<ChatEntryView> chat = FXCollections.observableArrayList();

    /**
     * User's cards.
     */
    private ObservableList<Card> myCards = FXCollections.observableArrayList();

    /**
     * Grid pane for displaying cards.
     */
    private GridPane cardsGridPane = new GridPane();

    /**
     * Stage.
     */
    private Stage stage;

    /**
     * Label for displaying this player name (bottom)
     */
    private Label myPlayerLabel = new Label("Player - me");

    /**
     * Label for displaying other player name (Left)
     */
    private Label leftPlayerLabel = new Label("Player - left");

    /**
     * Label for displaying other player name (Top)
     */
    private Label topPlayerLabel = new Label("Player - top");

    /**
     * Label for displaying other player name (Right)
     */
    private Label rightPlayerLabel = new Label("Player - right");

    /**
     * Button for skipping round.
     */
    final Button skipPlayButton = new Button("Skip round");

    /**
     * Opens the game play window, displaying the game UI and managing user interactions.
     * This method sets up the main components, such as the game panel, score, and controls,
     * and initializes the communication with the server to retrieve or update game details.
     * The window includes player labels, a game status label, and buttons for player interactions.
     * The window is responsive to user actions, and closing the window triggers a leave game request.
     */
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

    /**
     * Sets up game panel given by argument.
     * @param gamePanel game panel.
     */
    private void setupGamePanel(VBox gamePanel) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gamePanel.getChildren().addAll(cardsGridPane, gridPane);
    }

    /**
     * Sets up the score and controls section of the game UI, including game status, score table,
     * user invitation, chat, leave game button, start game button, and skip play button.
     * @param scoreAndControls The VBox container to which the score and controls components will be added.
     */
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

    /**
     * Creates a customized ListCell for rendering UserView items in a JavaFX ComboBox.
     * this list cell displays the user email.
     * @return The customized ListCell for user email.
     */
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

    /**
     *  Configures columns of JavaFX scoreTable.
     * @param scoreTable Table of user scores.
     */
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

    /**
     * Sets up VBox representing chat.
     * @return Chat VBox.
     */
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

    /**
     * Sends start game request.
     */
    private void sendStartGameRequest() {
        try {
            serverConnector.sendMessage(new StartGameRequest(roomId));
        } catch (ClientSocketConnectionException e) {
            Utils.showError("Failed to start game");
        }
    }

    /**
     * Sends user invitation.
     * @param userView User view representing user to be invited.
     */
    private void sendInviteUserRequest(UserView userView) {
        try {
            serverConnector.sendMessage(new InviteUserRequest(userView.getEmail(), roomId));
        } catch (ClientSocketConnectionException e) {
            Utils.showError("Failed to send Invitation Request.");
        }
    }

    /**
     * Sends request for room details.
     */
    private void sendRoomDetailsRequest() {
        try {
            serverConnector.sendMessage(new RoomDetailsRequest(roomId));
        } catch (ClientSocketConnectionException e) {
            Utils.showError("Failed to send RoomDetailsRequest.");
        }
    }

    /**
     * Sends request to leave game room.
     */
    private void sendLeaveGameRequest() {
        try {
            serverConnector.sendMessage(new LeaveRoomRequest(roomId));
        } catch (ClientSocketConnectionException e) {
            Utils.showError("Failed to send Leave Room Request");
        }
    }

    /**
     * Sends request for writing message to chat.
     * @param message Message to be inserted into chat.
     */
    private void sendWriteChatRequest(String message) {
        try {
            serverConnector.sendMessage(new WriteChatRequest(roomId, message));
        } catch (ClientSocketConnectionException e) {
            Utils.showError("Failed to send Chat message");
        }
    }

    /**
     * Handles the response containing details about the game room.
     * This method updates the UI components based on the received room details.
     * @param message Message containing data for UI update.
     */
    public void handleRoomDetailsResponse(RoomDetailsResponse message) {
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

    /**
     * Displays cards in UI.
     * @param roomDetails Room details.
     */
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

    /**
     * Shows user's card in trick.
     * @param card Card.
     */
    private void showMyPlayedCard(Card card) {
        cardsGridPane.add(getCardImage(card), 7, 4, 2, 1);
    }

    /**
     * Shows user on left card in trick.
     * @param card Card.
     */
    private void showLeftPlayerCard(Card card) {
        cardsGridPane.add(getCardImage(card), 6, 3);
    }

    /**
     * Shows user on top card in trick.
     * @param card Card.
     */
    private void showTopPlayerCard(Card card) {
        cardsGridPane.add(getCardImage(card), 7, 2, 2, 1);
    }

    /**
     * Shows user on right card in trick.
     * @param card Card.
     */
    private void showRightPlayedCard(Card card) {
        cardsGridPane.add(getCardImage(card), 8, 3);
    }

    /**
     * Displays cards on table.
     * @param cards Cards.
     * @param isMyTurn Is this user's move.
     * @param firstCardOnTable First card in trick.
     */
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


    /**
     * Checks if card is clickable.
     * @param card Card.
     * @param cards Cards.
     * @param isMyTurn Is this user's turn.
     * @param firstCardOnTable First card in trick.
     * @return Boolean
     */
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

    /**
     * Fills left column with cards.
     */
    private void fillLeftColumn() {
        for (int i = 0; i < 7; i++) {
            final ImageView image = Math.abs(3 - i) <= 1 ? getBackCardImage() : getDummyImage();
            cardsGridPane.add(image, 0, i);
        }
        cardsGridPane.add(leftPlayerLabel, 1, 3);
    }

    /**
     * Fills top row with cards.
     */
    private void fillTopRow() {
        for (int i = 0; i < 15; i++) {
            final ImageView image = Math.abs(7 - i) <= 1 ? getBackCardImage() : getDummyImage();
            cardsGridPane.add(image, i, 0);
        }
        cardsGridPane.add(topPlayerLabel, 7, 1);
    }

    /**
     * Fills right column with cards.
     */
    private void fillRightColumn() {
        for (int i = 0; i < 7; i++) {
            final ImageView image = Math.abs(3 - i) <= 1 ? getBackCardImage() : getDummyImage();
            cardsGridPane.add(image, 14, i);
        }
        cardsGridPane.add(rightPlayerLabel, 13, 3);
    }

    /**
     * Sends request to play given card.
     * @param card Card.
     */
    private void sendPlayCardRequest(Card card) {
        try {
            serverConnector.sendMessage(new PlayCardRequest(roomId, card));
        } catch (ClientSocketConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets card's front image.
     * @param card Card.
     * @return Card's front image.
     */
    private ImageView getCardImage(Card card) {
        final ImageView imageView = new ImageView(
          String.format("cards/%s_%s.png", card.getCardType().getCardTypeName(), card.getCardSet().getTypeName())
        );
        imageView.setFitWidth(90);
        imageView.setFitHeight(126);
        return imageView;
    }

    /**
     * Gets card's back image.
     * @return Card's back image.
     */
    private ImageView getBackCardImage() {
        final ImageView emptyCard = new ImageView("cards/Hidden.png");
        emptyCard.setFitWidth(90);
        emptyCard.setFitHeight(126);
        return emptyCard;
    }

    /**
     * Creates dummy image.
     * @return dummy image.
     */
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

    /**
     * Returns players scores.
     * @param room Room.
     * @return List of scores.
     */
    private List<Score> getScores(RoomDetails room) {
        return room.getScores().entrySet().stream()
                .map(entry -> new Score(entry.getKey().getEmail(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Closes stage.
     */
    public void close() {
        stage.close();
    }

    /**
     * Getter for title.
     * @param roomId Room's ID.
     * @return Room's title.
     */
    private String getTitle(int roomId) {
        return String.format("Hearts Game - Room %s", roomId);
    }

    /**
     * Generates and returns a JavaFX style string for styling a button with given properties.
     * @param color Button color.
     * @return Styling string.
     */
    private String getBigButtonStyle(String color) {
        return String.format(
                "-fx-font-size: 20px; -fx-background-color: %s; -fx-padding: 20px; -fx-border-color: black; -fx-border-width: 2px; -fx-border-style: solid;",
                color
        );
    }

    /**
     * Sets player's labels with their emails.
     * @param roomDetails Game room data.
     */
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

    /**
     * Gets styling function for player email labels, user gets green background, while other players get orange one.
     * @param roomDetails Room's data.
     * @return Styling function.
     */
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

    /**
     * Gets a list of player emails ordered in a relative clockwise direction;
     * @param roomDetails Room's data.
     * @return List of emails.
     */
    private List<String> getRelativeClockWisePlayersOrderEmails(RoomDetails roomDetails) {
        final List<String> userIdsOrder = getRelativeClockWisePlayersOrderUserIds(roomDetails);
        final Map<String, String> userIdsToEmailMapping = roomDetails.getUserIdsToEmailsMapping();
        return userIdsOrder.stream().map(userIdsToEmailMapping::get).collect(Collectors.toList());
    }

    /**
     * Gets a list of player IDs ordered in a relative clockwise direction;
     * @param roomDetails Room's data.
     * @return List of IDs.
     */
    private List<String> getRelativeClockWisePlayersOrderUserIds(RoomDetails roomDetails) {
        final List<String> userIdsOrder = roomDetails.getUsersOrder();
        final String myUserId = roomDetails.getMyUserId();
        final List<String> userOrder = new ArrayList<>(userIdsOrder);
        Collections.rotate(userOrder, (-1) * userOrder.indexOf(myUserId));
        return userOrder;
    }

    /**
     * Sets default users' email label styles.
     */
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

    /**
     * Sets label style for player.
     * @param label Styled label.
     */
    private void setPlayingLabelStyle(Label label) {
        label.setStyle("-fx-background-color: green; -fx-font-weight: bold; -fx-border-style: solid; -fx-border-color: black;");
    }

    /**
     * Sets label style for other player.
     * @param label Styled label.
     */
    private void setNotPlayingLabelStyle(Label label) {
        label.setStyle("-fx-background-color: orange; -fx-font-weight: bold; -fx-border-style: solid; -fx-border-color: black;");
    }

    /**
     * Displays trick from previous round.
     * @param roomDetails Room's data.
     */
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

    /**
     * Class for storing user's email and score.
     */
    @Data
    public class Score {
        final String email;
        final int score;
    }
}
