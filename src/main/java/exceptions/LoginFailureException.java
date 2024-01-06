package exceptions;

/**
 * Exception for handling failed login procedure.
 * Provides information about the reason of failure in form of String.
 */
public class LoginFailureException extends Exception {

    public static String INVALID_CREDENTIALS = "Users with given credentials doesn't exist.";

    public static String INCORRECT_EMAIL = "Provided Email is incorrect.";

    public static String EMPTY_FIELDS = "Not all login data provided.";

    public static String USER_ALREADY_LOGGED_IN = "User is already logged in.";

    private final String exceptionReason;

    public LoginFailureException(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }

    public String getExceptionReason() {
        return exceptionReason;
    }
}
