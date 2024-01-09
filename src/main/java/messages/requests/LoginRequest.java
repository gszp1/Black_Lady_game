package messages.requests;

import exceptions.LoginFailureException;
import messages.Message;
import messages.MessageType;
import messages.responses.LoginResponse;
import org.apache.commons.codec.digest.DigestUtils;
import server.DatabaseConnector;
import utils.User;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Class for login request message.
 */
public class LoginRequest extends Message {

    public static String SUCCESS = "Success";

    public static String FAILURE = "Failure";

    public static String SUCCESS_RESPONSE = "Login successful.";

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
     * @param userList List of users.
     * @param databaseConnector Connection to database.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    @Override
    public boolean handleMessage(UserList userList, DatabaseConnector databaseConnector) throws IOException, SQLException{
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
            user = userList.getUser(this.getClientID());
            if (!updateUserID(userDatabaseData.get(0), userList)) {
                return false;
            }
            if(user.isPresent()) {
                sendResponse(SUCCESS, SUCCESS_RESPONSE, user.get());
            }
            // Set ClientID on server side to the ClientID stored on server
        } catch (LoginFailureException l) {
            Optional<User> user = userList.getUser(this.getClientID());
            if (user.isPresent()) {
                sendResponse(FAILURE, l.getExceptionReason(), user.get());
            }
        }
        //Given credentials are correct.
        return true;
    }

    /**
     * Checks if given password is equal to password stored in database.
     * @param password Password given for validation.
     * @param dbPassword Password retrieved from database.
     * @return true if password is correct, false otherwise.
     */
    private boolean checkPassword(String password, String dbPassword) {
        String hashPassword = DigestUtils.md5Hex(password).toUpperCase();
        return !hashPassword.equals(dbPassword);
    }

    /**
     * Updates userID of user to whom this message was sent.
     * @param newID UserID to be set.
     * @param userList List of users from which will user be taken.
     * @return true if update was successful, false if not.
     */
    private boolean updateUserID(String newID, UserList userList) {
        Optional<User> user = userList.getUser(this.getClientID());
        if(!user.isPresent()) {
            return false;
        }
        user.get().setUserID(newID);
        return true;
    }

    /**
     * Sends response to user who sent this message.
     * @param result String stating result of operation: Success or Failure.
     * @param response Message to be sent together with result.
     * @param user User to whom we send this response.
     * @throws IOException - Exception thrown if failed to send the message.
     */
    private void sendResponse(String result, String response, User user) throws IOException{
        String messageContents = result.concat("|").concat(response);
        Message message = new LoginResponse(messageContents, user.getUserID());
        user.getOutputStream().writeObject(message);
    }


}
