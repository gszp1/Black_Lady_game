package Cards;

public enum CardSet {
    Spades("Spades"),
    Hearts("Hearts"),
    Diamonds("Diamonds"),
    Clubs("Clubs");

    private final String typeName;

    CardSet(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
