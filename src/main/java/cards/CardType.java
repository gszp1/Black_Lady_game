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
     * Provides information about card's rank.
     */
    private final int rank;

    /**
     * Provides name of given card type.
     */
    private final String cardTypeName;

    /**
     * Constructor, sets type's rank and name.
     * @param rank Type's rank.
     * @param cardTypeName Type's name.
     */
    CardType(int rank, String cardTypeName) {
        this.rank = rank;
        this.cardTypeName = cardTypeName;
    }

    /**
     * Gets type with given rank.
     * @param rank Rank of searched type.
     * @return Type.
     */
    public CardType getCardTypeByRank(int rank) {
        for (CardType cardType: values()) {
            if (cardType.rank == rank) {
                return cardType;
            }
        }
        return null;
    }

    /**
     * Getter for type's name.
     * @return Type name.
     */
    public String getCardTypeName() {
        return cardTypeName;
    }

    /**
     * Getter for type's rank.
     * @return Type rank.
     */
    public int getRank() {
        return rank;
    }
}
