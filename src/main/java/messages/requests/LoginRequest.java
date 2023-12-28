package messages.requests;

import messages.Message;
import messages.MessageType;

import java.io.IOException;

/**
 * Class for login request message.
 */
public class LoginRequest extends Message {
    /**
     * Constructor for login request message.
     * @param email - User's email
     * @param username - User's username.
     * @param password - User's password.
     * @param clientID - Client's ID.
     */
    public LoginRequest(String email, String username, String password, int clientID) {
        super(MessageType.LoginRequest, String.format("%s|%s|%S", email, username, password), clientID);
    }

    /**
     * Login Request handling procedure, behaviour defined by type of message.
     * @return - Returns boolean describing result of message handling.
     * @throws IOException - Exception thrown if something went wrong with sending message.
     */
    @Override
    public boolean handleMessage() throws IOException {
        return false;
    }

    /**
     * Parses data field to String array with email and password.
     * @return - String array with email and password.
     */
    public String [] parseData() {
        return getData().trim().split("\\|");
    }
}
