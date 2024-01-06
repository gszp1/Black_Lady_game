package messages.requests;

import exceptions.LoginFailureException;
import messages.Message;
import messages.MessageType;
import org.apache.commons.codec.digest.DigestUtils;
import server.DatabaseConnector;
import utils.UserList;
import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;

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
            // User not found in database.
            if (userDatabaseData.isEmpty()) {
                //todo;
            }
            checkPassword(messageContents[1], userDatabaseData.get(2));
            if (userList.getUser(userDatabaseData.get(0)).isPresent()) {

            }

            // Set ClientID on server side to the ClientID stored on server
        } catch (SQLException e) {

        } catch (LoginFailureException l) {

        }
        //Given credentials are correct.
        return true;
    }

    /**
     * Parses data field to String array with email and password.
     * @return - String array with email and password.
     */
    public String [] parseData() {
        return getData().trim().split("\\|");
    }

    private boolean checkPassword(String password, String dbPassword) throws LoginFailureException {
        String hashPassword = DigestUtils.md5Hex(password).toUpperCase();
        return !hashPassword.equals(dbPassword);
    }
}
