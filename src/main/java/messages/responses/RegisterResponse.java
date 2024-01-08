package messages.responses;

import messages.Message;
import messages.MessageType;
import server.DatabaseConnector;
import utils.UserList;

import java.io.IOException;

//todo: adapt to needs when implementing register

/**
 * Class for register response message.
 */
public class RegisterResponse extends Message {

    /**
     * Constructor for register response.
     * @param data - Information contained by message.
     * @param userID - ID of user to whom the message is addressed.
     */
    public RegisterResponse(String data, String userID) {
        super(MessageType.RegisterResponse, data, userID);
    }

    /**
     * Register response handling procedure.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     */
    @Override
    public boolean handleMessage(UserList userList, DatabaseConnector databaseConnector) throws IOException {
        return false;
    }

    private String [] parseData() {
        return getData().trim().split("\\|");
    }
}
