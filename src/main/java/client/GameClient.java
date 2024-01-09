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
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

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
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(column1, column2);

        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setHalignment(registerButton, HPos.CENTER);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS); // Allow row 4 to grow
        row2.setVgrow(Priority.ALWAYS); // Allow row 4 to grow
        row3.setVgrow(Priority.ALWAYS); // Allow row 4 to grow
        grid.getRowConstraints().addAll(row1, row2, row3);

        // Create a Scene and set it on the Stage
        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);

        // Show the Stage
        primaryStage.show();
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
    }
}

