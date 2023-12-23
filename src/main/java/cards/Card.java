package cards;

/**
 * Class representing a game card.
 */
public abstract class Card {
    /**
     * To which set (Spades, Hearts, Diamonds, Clubs) does this card belong.
     */
    private final CardSet cardSet;

    /**
     * Provides information about card type and it's rank.
     */
    private final CardType cardType;

    Card(CardSet cardSet, CardType cardType) {
        this.cardSet = cardSet;
        this.cardType = cardType;
    }

    public CardSet getCardSet() {
        return cardSet;
    }

    public CardType getCardType() {
        return cardType;
    }
}
