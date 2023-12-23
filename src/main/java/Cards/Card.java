package Cards;

public abstract class Card {

    private final CardSet cardSet;

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
