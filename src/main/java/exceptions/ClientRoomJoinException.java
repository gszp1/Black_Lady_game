package exceptions;

/**
 * Exception for errors during joining game rooms.
 */
public class ClientRoomJoinException extends Exception {

    /**
     * Error message for full room.
     */
    public static final String ROOM_FULL = "Room is full.";
    /**
     * Error message for user already being in room.
     */
    public static final String PLAYER_ALREADY_JOINED = "Player has already joined the room.";

    /**
     * String for cause of error.
     */
    private final String errorCause;

    /**
     * Constructor, sets error cause.
     * @param errorCause Cause of error.
     */
    public ClientRoomJoinException(String errorCause) {
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
