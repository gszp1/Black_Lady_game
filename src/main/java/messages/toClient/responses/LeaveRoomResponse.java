package messages.toClient.responses;

import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;

public class LeaveRoomResponse extends ToClientMessage {

    private String result;

    private String details;

    public LeaveRoomResponse(String result, String details) {
        super(MessageType.LeaveRoomResponse, String.format("%s|%s", result, details));
        this.result = result;
        this.details = details;
    }

    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
