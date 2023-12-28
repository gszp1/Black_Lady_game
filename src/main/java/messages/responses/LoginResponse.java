package messages.responses;

import messages.Message;
import messages.MessageType;

import java.io.IOException;

//todo: adapt to needs when implementing login

public class LoginResponse extends Message {

    public LoginResponse(String data, int userID) {
        super(MessageType.LoginResponse, data, userID);
    }
    @Override
    public boolean handleMessage() throws IOException {
        return false;
    }
}
