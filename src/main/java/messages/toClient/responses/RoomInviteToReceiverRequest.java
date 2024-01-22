package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;

/**
 * Invitation request to client.
 */
@Getter
public class RoomInviteToReceiverRequest extends ToClientMessage {

    /**
     * Email of invitation receiver.
     */
    private final String receiverEmail;

    /**
     * Email of invitation sender.
     */
    private final String senderEmail;

    /**
     * ID of room.
     */
    private final int roomId;

    /**
     * Constructor, sets receiver email, sender email and room id.
     * @param receiverEmail Email of invitation receiver.
     * @param senderEmail Email of invitation sender.
     * @param roomId ID of room.
     */
    public RoomInviteToReceiverRequest(String receiverEmail, String senderEmail, int roomId) {
        super(MessageType.RoomInviteToReceiverRequest, String.format("%s|%s", senderEmail, roomId));
        this.receiverEmail = receiverEmail;
        this.senderEmail = senderEmail;
        this.roomId = roomId;
    }

    /**
     * Default handling procedure.
     * @return false.
     * @throws IOException IOException.
     */
    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
