package exceptions;

/**
 * Exception for errors during gameplay.
 */
public class PlayException extends Exception {

    /**
     * Error for card being not present in user's deck.
     */
    public static final String CARD_NOT_IN_USER_DECK = "Card is not in users deck";

    /**
     * Error occurred during play.
     */
    public final static String PLAY_EXCEPTION = "Error occurred during play.";

    /**
     * Error for not enough participant being in room.
     */
    public static final String NOT_ENOUGH_PARTICIPANTS = "Not enough participants in game room";

    /**
     * Error for not existing user.
     */
    public static final String USER_DOES_NOT_EXIST = "User does not exist";

    /**
     * Error cause.
     */
    private final String errorCause;

    /**
     * Constructor, sets cause of error.
     */
    public PlayException(String errorCause) {
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
