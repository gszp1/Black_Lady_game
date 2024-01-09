package messages.responses;

import messages.Message;
import messages.MessageType;
import server.DatabaseConnector;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Class for logout response.
 */
public class LogoutResponse extends Message {
    /**
     * Constructor for LogoutResponse.
     * @param messageType Type of message.
     * @param data Information passed with message.
     * @param clientID ID of client.
     */
    public LogoutResponse(MessageType messageType, String data, String clientID) {
        super(messageType, data, clientID);
    }

    /**
     * Logout response message handling procedure.
     * @param userList List of users.
     * @param databaseConnector Connection to database.
     * @return result of message handling: true or false.
     * @throws IOException Thrown if something went wrong with server-user connection.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    @Override
    public boolean handleMessage(UserList userList, DatabaseConnector databaseConnector) throws IOException, SQLException {
        return true;
    }
}
