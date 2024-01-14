package exceptions;

public class ClientRoomJoinException extends Exception {

    public static final String ROOM_FULL = "Room is full.";

    public static final String PLAYER_ALREADY_JOINED = "Player has already joined the room.";

    private final String errorCause;

    public ClientRoomJoinException(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
