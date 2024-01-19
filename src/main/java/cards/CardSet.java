package cards;

import java.io.Serializable;

/**
 * Enumerate for sets to which a card may belong.
 */
public enum CardSet implements Serializable {
    Spades("Spades"),
    Hearts("Hearts"),
    Diamonds("Diamonds"),
    Clubs("Clubs");

    /**
     * Name of set.
     */
    private final String typeName;

    // Constructor, sets card's set.
    CardSet(String typeName) {
        this.typeName = typeName;
    }

    // Getter for set name.
    public String getTypeName() {
        return typeName;
    }
}
