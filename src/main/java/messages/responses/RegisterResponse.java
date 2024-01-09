package messages.responses;

import messages.Message;
import messages.MessageType;
import server.DatabaseConnector;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;

//todo: adapt to needs when implementing register

/**
 * Class for register response message.
 */
public class RegisterResponse extends Message {

    /**
     * Constructor for register response.
     * @param data - Information contained by message.
     * @param userID - ID of user to whom the message is addressed.
     */
    public RegisterResponse(String data, String userID) {
        super(MessageType.RegisterResponse, data, userID);
    }

    /**
     * Register response message handling procedure.
     * @param userList List of users.
     * @param databaseConnector Connection to database.
     * @return result of message handling: true or false.
     * @throws IOException Thrown if something went wrong with server-user connection.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    @Override
    public boolean handleMessage(UserList userList, DatabaseConnector databaseConnector) throws IOException {
        return false;
    }
}
