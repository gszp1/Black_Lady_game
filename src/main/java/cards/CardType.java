package cards;

/**
 * Enumerate for each type of cards in classic card deck.
 */
public enum CardType {
    TwoCard(1, "TwoCard"),

    ThreeCard(2, "ThreeCard"),

    FourCard(3, "FourCard"),

    FiveCard(4, "FiveCard"),

    SixCard(5, "SixCard"),

    SevenCard(6, "SevenCard"),

    EightCard(7, "EightCard"),

    NineCard(8, "NineCard"),

    TenCard(9, "TenCard"),

    KnavesCard(10, "KnavesCard"),

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

    CardType(int rank, String cardTypeName) {
        this.rank = rank;
        this.cardTypeName = cardTypeName;
    }

    public String getCardTypeName() {
        return cardTypeName;
    }

    public int getRank() {
        return rank;
    }
}
