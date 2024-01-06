package messages.requests;

import exceptions.LoginFailureException;
import messages.Message;
import messages.MessageType;
import org.apache.commons.codec.digest.DigestUtils;
import server.DatabaseConnector;
import utils.User;
import utils.UserList;
import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
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
            // Check if user exists.
            if (userDatabaseData == null) {
                throw new LoginFailureException(LoginFailureException.INVALID_CREDENTIALS);
            }
            // Check password.
            if (!checkPassword(messageContents[1], userDatabaseData.get(2))) {
                throw new LoginFailureException(LoginFailureException.INVALID_CREDENTIALS);
            }
            // Check if such user is already logged in.
            Optional<User> user = userList.getUser(userDatabaseData.get(1));
            if (user.isPresent()) {
                throw new LoginFailureException(LoginFailureException.USER_ALREADY_LOGGED_IN);
            }
            if (!updateUserID(userDatabaseData.get(0), userList)) {
                return false;
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

    private boolean updateUserID(String newID, UserList userList) {
        Optional<User> user = userList.getUser(this.getClientID());
        if(!user.isPresent()) {
            return false;
        }
        user.get().setUserID(newID);
        return true;
    }


}
