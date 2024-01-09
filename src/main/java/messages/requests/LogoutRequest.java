package messages.requests;

import messages.Message;
import messages.MessageType;
import server.DatabaseConnector;
import utils.User;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class LogoutRequest extends Message {

    /**
     * Constructor LogoutRequest.
     *
     * @param messageType - Type of message.
     * @param data        - Information passed with message.
     * @param clientID    - ID of client.
     */
    public LogoutRequest(MessageType messageType, String data, String clientID) {
        super(messageType, data, clientID);
    }

    @Override
    public boolean handleMessage(UserList userList, DatabaseConnector databaseConnector) throws IOException, SQLException {
        return true;
    }
}
