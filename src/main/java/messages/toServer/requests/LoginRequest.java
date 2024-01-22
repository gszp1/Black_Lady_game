package messages.toServer.requests;

import exceptions.LoginFailureException;
import messages.MessageType;
import messages.toClient.responses.LoginResponse;
import messages.toServer.ToServerMessage;
import org.apache.commons.codec.digest.DigestUtils;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.model.UserData;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Class for login request message.
 */
public class LoginRequest extends ToServerMessage {

    /**
     * Success response contents.
     */
    public static String SUCCESS_RESPONSE = "Login successful.";

    /**
     * User email.
     */
    private final String email;

    /**
     * User password.
     */
    private final String password;

    /**
     * Client's ID.
     */
    private String clientId;

    /**
     * Constructor for login request message.
     * @param email - User's email
     * @param password - User's password.
     * @param clientId - Client's ID.
     */
    public LoginRequest(String email, String password, String clientId) {
        super(MessageType.LoginRequest, String.format("%s|%s", email, password), clientId);
        this.email = email;
        this.password = password;
        this.clientId = clientId;
    }

    /**
     * Login request handling procedure.
     * @param userList List of users.
     * @param databaseConnector Connection to database.
     * @param gameDetails Game server data.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException{
        try {
            System.out.println("Handling login request");
            Optional<UserData> userData = databaseConnector.getUserByEmail(email);
            // Check if user exists.
            if (!userData.isPresent()) {
                throw new LoginFailureException(LoginFailureException.INVALID_CREDENTIALS);
            }
            // Check password.
            String passwordHash = DigestUtils.md5Hex(password).toUpperCase();
            if (!checkPassword(passwordHash, userData.get().getPassword())) {
                throw new LoginFailureException(LoginFailureException.INVALID_CREDENTIALS);
            }
            // Check if such user is already logged in.
            Optional<User> user = userList.getUserByUserID(userData.get().getUserId());
            if (user.isPresent()) {
                System.out.printf("User %s is present\n", user.get().getUserID());
                throw new LoginFailureException(LoginFailureException.USER_ALREADY_LOGGED_IN);
            }
            Optional<User> thisUser = userList.getUserByConnectionId(this.getConnectionId());
            if (!thisUser.isPresent()) {
                return false; // user who wanted to log in turned off application
            }
            System.out.println("User connected!");
            thisUser.get().updateUserData(userData.get());
            sendResponse(SUCCESS, SUCCESS_RESPONSE, thisUser.get());
            broadcastRoomDetails(userList, gameDetails);
            // Set ClientID on server side to the ClientID stored on server
        } catch (LoginFailureException l) {
            Optional<User> user = userList.getUserByConnectionId(getConnectionId());
            if (user.isPresent()) {
                sendResponse(FAILURE, l.getExceptionReason(), user.get());
            }
        }
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
     * Sends response to user who sent this message.
     * @param result String stating result of operation: Success or Failure.
     * @param response ToServerMessage to be sent together with result.
     * @param user User to whom we send this response.
     * @throws IOException - Exception thrown if failed to send the message.
     */
    private void sendResponse(String result, String response, User user) throws IOException{
        user.getOutputStream().writeObject(new LoginResponse(result, response));
    }
}
