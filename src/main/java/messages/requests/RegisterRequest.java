package messages.requests;

import messages.Message;
import messages.MessageType;

import java.io.IOException;

public class RegisterRequest extends Message {


    public RegisterRequest(MessageType messageType, String email, String username, String password,
                           String passwordConfirmation, int clientID) {
        super(messageType, String.format("%s|%s|%s|%s", email, username, password, passwordConfirmation), clientID);
    }

    @Override
    public boolean handleMessage() throws IOException {
        return false;
    }

    public String [] parseData() {
        return getData().trim().split("\\|");
    }
}
