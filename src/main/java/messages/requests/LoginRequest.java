package messages.requests;

import messages.Message;
import messages.MessageType;
import server.DatabaseConnector;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class for login request message.
 */
public class LoginRequest extends Message {
    /**
     * Constructor for login request message.
     * @param email - User's email
     * @param password - User's password.
     * @param clientID - Client's ID.
     */
    public LoginRequest(String email, String password, String clientID) {
        super(MessageType.LoginRequest, String.format("%s|%s", email, password), clientID);
    }

    /**
     * Login request handling procedure.
     * @return - Returns boolean describing result of message handling.
     * @throws IOException - Exception thrown if something went wrong with sending message.
     */
    @Override
    public boolean handleMessage(UserList userList, DatabaseConnector databaseConnector) throws IOException {
        String [] messageContents = parseData();
        try {
            ArrayList<String> userDatabaseData = databaseConnector.getUserFromDatabase(messageContents[0]);
            if (userDatabaseData == null) {

            }

        } catch (SQLException e) {

        }
        return true;
    }

    /**
     * Parses data field to String array with email and password.
     * @return - String array with email and password.
     */
    public String [] parseData() {
        return getData().trim().split("\\|");
    }
}
