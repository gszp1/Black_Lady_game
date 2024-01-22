package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;

/**
 * Response to start game request.
 */
@Getter
public class StartGameResponse extends ToClientMessage {

    /**
     * Result of requested operation.
     */
    private final String result;

    /**
     * Response description.
     */
    private final String description;

    /**
     * Constructor, sets result and description.
     * @param result Requested action result.
     * @param description Response description.
     */
    public StartGameResponse(String result, String description) {
        super(MessageType.StartGameResponse, String.format("%s|%s", result, description));
        this.result = result;
        this.description = description;
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
