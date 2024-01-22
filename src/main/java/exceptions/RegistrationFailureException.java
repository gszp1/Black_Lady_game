package exceptions;

/**
 * Exception for handling failed registration procedure.
 * Provides information about the reason of failure in form of String.
 */
public class RegistrationFailureException extends Exception{
    /**
     * Error for trying to create account that already exists.
     */
    public static String USER_EXISTS = "Users with such credentials already exists.";

    /**
     * Error for unexpected server failure.
     */
    public static String SERVER_FAILURE = "Unexpected server failure.";

    /**
     * Error for email being invalid.
     */
    public static String INCORRECT_EMAIL = "Provided Email is incorrect";

    /**
     * Error for fields being empty.
     */
    public static String EMPTY_FIELDS = "Not all registration data provided.";

    /**
     * Error for refused registration.
     */
    public static String REGISTRATION_FAIL = "Registration refused.";

    /**
     * Cause of error.
     */
    private final String exceptionReason;

    /**
     * Constructor, sets error cause.
     */
    public RegistrationFailureException(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }

    /**
     * Returns error cause.
     */
    public String getExceptionReason() {
        return exceptionReason;
    }

}
