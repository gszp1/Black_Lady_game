package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;
import messages.toServer.ToServerMessage;

import java.io.IOException;

@Getter
public class InviteUserResponse extends ToClientMessage {

    private final String result;

    private final String description;

    public InviteUserResponse(String result, String description) {
        super(MessageType.InviteUserResponse, String.format("%s|%s", result, description));
        this.result = result;
        this.description = description;
    }

    @Override
    public boolean handle() throws IOException {
        return false;
    }

    public boolean isSuccess() {
        return result.equals(ToServerMessage.SUCCESS);
    }
}
