package messages.toServer.requests;

import messages.MessageType;
import messages.toClient.responses.InviteUserResponse;
import messages.toClient.responses.RoomInviteToReceiverRequest;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Request for creating game room.
 */
public class InviteUserRequest extends ToServerMessage {

    /**
     * Receiver email.
     */
    private String receiverEmail;

    /**
     * ID of room to which user is invited.
     */
    private int roomId;

    /**
     * Constructor for message.
     * @param receiverEmail Receiver's email.
     * @param roomId Room's ID.
     */
    public InviteUserRequest(String receiverEmail, int roomId){
        super(MessageType.InviteUserRequest, String.format("Invite user request to room %s sent to email %s", roomId, receiverEmail), null);
        this.receiverEmail = receiverEmail;
        this.roomId = roomId;
    }

    /**
     * Request Handling procedure.
     * @param userList List of users.
     * @param databaseConnector Connection to database.
     * @param gameDetails Data about active game rooms.
     * @return Boolean.
     * @throws IOException Exception for connection errors.
     * @throws SQLException Exception for database connection error.
     */
    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        Optional<User> user = userList.getUserByConnectionId(getConnectionId());
        if (!user.isPresent()) {
            System.out.printf("S: Cannot find user by connection id: %s\n", getConnectionId());
            return false;
        }
        Optional<User> userToInvite = userList.getUserByEmail(receiverEmail);
        if (!userToInvite.isPresent()){
            sendBackError(user.get(), String.format("User %s does not exist.", user.get().getEmail()));
            return false;
        }
        sendInvitationRequestToUser(user.get(), userToInvite.get());
        return true;
    }

    /**
     * Sends invitation to user.
     * @param sender Invitation sender.
     * @param receiver Invitation receiver.
     */
    private void sendInvitationRequestToUser(User sender, User receiver) {
        try {
            receiver.getOutputStream().writeObject(new RoomInviteToReceiverRequest(receiverEmail, sender.getEmail(), roomId));
        } catch (IOException e) {
            System.out.println("S: Failed to send invitation request to user.");
        }
    }

    /**
     * Sends error to sender.
     * @param user Invitation sender.
     * @param error Error message.
     */
    private void sendBackError(User user, String error) {
        try {
            user.getOutputStream().writeObject(new InviteUserResponse(ToServerMessage.SUCCESS, error));
        } catch (IOException e) {
            System.out.println("S: Failed to send back error regarding invitation request.");
        }
    }
}
