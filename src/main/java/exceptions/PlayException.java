package exceptions;

public class PlayException extends Exception {

    public static final String CARD_NOT_IN_USER_DECK = "Card is not in users deck";

    public final static String PLAY_EXCEPTION = "Error occurred during play.";

    public static final String NOT_ENOUGH_PARTICIPANTS = "Not enough participants in game room";

    public static final String USER_DOES_NOT_EXIST = "User does not exist";

    private final String errorCause;

    public PlayException(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getErrorCause() {
        return errorCause;
    }
}
