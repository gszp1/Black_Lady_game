package client.ui.stages;

import client.ServerConnector;
import exceptions.ClientSocketConnectionException;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.geometry.HPos;
import javafx.stage.Stage;
import messages.toClient.ToClientMessage;
import messages.toServer.requests.RegisterRequest;
import utils.Utils;

import java.util.Optional;

import java.util.function.Consumer;

/**
 * Stage for user registration."
 */
public class RegisterStage extends GameStage {

    /**
     * Page name.
     */
    private String TITLE = "Register page";

    /**
     * Label for displaying register notifications.
     */
    private Label registerNotificationLabel = new Label("");

    /**
     * Button for moving to logsin stage.
     */
    private Button changeToLoginStageButton = new Button("Move to Login");

    /**
     * Field for inserting email.
     */
    private TextField emailField;
    /**
     * Field for inserting username.
     */
    private TextField usernameField;
    /**
     * Field for inserting password.
     */
    private PasswordField passwordField;
    /**
     * Field for inserting password confirmation.
     */
    private PasswordField passwordConfirmationField;

    /**
     * Constructor, sets fields from GameStage.
     * @param primaryStage Reference to primaryStage.
     * @param serverConnector Reference to serverConnector.
     * @param changeStageHandler Handler for changing stages.
     */
    public RegisterStage(
            Stage primaryStage,
            ServerConnector serverConnector,
            Consumer<GameStageType> changeStageHandler
    ) {
        super(primaryStage, serverConnector, changeStageHandler);
    }

    /**
     * Operations performed upon changing stages.
     */
    @Override
    public void onOpen() {
        primaryStage.setTitle(TITLE);
        GridPane grid = getMainGrid();
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
    }

    /**
     * Creates and initializes main grid.
     * @return Created grid.
     */
    private GridPane getMainGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        addGridConstraints(grid);
        addRegistrationGridComponents(grid);
        return grid;
    }

    /**
     * Adds constraints to grid pane.
     * @param grid Grid pane to which constraints are meant to be added.
     */
    private void addGridConstraints(GridPane grid) {
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(column1, column2);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);
        row2.setVgrow(Priority.ALWAYS);
        row3.setVgrow(Priority.ALWAYS);
        grid.getRowConstraints().addAll(row1, row2, row3);
    }

    /**
     * Adds components of grid pane for Registration view.
     * @param grid Grid pane to which components are added.
     */
    public void addRegistrationGridComponents(GridPane grid) {
        // Create UI components for the new window
        emailField = new TextField();
        usernameField = new TextField();
        passwordField = new PasswordField();
        passwordConfirmationField = new PasswordField();

        Label emailLabel = new Label("Email:");
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label passwordConfirmationLabel = new Label("Confirm password:");

        Button registerButton = new Button("Register");

        // Add components to the new GridPane
        grid.add(emailLabel, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(passwordConfirmationLabel, 0, 3);
        grid.add(passwordConfirmationField, 1, 3);
        grid.add(registerButton, 0, 4, 1, 1); // span button across two columns
        grid.add(changeToLoginStageButton, 1, 4, 1, 1);
        grid.add(registerNotificationLabel, 0, 6, 2, 1);
        GridPane.setHalignment(registerButton, HPos.CENTER);

        registerButton.setOnAction(event -> {
            try {
                handleRegisterClicked();
            } catch (ClientSocketConnectionException e) {
                setErrorRegisterLabel(e.getErrorCause());
            }
        });
        changeToLoginStageButton.setOnAction(e -> changeStageHandler.accept(GameStageType.LOGIN));
    }

    /**
     * Creates form based on data in text fields, creates register request and sends it to server.
     * @throws ClientSocketConnectionException Connection error excepiton.
     */
    private void handleRegisterClicked() throws ClientSocketConnectionException {
        final FormData formData = new FormData();
        Optional<String> formError = getFormError(formData);
        setErrorRegisterLabel(formError.orElse(""));
        if (formError.isPresent()){
            return;
        }
        final RegisterRequest registerRequest = formData.toRegisterRequest();
        serverConnector.sendMessage(registerRequest);
    }

    /**
     * Validates credentials in form.
     * @param formData Form.
     * @return Optional with error.
     */
    private Optional<String> getFormError(FormData formData) {
        if (formData.email.isEmpty()) {
            return Optional.of("Email field is empty");
        }
        if (!Utils.isEmailValid(formData.email)){
            return Optional.of("Email is not valid");
        }
        if (formData.username.isEmpty()) {
            return Optional.of("Username field is empty");
        }
        if (formData.password.isEmpty()) {
            return Optional.of("Password field is empty");
        }
        if (formData.passwordConfirmation.isEmpty()) {
            return Optional.of("Password confirmation field is empty");
        }
        if (!formData.password.equals(formData.passwordConfirmation)) {
            return Optional.of("Passwords do not match");
        }
        return Optional.empty();
    }

    /**
     * Sets notification label with given message and failure.
     * @param message Message to be set into notification label.
     */
    public void setErrorRegisterLabel(String message) {
        registerNotificationLabel.setText(message);
        registerNotificationLabel.setStyle("-fx-text-fill: red;");
    }

    /**
     * Sets notification label with given message and success.
     * @param message Message to be set into notification label.
     */
    public void setSuccessRegisterLabel(String message) {
        registerNotificationLabel.setText(message);
        registerNotificationLabel.setStyle("-fx-text-fill: green;");
    }

    /**
     * Sets register label with data from message.
     * @param message Received message.
     */
    @Override
    public void handleMessage(ToClientMessage message) {
        Platform.runLater(() -> setErrorRegisterLabel(message.getData()));
    }

    /**
     * Inner class representing data form.
     */
    class FormData {
        String email;
        String username;
        String password;
        String passwordConfirmation;

        /**
         * Constructor sets fields using data in textfields.
         */
        FormData() {
            this.email = emailField.getText().trim();
            this.username = usernameField.getText().trim();
            this.password = passwordField.getText().trim();
            this.passwordConfirmation = passwordConfirmationField.getText().trim();
        }

        /**
         * Generates Register request using data from form.
         * @return Register request.
         */
        RegisterRequest toRegisterRequest() {
            return new RegisterRequest(email, username, password);
        }
    }
}
