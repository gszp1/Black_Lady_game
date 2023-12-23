package exceptions;

public class loginFailureException extends Exception {
    private final String exceptionReason;

    public loginFailureException(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }

    public String getExceptionReason() {
        return exceptionReason;
    }
}
