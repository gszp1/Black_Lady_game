package messages.toClient.responses;

import messages.MessageType;
import messages.toClient.ToClientMessage;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;

//todo: adapt to needs when implementing register

/**
 * Class for register response message.
 */
public class RegisterResponse extends ToClientMessage {

    private final String result;

    private final String additionalData;

    /**
     * Constructor for register response.
     */
    public RegisterResponse(String result, String additionalData) {
        super(MessageType.RegisterResponse, String.format("%s|%s", result, additionalData));
        this.result = result;
        this.additionalData = additionalData;
    }

    /**
     * Register response message handling procedure.
     * @return result of message handling: true or false.
     * @throws IOException Thrown if something went wrong with server-user connection.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    @Override
    public boolean handle() throws IOException {
        return false;
    }

    public String getResult() {
        return result;
    }

    public String getAdditionalData() {
        return additionalData;
    }
}
