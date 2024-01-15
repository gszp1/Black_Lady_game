package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;
import messages.toServer.requests.WriteChatRequest;

import java.io.IOException;

@Getter
public class WriteChatResponse extends ToClientMessage {

    private final String result;
    private final String details;

    public WriteChatResponse(String result, String details) {
        super(MessageType.WriteChatResponse, String.format("%s|%s", result, details));
        this.result = result;
        this.details = details;
    }

    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
