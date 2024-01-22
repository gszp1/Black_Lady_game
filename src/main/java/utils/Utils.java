package utils;

import cards.Card;
import cards.CardSet;
import cards.CardType;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.List;

/**
 * Class for universal code, not belonging specifically to any other class.
 * Contains pattern for email validation and compiled pattern.
 */
public class Utils {

    /**
     * Regular expression for validating emails.
     */
    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Pattern for validating emails.
     */
    public static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * Method for validating email.
     * @param email Checked email.
     * @return Boolean.
     */
    public static boolean isEmailValid(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    /**
     * Generates full deck of 52 cards.
     * @return Deck of cards.
     */
    public static List<Card> getFullDeck() {
        List<Card> cards = new ArrayList<>();
        for (CardSet cardSet : CardSet.values()) {
            for (CardType cardType : CardType.values()) {
                cards.add(new Card(cardSet, cardType));
            }
        }
        return cards;
    }
}
