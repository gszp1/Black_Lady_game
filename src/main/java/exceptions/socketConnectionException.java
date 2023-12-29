package exceptions;

public class socketConnectionException {

    public static String SOCKET_CONNECTION_FAILURE = "Failed to establish connection with server.";

    public static String STREAM_OPENING_FAILURE = "Failed to open output stream.";

    public static String MESSAGE_SENDING_FAILURE = "Failed to send message to server.";

    private final String errorCause;

    public socketConnectionException(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
