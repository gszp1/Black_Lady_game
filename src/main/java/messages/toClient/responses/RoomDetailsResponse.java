package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.dto.RoomDetails;
import messages.toClient.ToClientMessage;

import java.io.IOException;

/**
 * Response to request for room details.
 */
@Getter
public class RoomDetailsResponse extends ToClientMessage {

    /**
     * Room details.
     */
    private final RoomDetails roomDetails;

    /**
     * Constructor, sets roomDetails.
     * @param roomDetails Room details.
     */
    public RoomDetailsResponse(RoomDetails roomDetails) {
        super(MessageType.RoomDetailsResponse, "Room details");
        this.roomDetails = roomDetails;
    }

    /**
     * Default handling procedure.
     * @return false;
     * @throws IOException IOException.
     */
    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
