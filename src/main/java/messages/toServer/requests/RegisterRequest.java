package messages.toServer.requests;

import exceptions.RegistrationFailureException;
import messages.MessageType;
import messages.toClient.responses.RegisterResponse;
import messages.toServer.ToServerMessage;
import org.apache.commons.codec.digest.DigestUtils;
import server.DatabaseConnector;
import utils.GameDetails;
import utils.User;
import utils.UserList;
import utils.Utils;
import utils.model.UserData;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Class for register request message.
 */
public class RegisterRequest extends ToServerMessage {

    /**
     * Success response.
     */
    public static String REGISTRATION_SUCCESS_RESPONSE = "Registration successful.";

    /**
     * User's email.
     */
    private final String email;

    /**
     * User's username.
     */
    private final String username;

    /**
     * User's password.
     */
    private final String password;

    /**
     * Constructor for RegisterRequest.
     * @param email New user email.
     * @param username New user username.
     * @param password New user password.
     */
    public RegisterRequest(String email, String username, String password) {
        super(MessageType.RegisterRequest, String.format("%s|%s|%s", email, username, password), null);
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * Register request handling procedure.
     * @param userList List of users.
     * @param databaseConnector Connection to database.
     * @param gameDetails Game server details.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    @Override
    public boolean handle(UserList userList, DatabaseConnector databaseConnector, GameDetails gameDetails) throws IOException, SQLException {
        System.out.println("handling");
        try {
            validateCredentials();
            Optional<UserData> userData = databaseConnector.getUserByEmail(email);
            if (userData.isPresent()) {
                throw new RegistrationFailureException(RegistrationFailureException.USER_EXISTS);
            }
            String hashPassword = DigestUtils.md5Hex(password).toUpperCase();
            if (databaseConnector.insertUserIntoDatabase(email, username, hashPassword) == 0) {
                throw new RegistrationFailureException(RegistrationFailureException.REGISTRATION_FAIL);
            }
            System.out.printf("Registration of user %s %s finished with success", email, username);
            sendResponse(SUCCESS, REGISTRATION_SUCCESS_RESPONSE, userList);
        } catch (RegistrationFailureException e) {
            System.out.println("User is present");
            sendResponse(FAILURE, e.getExceptionReason(), userList);
        }
        return true;
    }

    /**
     * Validates given email.
     * @param email Email to be validated.
     * @return true if email is correct, otherwise false.
     */
    private boolean validateEmail(String email) {
        Matcher matcher = Utils.pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Validates credentials stored in message content.
     * @throws RegistrationFailureException Thrown if credentials are invalid.
     */
    private void validateCredentials() throws RegistrationFailureException {
        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            throw new RegistrationFailureException(RegistrationFailureException.EMPTY_FIELDS);
        }
        if (!validateEmail(email)) {
            throw new RegistrationFailureException(RegistrationFailureException.INCORRECT_EMAIL);
        }
    }

    /**
     * Sends response to user who sent this message.
     * @param result String stating result of operation: Success or Failure.
     * @param responseContent ToServerMessage to be sent together with result.
     * @param userList List of users.
     * @throws IOException Exception thrown if failed to send the message.
     */
    private void sendResponse(String result, String responseContent, UserList userList) throws IOException {
        Optional<User> user = userList.getUserByConnectionId(this.getConnectionId());
        if (user.isPresent()) {
            System.out.printf("Sending to %s", user.get().getConnectionID());
            user.get().getOutputStream().writeObject(new RegisterResponse(result, responseContent));
        }
    }
}
