package messages.toClient.responses;

import messages.MessageType;
import messages.toClient.ToClientMessage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Class for register response message.
 */
public class RegisterResponse extends ToClientMessage {

    /**
     * Requested action result.
     */
    private final String result;

    /**
     * Response additional data.
     */
    private final String additionalData;

    /**
     * Constructor for register response.
     */
    public RegisterResponse(String result, String additionalData) {
        super(MessageType.RegisterResponse, String.format("%s|%s", result, additionalData));
        this.result = result;
        this.additionalData = additionalData;
    }

    /**
     * Register response message handling procedure.
     * @return result of message handling: true or false.
     * @throws IOException Thrown if something went wrong with server-user connection.
     * @throws SQLException Thrown if something went wrong with database connection.
     */
    @Override
    public boolean handle() throws IOException {
        return false;
    }

    /**
     * Gets requested action result.
     * @return Result.
     */
    public String getResult() {
        return result;
    }

    /**
     * Getter for additional data.
     * @return additional data.
     */
    public String getAdditionalData() {
        return additionalData;
    }
}
