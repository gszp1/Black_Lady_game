package messages.responses;

import messages.Message;
import messages.MessageType;

import java.io.IOException;

//todo: adapt to needs when implementing login

/**
 * Class for login response message.
 */
public class LoginResponse extends Message {

    /**
     * Constructor for login response.
     * @param data - Information contained by message.
     * @param userID - ID of user to whom the message is addressed.
     */
    public LoginResponse(String data, int userID) {
        super(MessageType.LoginResponse, data, userID);
    }

    /**
     * Login response handling procedure.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     */
    @Override
    public boolean handleMessage() throws IOException {
        return false;
    }
}
