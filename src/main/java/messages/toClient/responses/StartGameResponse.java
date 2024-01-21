package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;

@Getter
public class StartGameResponse extends ToClientMessage {

    private final String result;

    private final String description;

    public StartGameResponse(String result, String description) {
        super(MessageType.StartGameResponse, String.format("%s|%s", result, description));
        this.result = result;
        this.description = description;
    }

    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
