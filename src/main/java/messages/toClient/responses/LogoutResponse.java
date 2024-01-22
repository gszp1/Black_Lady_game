package messages.toClient.responses;

import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Class for logout response.
 */
public class LogoutResponse extends ToClientMessage {

    /**
     * Constructor, sets data.
     * @param data Additional data.
     */
    public LogoutResponse(String data) {
        super(MessageType.LogoutResponse, data);
    }

    /**
     * Logout response message handling procedure.
     * @return result of message handling: true or false.
     * @throws IOException Thrown if something went wrong with server-user connection.
     */
    @Override
    public boolean handle() throws IOException{
        return true;
    }
}
