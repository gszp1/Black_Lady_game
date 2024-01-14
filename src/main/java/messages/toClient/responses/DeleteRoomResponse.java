package messages.toClient.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;

@Getter
@Setter
public class DeleteRoomResponse extends ToClientMessage {

    private final String result;

    private final String details;

    public DeleteRoomResponse(String result, String details) {
        super(MessageType.DeleteRoomResponse, String.format("%s|%s", result, details));
        this.result = result;
        this.details = details;
    }

    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
