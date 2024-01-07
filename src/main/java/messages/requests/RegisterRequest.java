package messages.requests;

import messages.Message;
import messages.MessageType;
import server.DatabaseConnector;
import utils.UserList;
import utils.Utils;

import java.io.IOException;
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

}
