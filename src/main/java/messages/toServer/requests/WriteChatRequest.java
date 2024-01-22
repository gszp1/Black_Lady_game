package messages.toServer.requests;

import exceptions.PlayException;
import messages.MessageType;
import messages.toClient.responses.WriteChatResponse;
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
 * Request for writing message to chat.
 */
public class WriteChatRequest extends ToServerMessage {

    /**
     * Room's ID.
     */
    private final int roomId;

    /**
     * Message.
     */
    private final String message;

    /**
     * Constructor.
     * @param roomId Room's ID.
     * @param message Message.
     */
    public WriteChatRequest(int roomId, String message) {
        super(MessageType.WriteChatRequest, message, null);
        this.roomId = roomId;
        this.message = message;
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
            System.out.printf("User with connection id %s does not exist.\n", getConnectionId());
            return false;
        }
        Optional<Room> room = gameDetails.getRoomById(roomId);
        if (!room.isPresent()) {
            sendError(user.get(), String.format("Room with id %s does not exist", roomId));
            return false;
        }
        if (!writeToChat(room.get(), user.get(), message)){
            return false;
        }
        broadcastRoomDetails(userList, gameDetails);
        return true;
    }

    /**
     * Write message to chat.
     * @param room Room.
     * @param user User.
     * @param msg Message.
     * @return Boolean.
     */
    private boolean writeToChat(Room room, User user, String msg) {
        try {
            room.writeToChat(user.getEmail(), msg);
            return true;
        } catch (PlayException e) {
            sendError(user, msg);
            return false;
        }
    }

    /**
     * Sends success message.
     * @param user User.
     * @param msg Message.
     */
    private void sendSuccess(User user, String msg) {
        send(user, msg, true);
    }

    /**
     * Sends error message.
     * @param user User.
     * @param msg Message.
     */
    private void sendError(User user, String msg) {
        send(user, msg, false);
    }

    /**
     * Send response.
     * @param user User.
     * @param msg Message.
     * @param isSuccess Message writing success.
     */
    private void send(User user, String msg, boolean isSuccess) {
        try {
            user.getOutputStream().writeObject(new WriteChatResponse(
                    isSuccess ? ToServerMessage.SUCCESS : ToServerMessage.FAILURE,
                    msg
            ));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("S: Failed to send WriteChatResponse to %s\n", user.getEmail());
        }
    }
}
