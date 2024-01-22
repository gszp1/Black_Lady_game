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
 * Request for game start.
 */
public class StartGameRequest extends ToServerMessage {

    /**
     * Room's ID.
     */
    private int roomId;

    /**
     * Constructor.
     * @param roomId Room's ID.
     */
    public StartGameRequest(int roomId) {
        super(MessageType.StartGameRequest, String.format("Start game %s", roomId), null);
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
            System.out.printf("User %s does not exist.\n", getConnectionId());
            return false;
        }
        Optional<Room> room = gameDetails.getRoomById(roomId);
        if (!room.isPresent()) {
            sendError(user.get(), String.format("Room %s does not exist", roomId));
            return false;
        }
        if (!startGame(user.get(), room.get())) {
            return false;
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

    /**
     * Start game.
     * @param user User.
     * @param room Room.
     * @return Boolean
     */
    private boolean startGame(User user, Room room) {
        try {
            if (!room.isUserIdOwner(user.getUserID())){
                throw new PlayException(String.format("User %s is not an owner of the room", user.getUserID()));
            }
            room.start();
            return true;
        } catch (PlayException e) {
            sendError(user, e.getErrorCause());
            return false;
        }
    }
}
