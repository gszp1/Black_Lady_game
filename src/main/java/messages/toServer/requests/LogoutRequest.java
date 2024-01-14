package messages.toServer.requests;

import messages.MessageType;
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
     * Constructor LogoutRequest.
     * @param data        - Information passed with message.
     * @param clientID    - ID of client.
     */
    public LogoutRequest(String data, String clientID) {
        super(MessageType.LogoutRequest, data, clientID);
    }

    /**
     * Logout request handling procedure.
     * @param userList List of users.
     * @param databaseConnector Connection to database.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        Optional<User> userWrapper = userList.getUserByConnectionId(this.getConnectionId());
        if (userWrapper.isPresent()) {
            User user = userWrapper.get();
            //todo remove user from all game rooms.
            user.close();
            userList.removeUser(user);
        }
        return true;
    }

}
