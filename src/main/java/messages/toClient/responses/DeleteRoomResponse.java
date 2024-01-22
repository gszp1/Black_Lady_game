package messages.toClient.responses;

import lombok.Getter;
import lombok.Setter;
import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;

/**
 * Response to delete room request.
 */
@Getter
@Setter
public class DeleteRoomResponse extends ToClientMessage {

    /**
     * Requested task result;
     */
    private final String result;

    /**
     * Response details.
     */
    private final String details;

    /**
     * Constructor, sets result, details and contents.
     * @param result Result of requested operation.
     * @param details Details of response.
     */
    public DeleteRoomResponse(String result, String details) {
        super(MessageType.DeleteRoomResponse, String.format("%s|%s", result, details));
        this.result = result;
        this.details = details;
    }

    /**
     * Default procedure.
     * @return Boolean.
     * @throws IOException Thrown upon connection error.
     */
    @Override
    public boolean handle() throws IOException {
        return false;
    }
}
