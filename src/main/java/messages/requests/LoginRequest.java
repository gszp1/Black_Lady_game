package messages.requests;

import messages.Message;
import messages.MessageType;

import java.io.IOException;

public class LoginRequest extends Message {

    public LoginRequest(MessageType messageType, String email, String password, int clientID) {
        super(messageType, String.format("%s|%s", email, password), clientID);
    }

    @Override
    public boolean handleMessage() throws IOException {
        return false;
    }

    public String [] parseData() {
        return getData().split("\\|");
    }
}
