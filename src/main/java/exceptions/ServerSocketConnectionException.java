package exceptions;

public class ServerSocketConnectionException {

    public static final String UNKNOWN_MESSAGE_TYPE = "Message received from client is of unknown type.";

    public static final String MESSAGE_READING_FAILURE = "Failed to read message from client.";

    private final String exceptionCause;

    public ServerSocketConnectionException(String exceptionCause) {
        this.exceptionCause = exceptionCause;
    }

    public String getExceptionCause() {
        return exceptionCause;
    }

}
