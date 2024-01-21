package messages.toClient.responses;

import messages.MessageType;
import messages.toClient.ToClientMessage;
import messages.toServer.ToServerMessage;
import messages.toServer.requests.LoginRequest;
import server.DatabaseConnector;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;

//todo: adapt to needs when implementing login

/**
 * Class for login response message.
 */
public class LoginResponse extends ToClientMessage {

    private final String result;

    private final String additionalData;

    /**
     * Constructor for login response.
     */
    public LoginResponse(String result, String additionalData) {
        super(MessageType.LoginResponse, String.format("%s|%s", result, additionalData));
        this.result = result;
        this.additionalData = additionalData;
    }

    /**
     * Login response handling procedure.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    @Override
    public boolean handle() throws IOException {
        return true;
    }

    public boolean isSuccess() {
        return this.result.equals(ToServerMessage.SUCCESS);
    }

    public String getResult() {
        return result;
    }

    public String getAdditionalData() {
        return additionalData;
    }
}
