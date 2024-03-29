package exceptions;

/**
 * Exception for client side errors occurring during server-client communication.
 */
public class ClientSocketConnectionException extends Throwable {
    /**
     * Error for application being unable to connect with server.
     */
    public static String SOCKET_CONNECTION_FAILURE = "Failed to establish connection with server.";

    /**
     * Error for failing to open output stream.
     */
    public static String OUTPUT_STREAM_OPENING_FAILURE = "Failed to open output stream.";

    /**
     * Error for failing to open input stream.
     */
    public static String INPUT_STREAM_OPENING_FAILURE = "Failed to open input stream.";

    /**
     * Error for failing to send message to server.
     */
    public static String MESSAGE_SENDING_FAILURE = "Failed to send message to server.";

    /**
     * Error for failing to read message from server.
     */
    public static String MESSAGE_READING_FAILURE = "Failed to read message from server.";

    /**
     * Error for failing to recognize received message.
     */
    public static String UNKNOWN_MESSAGE_TYPE = "ToServerMessage received from server is of unknown type.";

    /**
     * String variable defining error cause.
     */
    private final String errorCause;

    /**
     * Constructor, sets errorCause with String describing what caused the error to happen.
     * @param errorCause - String describing error.
     */
    public ClientSocketConnectionException(String errorCause) {
        this.errorCause = errorCause;
    }

    /**
     * Getter for errorCause.
     * @return - String describing error cause.
     */
    public String getErrorCause() {
        return errorCause;
    }
}
