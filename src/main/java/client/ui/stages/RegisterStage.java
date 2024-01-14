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

public class RegisterStage extends GameStage {

    private String TITLE = "Register page";

    private Label registerNotificationLabel = new Label("");

    private Button changeToLoginStageButton = new Button("Move to Login");

    private TextField emailField;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField passwordConfirmationField;

    public RegisterStage(
            Stage primaryStage,
            ServerConnector serverConnector,
            Consumer<GameStageType> changeStageHandler
    ) {
        super(primaryStage, serverConnector, changeStageHandler);
    }

    @Override
    public void onOpen() {
        primaryStage.setTitle(TITLE);
        GridPane grid = getMainGrid();
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
    }

    private GridPane getMainGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        addGridConstraints(grid);
        addRegistrationGridComponents(grid);
        return grid;
    }

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

    private void handleRegisterClicked() throws ClientSocketConnectionException {
        final FormData formData = new FormData();
        Optional<String> formError = getFormError(formData);
        setErrorRegisterLabel(formError.orElse(""));
        if (formError.isPresent()){
            return;
        }
        final RegisterRequest registerRequest = formData.toRegisterRequest();
        serverConnector.sendMessage(registerRequest);

//        String password = passwordField.getText().trim();
//        if (!password.equals(passwordConfirmationField.getText().trim())) {
//            setErrorRegisterLabel("Passwords do not match");
//        }
    }

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

    public void setErrorRegisterLabel(String message) {
        registerNotificationLabel.setText(message);
        registerNotificationLabel.setStyle("-fx-text-fill: red;");
    }

    public void setSuccessRegisterLabel(String message) {
        registerNotificationLabel.setText(message);
        registerNotificationLabel.setStyle("-fx-text-fill: green;");
    }

    @Override
    public void handleMessage(ToClientMessage message) {
        Platform.runLater(() -> setErrorRegisterLabel(message.getData()));
    }

    class FormData {
        String email;
        String username;
        String password;
        String passwordConfirmation;

        FormData() {
            this.email = emailField.getText().trim();
            this.username = usernameField.getText().trim();
            this.password = passwordField.getText().trim();
            this.passwordConfirmation = passwordConfirmationField.getText().trim();
        }

        RegisterRequest toRegisterRequest() {
            return new RegisterRequest(email, username, password);
        }
    }
}
