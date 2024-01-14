package exceptions;

public class RequestFailureException extends Exception {

    public static final String MESSAGE_RECEIVER_NOT_FOUND = "Failed to find user with such credentials.";

    private final String errorCause;

    public RequestFailureException(String errorCause) {
        this.errorCause = errorCause;
    }
}
