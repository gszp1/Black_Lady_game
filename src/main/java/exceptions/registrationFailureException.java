package exceptions;

public class registrationFailureException extends Exception{

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
