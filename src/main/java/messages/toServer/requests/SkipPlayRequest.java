package messages.toServer.requests;

import exceptions.PlayException;
import messages.MessageType;
import messages.toClient.responses.StartGameResponse;
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
 * Request for skipping play.
 */
public class SkipPlayRequest extends ToServerMessage {

    /**
     * Room's ID.
     */
    private int roomId;

    /**
     * Constructor.
     * @param roomId Room's ID.
     */
    public SkipPlayRequest(int roomId) {
        super(MessageType.SkipGameRequest, String.format("Skipping play in %s", roomId), null);
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
        Optional<User> loggedInUser = userList.getUserByConnectionId(getConnectionId());
        if (!loggedInUser.isPresent()) {
            return false;
        }
        Optional<Room> room = gameDetails.getRoomById(roomId);
        if (!room.isPresent()) {
            sendError(loggedInUser.get(), String.format("Room %s does not exist", roomId));
            return false;
        }
        if (!room.get().isStarted()) {
            sendError(loggedInUser.get(), "Game not yet started");
            return false;
        }
        try {
            if (!room.get().skipPlay(loggedInUser.get().getUserID())) {
                return false;
            }
        } catch (PlayException e) {
            sendError(loggedInUser.get(), e.getErrorCause());
        }
        broadcastRoomDetails(userList, gameDetails);
        return true;
    }

    /**
     * Method for sending error.
     * @param user Error message receiver.
     * @param message Error message.
     */
    private void sendError(User user, String message) {
        try {
            user.getOutputStream().writeObject(new StartGameResponse(ToServerMessage.FAILURE, message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
