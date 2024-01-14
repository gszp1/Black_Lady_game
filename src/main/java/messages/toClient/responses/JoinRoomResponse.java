package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;
import messages.toServer.ToServerMessage;

import java.io.IOException;

@Getter
public class JoinRoomResponse extends ToClientMessage {

    private final String result;

    private final String details;

    public JoinRoomResponse(String result, String details) {
        super(MessageType.DeleteRoomResponse, String.format("%s|%s", result, details));
        this.result = result;
        this.details = details;
    }

    @Override
    public boolean handle() throws IOException {
        return false;
    }

    public boolean isSuccess() {
        return result.equals(ToServerMessage.SUCCESS);
    }
}
