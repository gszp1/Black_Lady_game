package messages.responses;

import messages.Message;
import messages.MessageType;
import server.DatabaseConnector;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;

public class LogoutResponse extends Message {
    /**
     * Constructor for LogoutResponse.
     *
     * @param messageType - Type of message.
     * @param data        - Information passed with message.
     * @param clientID    - ID of client.
     */
    public LogoutResponse(MessageType messageType, String data, String clientID) {
        super(messageType, data, clientID);
    }

    @Override
    public boolean handleMessage(UserList userList, DatabaseConnector databaseConnector) throws IOException, SQLException {
        return false;
    }
}
