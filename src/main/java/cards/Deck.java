package cards;

import java.util.ArrayList;

/**
 * Class representing cards deck.
 */
public class Deck {

    /**
     * Array list which contains all cards in deck.
     */
    private final ArrayList<Card> cards;


    /**
     * Constructor, creates ArrayList with cards, and adds these cards to it.
     */
    public Deck() {
        cards = new ArrayList<>();
        for (CardType cardType: CardType.values()) {
            for (CardSet cardSet: CardSet.values()) {
                cards.add(new Card(cardSet, cardType));
            }
        }
    }

    /**
     * Get ArrayList containing all cards in the deck.
     * @return - Array list of cards.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }
}
