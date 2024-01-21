package client.ui.panel;

import client.ServerConnector;
import exceptions.ClientSocketConnectionException;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Data;
import messages.toServer.requests.RoomInviteAcceptRequest;
import messages.toServer.requests.RoomInviteDenyRequest;

import java.io.IOException;

/**
 * Class for invitation window.
 */
@Data
public class RoomInviteDialog {

    /**
     * Title of window.
     */
    public static String TITLE = "Room Invitation.";

    /**
     * ID of room to which user was invited.
     */
    private final int roomId;

    /**
     * Email of receiver.
     */
    private final String receiverEmail;

    /**
     * Email of sender.
     */
    private final String senderEmail;

    /**
     * Reference to server connector.
     */
    private final ServerConnector serverConnector;

    /**
     * Opens invitation window.
     */
    public void open() {
        final Stage roomInviteStage = new Stage();
        final VBox vbox = new VBox(10);
        final Label inviteLabel = new Label(String.format("You were invited to room %s by user %s.", roomId, senderEmail));
        inviteLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: black;");

        final HBox hbox = new HBox(10);
        final Button acceptButton = new Button("Accept");
        acceptButton.setOnAction(e -> {
            sendRoomInviteAcceptRequest();
            roomInviteStage.close();
        });

        final Button rejectButton = new Button("Reject");
        rejectButton.setOnAction(e -> {
            sendRoomInviteDenyRequest();
            roomInviteStage.close();
        });
        hbox.getChildren().addAll(acceptButton, rejectButton);

        vbox.getChildren().addAll(inviteLabel, hbox);

        Scene scene = new Scene(vbox, 500, 80);

        roomInviteStage.setTitle(TITLE);
        roomInviteStage.setScene(scene);
        roomInviteStage.show();
    }

    /**
     * Sends invitation denial response.
     */
    private void sendRoomInviteDenyRequest() {
        try {
            serverConnector.sendMessage(new RoomInviteDenyRequest(roomId, receiverEmail, senderEmail));
        } catch (ClientSocketConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends invitation accept response.
     */
    private void sendRoomInviteAcceptRequest() {
        try {
            serverConnector.sendMessage(new RoomInviteAcceptRequest(roomId, receiverEmail, senderEmail));
        } catch (ClientSocketConnectionException e) {
            throw new RuntimeException(e);
        }
    }
}
