package cards;

import java.io.Serializable;

/**
 * Enumerate for each type of cards in classic card deck.
 */
public enum CardType implements Serializable {
    TwoCard(1, "TwoCard"),

    ThreeCard(2, "ThreeCard"),

    FourCard(3, "FourCard"),

    FiveCard(4, "FiveCard"),

    SixCard(5, "SixCard"),

    SevenCard(6, "SevenCard"),

    EightCard(7, "EightCard"),

    NineCard(8, "NineCard"),

    TenCard(9, "TenCard"),

    KnavesCard(10, "JackCard"),

    QueenCard(11, "QueenCard"),

    KingCard(12, "KingCard"),

    AceCard(13, "AceCard");

    /**
     * Provides information about type's rank.
     */
    private final int rank;

    /**
     * Provides name of given type.
     */
    private final String cardTypeName;

    /**
     * Constructor, creates type with given rank and name.
     * @param rank Rank of card 1 - 13.
     * @param cardTypeName Name of card's type.
     */
    CardType(int rank, String cardTypeName) {
        this.rank = rank;
        this.cardTypeName = cardTypeName;
    }

    // Returns name of type with given rank.
    public CardType getCardTypeByRank(int rank) {
        for (CardType cardType: values()) {
            if (cardType.rank == rank) {
                return cardType;
            }
        }
        return null;
    }

    // Getter for type name.
    public String getCardTypeName() {
        return cardTypeName;
    }

    // Getter for rank of given type.
    public int getRank() {
        return rank;
    }
}
