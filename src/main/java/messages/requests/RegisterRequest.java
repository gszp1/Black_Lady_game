package messages.requests;

import exceptions.RegistrationFailureException;
import messages.Message;
import messages.MessageType;
import messages.responses.RegisterResponse;
import org.apache.commons.codec.digest.DigestUtils;
import server.DatabaseConnector;
import utils.UserList;
import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;

import utils.User;

/**
 * Class for register request message.
 */
public class RegisterRequest extends Message {

    public static String REGISTRATION_SUCCESS = "Success";

    public static String REGISTRATION_FAILURE = "Failure";

    public static String REGISTRATION_SUCCESS_RESPONSE = "Registration successful.";

    public RegisterRequest(String email, String username, String password,
                           String passwordConfirmation, String clientID) {
        super(MessageType.RegisterRequest, String.format("%s|%s|%s|%s", email, username, password, passwordConfirmation), clientID);
    }

    /**
     * Register request handling procedure.
     * @return - Returns boolean describing result of message handling.
     * @throws IOException - Exception thrown if something went wrong with sending message.
     */
    @Override
    public boolean handleMessage(UserList userList, DatabaseConnector databaseConnector) throws IOException, SQLException {
        try {
            String [] credentials = parseData();
            validateCredentials(credentials);
            ArrayList<String> dbData = databaseConnector.getUserFromDatabase(credentials[0]);
            if (dbData != null) {
                throw new RegistrationFailureException(RegistrationFailureException.USER_EXISTS);
            }
            String hashPassword = DigestUtils.md5Hex(credentials[2]).toUpperCase();
            if (databaseConnector.insertUserIntoDatabase(credentials[0], credentials[1], hashPassword) == 0) {
                throw new RegistrationFailureException(RegistrationFailureException.REGISTRATION_FAIL);
            }
            sendResponse(REGISTRATION_SUCCESS, REGISTRATION_SUCCESS_RESPONSE, userList);
        } catch (RegistrationFailureException e) {
            sendResponse(REGISTRATION_FAILURE, e.getExceptionReason(), userList);
        }
        return true;
    }

    private boolean validateEmail(String email) {
        Matcher matcher = Utils.pattern.matcher(email);
        return matcher.matches();
    }

    private void validateCredentials(String [] credentials) throws RegistrationFailureException {
        if (credentials[0].isEmpty() || credentials[1].isEmpty() || credentials[2].isEmpty() || credentials[3].isEmpty()) {
            throw new RegistrationFailureException(RegistrationFailureException.EMPTY_FIELDS);
        }
        if (!validateEmail(credentials[0])) {
            throw new RegistrationFailureException(RegistrationFailureException.INCORRECT_EMAIL);
        }
        if (!credentials[2].equals(credentials[3])) {
            throw new RegistrationFailureException(RegistrationFailureException.PASSWORDS_NOT_EQUAL);
        }
    }

    private void sendResponse(String result, String responseContent, UserList userList) throws IOException {
        Optional<User> user = userList.getUser(this.getClientID());
        if (user.isPresent()) {
            String responseMessage = result.concat("|").concat(responseContent);
            user.get().getOutputStream().writeObject(new RegisterResponse(responseMessage, this.getClientID()));
        }
    }

}
