package messages.toClient.responses;

import messages.MessageType;
import messages.toClient.ToClientMessage;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Class for logout response.
 */
public class LogoutResponse extends ToClientMessage {
    /**
     * Constructor for LogoutResponse.
     *
     * @param data     Information passed with message.
     * @param clientID ID of client.
     */
    public LogoutResponse(String data, String clientID) {
        super(MessageType.LogoutResponse, data);
    }

    /**
     * Logout response message handling procedure.
     * @return result of message handling: true or false.
     * @throws IOException Thrown if something went wrong with server-user connection.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    @Override
    public boolean handle() throws IOException{
        return true;
    }
}
