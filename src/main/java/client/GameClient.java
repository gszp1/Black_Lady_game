package client;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class GameClient extends Application{

    private boolean registrationWindowOpened = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login page");

        // Create a GridPane layout
        GridPane grid = getLoginGrid();

        // Create a Scene and set it on the Stage
        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);

        // Show the Stage
        primaryStage.show();
    }

    private GridPane getLoginGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        addLoginGridComponents(grid);
        addGridConstraints(grid);

        return grid;
    }

    public void addLoginGridComponents(GridPane grid) {
        // Create UI components
        Label emailLabel = new Label("Email:");
        TextField emailTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Label notificationLabel = new Label("");

        // Add components to the GridPane
        grid.add(emailLabel, 0, 0);
        grid.add(emailTextField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);
        grid.add(notificationLabel, 0, 0, 2, 1);

        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setHalignment(registerButton, HPos.CENTER);

        registerButton.setOnAction(e ->openRegistrationWindow());
    }

    public void addGridConstraints(GridPane grid) {
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

    private void setErrorLabel(String message, Label label) {
        label.setText(message);
        label.setStyle("-fx-text-fill: red;");
    }

    private void setSuccessLabel(String message, Label label) {
        label.setText(message);
        label.setStyle("-fx-text-fill: green;");
    }

    public static void main(String[] args) throws IOException {
//        Socket socket = new Socket("0.0.0.0", 8080);
        launch(args);
    }

    private void openRegistrationWindow() {
        if (registrationWindowOpened) {
            return;
        }
        registrationWindowOpened = true;

        Stage newStage = new Stage();
        newStage.setTitle("Registration Form");
        GridPane newGrid = new GridPane();
        newGrid.setHgap(10);
        newGrid.setVgap(10);
        newGrid.setPadding(new Insets(20, 20, 20, 20));

        // Create UI components for the new window
        addRegistrationGridComponents(newGrid);

        // Configure layout constraints for the new window
        addGridConstraints(newGrid);

        // Set the new GridPane as the content of the new Stage
        Scene newScene = new Scene(newGrid, 400, 200);
        newStage.setScene(newScene);

        // Show the new Stage
        newStage.show();
    }

    public void addRegistrationGridComponents(GridPane grid) {
        // Create UI components for the new window
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField passwordConfirmationField = new PasswordField();
        TextField emailField = new TextField();

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
        grid.add(registerButton, 0, 4, 2, 1); // span button across two columns
        GridPane.setHalignment(registerButton, HPos.CENTER);
    }

}

