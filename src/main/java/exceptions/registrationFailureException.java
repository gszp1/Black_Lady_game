package exceptions;

/**
 * Exception for handling failed registration procedure.
 * Provides information about the reason of failure in form of String.
 */
public class registrationFailureException extends Exception{

    public static String USER_EXISTS = "Users with such credentials already exists.";

    public static String SERVER_FAILURE = "Unexpected server failure.";

    public static String INCORRECT_EMAIL = "Provided Email is incorrect";

    public static String PASSWORDS_NOT_EQUAL = "Provided passwords are not equal.";

    public static String EMPTY_FIELDS = "Not all registration data provided.";

    private final String exceptionReason;

    public registrationFailureException(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }

    public String getExceptionReason() {
        return exceptionReason;
    }

}
