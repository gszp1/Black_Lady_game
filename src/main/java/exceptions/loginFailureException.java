package exceptions;

public class loginFailureException extends Exception {

    public static String INCORRECT_EMAIL = "Provided Email is incorrect.";

    public static String EMPTY_FIELDS = "Not all login data provided.";

    private final String exceptionReason;

    public loginFailureException(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }

    public String getExceptionReason() {
        return exceptionReason;
    }
}
