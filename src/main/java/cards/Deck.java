package cards;

import java.util.ArrayList;

public class Deck {

    private final ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        for (CardType cardType: CardType.values()) {
            for (CardSet cardSet: CardSet.values()) {
                cards.add(new Card(cardSet, cardType));
            }
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
