package messages.toClient.responses;

import messages.MessageType;
import messages.toClient.ToClientMessage;
import messages.toServer.ToServerMessage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Class for login response message.
 */
public class LoginResponse extends ToClientMessage {

    /**
     * Requested action result.
     */
    private final String result;

    /**
     * Response details.
     */
    private final String additionalData;

    /**
     * Constructor for login response.
     * @param result Requested action result.
     * @param additionalData Response additional data.
     */
    public LoginResponse(String result, String additionalData) {
        super(MessageType.LoginResponse, String.format("%s|%s", result, additionalData));
        this.result = result;
        this.additionalData = additionalData;
    }

    /**
     * Login response handling procedure.
     * @return - Boolean telling if operation was successful.
     * @throws IOException - Exception thrown if something went wrong with sending the message.
     */
    @Override
    public boolean handle() throws IOException {
        return true;
    }

    /**
     * Checks if requested operation was finished successfully.
     * @return Boolean stating operation result.
     */
    public boolean isSuccess() {
        return this.result.equals(ToServerMessage.SUCCESS);
    }

    /**
     * Getter for result;
     * @return Result.
     */
    public String getResult() {
        return result;
    }

    /**
     * Getter for additional data.
     * @return Additional data.
     */
    public String getAdditionalData() {
        return additionalData;
    }
}
