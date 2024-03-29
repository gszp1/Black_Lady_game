package client.ui.stages;

import client.ServerConnector;
import exceptions.ClientSocketConnectionException;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import messages.MessageType;
import messages.toClient.ToClientMessage;
import messages.toClient.responses.LoginResponse;
import messages.toServer.requests.LoginRequest;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Stage for user login."
 */
public class LoginStage extends GameStage {

    /**
     * Stage title.
     */
    private String TITLE = "Login page";

    /**
     * Label for displaying notifications.
     */
    private final Label loginNotificationLabel = new Label("");

    /**
     * Button for moving to register stage.
     */
    private final Button changeToRegisterStageButton = new Button("Move to Register");

    /**
     * Text field for email.
     */
    private TextField emailTextField;

    /**
     * Text field for password.
     */
    private TextField passwordField;

    /**
     * Constructor for LoginStage.
     * @param primaryStage Reference to primaryStage.
     * @param serverConnector Reference to serverConnector.
     * @param changeStageHandler Handler for changing stages.
     */
    public LoginStage(
            Stage primaryStage,
            ServerConnector serverConnector,
            Consumer<GameStageType> changeStageHandler
    ) {
        super(primaryStage, serverConnector, changeStageHandler);
    }

    /**
     * Actions to do when login stage is opened.
     */
    @Override
    public void onOpen() {
        primaryStage.setTitle(TITLE);
        GridPane grid = getMainGrid();
        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
    }

    /**
     * Creates main grid pane.
     * @return Main grid pane.
     */
    private GridPane getMainGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        addGridElements(grid);
        addGridConstraints(grid);

        return grid;
    }

    /**
     * Adds elements to gridPane.
     * @param grid Grid pane.
     */
    private void addGridElements(GridPane grid) {
        // Create UI components
        Label emailLabel = new Label("Email:");
        emailTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        // Add components to the GridPane
        grid.add(emailLabel, 0, 0);
        grid.add(emailTextField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2);
        grid.add(changeToRegisterStageButton, 1, 2);
        grid.add(loginNotificationLabel, 0, 3, 2, 1);

        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setHalignment(changeToRegisterStageButton, HPos.CENTER);

        loginButton.setOnAction(event -> {
            Optional<String> maybeError = validateForm();
            setErrorLoginLabel(maybeError.orElse(""));

            final String email = emailTextField.getText().trim();
            final String password = passwordField.getText().trim();

            LoginRequest loginRequest = new LoginRequest(email, password, null);
            try {
                serverConnector.sendMessage(loginRequest);
            } catch (ClientSocketConnectionException e) {
                setErrorLoginLabel(e.getErrorCause()) ;
            }
        });
        changeToRegisterStageButton.setOnAction(e -> changeStageHandler.accept(GameStageType.REGISTER));
    }

    /**
     * Validates form's contents.
     * @return Optional with error message.
     */
    private Optional<String> validateForm() {
        if (emailTextField.getText().trim().isEmpty()) {
            return Optional.of("Email cannot be empty");
        }
        if (passwordField.getText().trim().isEmpty()) {
            return Optional.of("Password cannot be empty");
        }
        return Optional.empty();
    }

    /**
     * Adds constraint to grid.
     * @param grid Grid.
     */
    private void addGridConstraints(GridPane grid) {
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(column1, column2);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS); // Allow row 4 to grow
        row2.setVgrow(Priority.ALWAYS); // Allow row 4 to grow
        row3.setVgrow(Priority.ALWAYS); // Allow row 4 to grow
        grid.getRowConstraints().addAll(row1, row2, row3);
    }

    /**
     * Sets notification contents with given message.
     * @param message Message to be inserted into label.
     */
    public void setErrorLoginLabel(String message) {
        loginNotificationLabel.setText(message);
        loginNotificationLabel.setStyle("-fx-text-fill: red;");
    }

    /**
     * Sets notification contents with given message.
     * @param message Message to be inserted into label.
     */
    public void setSuccessLoginLabel(String message) {
        loginNotificationLabel.setText(message);
        loginNotificationLabel.setStyle("-fx-text-fill: green;");
    }

    /**
     * Message handler for login .
     * @param message Handled message.
     */
    @Override
    public void handleMessage(ToClientMessage message) {
        if (message.getMessageType() == MessageType.LoginResponse){
            LoginResponse loginResponse = (LoginResponse) message;
            Platform.runLater(() -> {
                if (!loginResponse.isSuccess()) {
                    setErrorLoginLabel(message.getData());
                } else {
                    changeStageHandler.accept(GameStageType.ROOM_PANEL);
                }
            });
        }
    }
}
