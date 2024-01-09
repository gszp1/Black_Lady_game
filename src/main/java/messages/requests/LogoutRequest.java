package messages.requests;

import messages.Message;
import messages.MessageType;
import server.DatabaseConnector;
import utils.User;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Class for logout request.
 */
public class LogoutRequest extends Message {

    /**
     * Constructor LogoutRequest.
     * @param messageType - Type of message.
     * @param data        - Information passed with message.
     * @param clientID    - ID of client.
     */
    public LogoutRequest(MessageType messageType, String data, String clientID) {
        super(messageType, data, clientID);
    }

    /**
     * Logout request handling procedure.
     * @return - Returns boolean describing result of message handling.
     * @throws IOException - Exception thrown if something went wrong with sending message.
     */
    @Override
    public boolean handleMessage(UserList userList, DatabaseConnector databaseConnector) throws IOException, SQLException {
        Optional<User> userWrapper = userList.getUser(this.getClientID());
        if (userWrapper.isPresent()) {
            User user = userWrapper.get();
            //todo remove user from all game rooms.
            user.close();
            userList.removeUser(user);
        }
        return true;
    }

}
