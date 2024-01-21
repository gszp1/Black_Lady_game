package exceptions;

/**
 * Exception for request failure.
 */
public class RequestFailureException extends Exception {

    /**
     * Error for message receiver being not found.
     */
    public static final String MESSAGE_RECEIVER_NOT_FOUND = "Failed to find user with such credentials.";

    /**
     * Cause of error.
     */
    private final String errorCause;

    /**
     * Constructor, sets error cause.
     * @param errorCause
     */
    public RequestFailureException(String errorCause) {
        this.errorCause = errorCause;
    }
}
