package exceptions;

public class SocketConnectionException extends Throwable {

    public static String SOCKET_CONNECTION_FAILURE = "Failed to establish connection with server.";

    public static String OUTPUT_STREAM_OPENING_FAILURE = "Failed to open output stream.";

    public static String INPUT_STREAM_OPENING_FAILURE = "Failed to open input stream.";

    public static String MESSAGE_SENDING_FAILURE = "Failed to send message to server.";

    public static String MESSAGE_READING_FAILURE = "Failed to read message from server.";

    public static String UNKNOWN_MESSAGE_TYPE = "Message received from server is of unknown type.";

    private final String errorCause;

    public SocketConnectionException(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
