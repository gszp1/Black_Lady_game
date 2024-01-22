package messages.toClient.responses;

import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;

/**
 * Response to play card request.
 */
public class PlayCardResponse extends ToClientMessage {

    /**
     * Requested action result.
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
    public PlayCardResponse(String result, String description) {
        super(MessageType.PlayCardResponse, String.format("%s|%s", result, description));
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
