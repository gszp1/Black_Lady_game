package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;

/**
 * Response to write chat request.
 */
@Getter
public class WriteChatResponse extends ToClientMessage {

    /**
     * Result of requested operation.
     */
    private final String result;

    /**
     * Response details.
     */
    private final String details;

    /**
     * Constructor, sets result and details.
     * @param result Operation result.
     * @param details Operation details.
     */
    public WriteChatResponse(String result, String details) {
        super(MessageType.WriteChatResponse, String.format("%s|%s", result, details));
        this.result = result;
        this.details = details;
    }

    /**
     * Default handling procedure.
     * @return false;
     * @throws IOException IOException.
     * */
    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
