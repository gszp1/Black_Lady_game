package messages.toClient.responses;

import lombok.Getter;
import messages.MessageType;
import messages.toClient.ToClientMessage;
import messages.toServer.ToServerMessage;

import java.io.IOException;

/**
 * Response to InviteUserRequest.
 */
@Getter
public class InviteUserResponse extends ToClientMessage {

    /**
     * Request handling result.
     */
    private final String result;

    /**
     * Response description.
     */
    private final String description;

    /**
     * Constructor, sets result and description.
     * @param result Request handling result.
     * @param description Response description.
     */
    public InviteUserResponse(String result, String description) {
        super(MessageType.InviteUserResponse, String.format("%s|%s", result, description));
        this.result = result;
        this.description = description;
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

    /**
     * Checks if request handling resulted in success.
     * @return Response handling result.
     */
    public boolean isSuccess() {
        return result.equals(ToServerMessage.SUCCESS);
    }
}
