package utils;

import cards.Card;
import cards.CardSet;
import cards.CardType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.List;

/**
 * Class for universal code, not belonging specifically to any other class.
 * Contains pattern for email validation and compiled pattern.
 */
public class Utils {
    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isEmailValid(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

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
