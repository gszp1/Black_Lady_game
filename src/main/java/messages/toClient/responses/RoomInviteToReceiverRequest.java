package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;

@Getter
public class RoomInviteToReceiverRequest extends ToClientMessage {

    private final String receiverEmail;

    private final String senderEmail;

    private final int roomId;

    public RoomInviteToReceiverRequest(String receiverEmail, String senderEmail, int roomId) {
        super(MessageType.RoomInviteToReceiverRequest, String.format("%s|%s", senderEmail, roomId));
        this.receiverEmail = receiverEmail;
        this.senderEmail = senderEmail;
        this.roomId = roomId;
    }

    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
