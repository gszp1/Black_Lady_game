package exceptions;


/**
 * Exception for server side errors occurring during server-client communication.
 */
public class ServerSocketConnectionException  extends Exception{

    public static final String UNKNOWN_MESSAGE_TYPE = "Message received from client is of unknown type.";

    public static final String MESSAGE_READING_FAILURE = "Failed to read message from client.";

    public static final String INPUT_STREAM_OPENING_FAILURE = "Failed to open input stream.";

    /**
     * String depicting cause of exception.
     */
    private final String exceptionCause;

    /**
     * Constructor, sets cause of exception.
     * @param exceptionCause - Cause of exception.
     */
    public ServerSocketConnectionException(String exceptionCause) {
        this.exceptionCause = exceptionCause;
    }

    /**
     * Getter for exceptionCause;
     * @return - String depicting cause of exception.
     */
    public String getExceptionCause() {
        return exceptionCause;
    }

}
