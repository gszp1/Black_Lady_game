package messages.toServer.requests;

import messages.MessageType;
import messages.toClient.responses.LogoutResponse;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Class for logout request.
 */
public class LogoutRequest extends ToServerMessage {

    /**
     * Constructor.
     */
    public LogoutRequest() {
        super(MessageType.LogoutRequest, "", null);
    }

    /**
     * Logout request handling procedure.
     * @param userList List of users.
     * @param databaseConnector Connection to database.
     * @param gameDetails Game server details.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        Optional<User> userWrapper = userList.getUserByConnectionId(this.getConnectionId());
        if (userWrapper.isPresent()) {
            User user = userWrapper.get();
            user.close();
            userList.removeUser(user);
            gameDetails.getRooms().stream()
                    .filter(room -> room.getOwner().getUserID().equals(user.getUserID()))
                    .forEach(room -> gameDetails.deleteRoom(room.getId()));
            user.getOutputStream().writeObject(new LogoutResponse("Logged out"));
            return true;
        }
        return false;
    }
}
