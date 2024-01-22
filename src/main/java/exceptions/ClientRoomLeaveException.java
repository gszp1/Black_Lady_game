package exceptions;

/**
 * Exception for errors during leaving game rooms.
 */
public class ClientRoomLeaveException extends Exception {

    /**
     * Error message if user can't leave the room.
     */
    public static final String LEAVE_FAIL = "Failed to leave room.";

    /**
     * Error message if user is not in the room.
     */
    public static final String USER_NOT_IN_ROOM = "User is not in this room.";

    /**
     * Cause of error.
     */
    private final String errorCause;

    /**
     * Constructor, sets error cause.
     * @param errorCause Cause of error.
     */
    public ClientRoomLeaveException(String errorCause) {
        this.errorCause = errorCause;
    }

    /**
     * Getter for error cause.
     * @return Error cause.
     */
    public String getErrorCause() {
        return errorCause;
    }
}
