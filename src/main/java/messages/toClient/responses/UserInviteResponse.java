package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;
import messages.toServer.ToServerMessage;
import server.DatabaseConnector;
import utils.UserList;

import java.io.IOException;
import java.sql.SQLException;

@Getter
public class UserInviteResponse extends ToClientMessage {

    private final String data;

    private final String userId;

    public UserInviteResponse(String data, String userID) {
        super(MessageType.InviteUserResponse, data);
        this.data = data;
        this.userId = userID;
    }

    /**
     * ToServerMessage handling procedure, behaviour defined by type of message.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     */
    public boolean handle() throws IOException{
        return true;
    }
}
