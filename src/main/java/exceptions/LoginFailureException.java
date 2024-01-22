package exceptions;

/**
 * Exception for handling failed login procedure.
 * Provides information about the reason of failure in form of String.
 */
public class LoginFailureException extends Exception {

    /**
     * Error for trying to log into not existing account.
     */
    public static String INVALID_CREDENTIALS = "Users with given credentials doesn't exist.";

    /**
     * Error for invalid email.
     */
    public static String INCORRECT_EMAIL = "Provided Email is incorrect.";

    /**
     * Error for fields being empty.
     */
    public static String EMPTY_FIELDS = "Not all login data provided.";

    /**
     * Error for user being already logged in.
     */
    public static String USER_ALREADY_LOGGED_IN = "User is already logged in.";

    /**
     * Cause of error.
     */
    private final String exceptionReason;

    /**
     * Constructor, sets error cause.
     * @param exceptionReason Error cause.
     */
    public LoginFailureException(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }

    /**
     * Getter for error cause.
     * @return Error cause.
     */
    public String getExceptionReason() {
        return exceptionReason;
    }
}
