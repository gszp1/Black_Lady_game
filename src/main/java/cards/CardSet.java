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
     * Name of card type.
     */
    private final String typeName;

    /**
     * Setter for type's name.
     * @param typeName Type's name.
     */
    CardSet(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Getter for type's name.
     * @return Type's name.
     */
    public String getTypeName() {
        return typeName;
    }
}
