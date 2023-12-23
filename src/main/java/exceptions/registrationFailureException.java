package exceptions;

public class registrationFailureException extends Exception{
    private final String exceptionReason;

    public registrationFailureException(String exceptionReason) {
        this.exceptionReason = exceptionReason;
    }

    public String getExceptionReason() {
        return exceptionReason;
    }

}
