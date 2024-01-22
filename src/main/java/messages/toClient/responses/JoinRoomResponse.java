package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;
import messages.toServer.ToServerMessage;

import java.io.IOException;

/**
 * Response to join room request.
 */
@Getter
public class JoinRoomResponse extends ToClientMessage {

    /**
     * Requested action result.
     */
    private final String result;

    /**
     * Details of response.
     */
    private final String details;

    /**
     * Constructor sets result and details.
     * @param result Requested action result.
     * @param details Details of response.
     */
    public JoinRoomResponse(String result, String details) {
        super(MessageType.DeleteRoomResponse, String.format("%s|%s", result, details));
        this.result = result;
        this.details = details;
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

    /**
     * Checks if requested action was successful;.
     * @return Boolean stating action result.
     */
    public boolean isSuccess() {
        return result.equals(ToServerMessage.SUCCESS);
    }
}
