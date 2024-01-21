package exceptions;

public class ClientRoomLeaveException extends Exception {

    public static final String LEAVE_FAIL = "Failed to leave room.";

    public static final String USER_NOT_IN_ROOM = "User is not in this room.";

    private final String errorCause;

    public ClientRoomLeaveException(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
