package messages.requests;

import exceptions.LoginFailureException;
import exceptions.RegistrationFailureException;
import messages.Message;
import messages.MessageType;
import server.DatabaseConnector;
import utils.UserList;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Class for register request message.
 */
public class RegisterRequest extends Message {


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
    public boolean handleMessage(UserList userList, DatabaseConnector databaseConnector) throws IOException {
        try {
            validateCredentials();
        } catch (RegistrationFailureException e) {

        }
        return true;
    }

    /**
     * Method for parsing message data into: email, username, password, passwordConfirmation.
     * @return - String array with registration data.
     */
    public String [] parseData() {
        return getData().trim().split("\\|");
    }

    private boolean validateEmail(String email) {
        Matcher matcher = Utils.pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validateCredentials() throws RegistrationFailureException {
        String [] credentials = this.parseData();
        if (credentials[0].isEmpty() || credentials[1].isEmpty() || credentials[2].isEmpty() || credentials[3].isEmpty()) {
            throw new RegistrationFailureException(RegistrationFailureException.EMPTY_FIELDS);
        }
        if (!validateEmail(credentials[0])) {
            throw new RegistrationFailureException(RegistrationFailureException.INCORRECT_EMAIL);
        }
        if (!credentials[2].equals(credentials[3])) {
            throw new RegistrationFailureException(RegistrationFailureException.PASSWORDS_NOT_EQUAL);
        }
        return true;
    }

}
