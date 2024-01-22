package messages.toServer.requests;

import messages.MessageType;
import messages.toClient.responses.InviteUserResponse;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.Room;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Request for invitation denial.
 */

public class RoomInviteDenyRequest extends ToServerMessage {

    /**
     * Room's ID.
     */
    private final int roomId;

    /**
     * Inviter's email.
     */
    private final String inviterEmail;

    /**
     * Denier's email.
     */
    private final String denierEmail;

    /**
     * Constructor.
     * @param roomId Room's ID.
     * @param inviterEmail Inviter's email.
     * @param denierEmail Denier's email.
     */
    public RoomInviteDenyRequest(int roomId, String inviterEmail, String denierEmail) {
        super(MessageType.RoomInviteDenyRequest, String.format("%s|%s|%s", roomId, inviterEmail, denierEmail), null);
        this.roomId = roomId;
        this.inviterEmail = inviterEmail;
        this.denierEmail = denierEmail;
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
        Optional<User> denier = userList.getUserByConnectionId(getConnectionId());
        if (!denier.isPresent()) {
            System.out.printf("User %s is cannot be fetched from user id.\n", getConnectionId());
            return false;
        }
        Optional<User> inviter = userList.getUserByEmail(inviterEmail);
        if(!inviter.isPresent()) {
            sendError(denier.get(), String.format("User %s does not exist", inviterEmail));
            return false;
        }
        Optional<Room> room = gameDetails.getRoomById(roomId);
        if (!room.isPresent()) {
            sendError(denier.get(), String.format("Room with id %s does not exist", roomId));
            sendError(inviter.get(), String.format("Room with id %s does not exist", roomId));
            return false;
        }

        sendSuccess(inviter.get(), String.format("User %s denied your invitation to room %s", inviterEmail, roomId));
        return true;
    }

    /**
     * Method for sending error.
     * @param user Error message receiver.
     * @param msg Error message.
     */
    private void sendError(User user, String msg) {
        try {
            user.getOutputStream().writeObject(new InviteUserResponse(ToServerMessage.FAILURE, msg));
        } catch (IOException e) {
            System.out.printf("Failed to send InviteUserResponse to user %s.\n", user.getEmail());
        }
    }

    /**
     * Method for sending success message.
     * @param user Success message receiver.
     * @param msg Success message.
     */
    private void sendSuccess(User user, String msg) {
        try {
            user.getOutputStream().writeObject(new InviteUserResponse(ToServerMessage.SUCCESS, msg));
        } catch (IOException e) {
            System.out.printf("Failed to send InviteUserResponse to user %s.\n", user.getEmail());
        }
    }
}
