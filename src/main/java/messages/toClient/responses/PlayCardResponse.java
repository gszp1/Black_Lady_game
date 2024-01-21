package messages.toClient.responses;

import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;

public class PlayCardResponse extends ToClientMessage {

    private final String result;

    private final String description;

    public PlayCardResponse(String result, String description) {
        super(MessageType.PlayCardResponse, String.format("%s|%s", result, description));
        this.result = result;
        this.description = description;
    }

    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
