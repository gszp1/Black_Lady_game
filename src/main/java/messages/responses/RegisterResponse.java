package messages.responses;

import messages.Message;
import messages.MessageType;

import java.io.IOException;

//todo: adapt to needs when implementing register

public class RegisterResponse extends Message {

    public RegisterResponse(String data, int userID) {
        super(MessageType.RegisterResponse, data, userID);
    }

    @Override
    public boolean handleMessage() throws IOException {
        return false;
    }
}
