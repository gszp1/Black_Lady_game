package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.dto.RoomDetails;
import messages.toClient.ToClientMessage;

import java.io.IOException;

@Getter
public class RoomDetailsResponse extends ToClientMessage {

    private final RoomDetails roomDetails;

    public RoomDetailsResponse(RoomDetails roomDetails) {
        super(MessageType.RoomDetailsResponse, "Room details");
        this.roomDetails = roomDetails;
    }
    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
