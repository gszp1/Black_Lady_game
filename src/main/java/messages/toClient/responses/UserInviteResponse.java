package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;

/**
 * Response for user invitation request.
 */
@Getter
public class UserInviteResponse extends ToClientMessage {

    /**
     * Response data.
     */
    private final String data;

    /**
     * User's ID.
     */
    private final String userId;

    /**
     * Constructor, sets user's ID and data.
     * @param data
     * @param userID
     */
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
