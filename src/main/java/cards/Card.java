package cards;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Card implements Serializable {
    /**
     * To which set (Spades, Hearts, Diamonds, Clubs) does this card belong.
     */
    private final CardSet cardSet;

    /**
     * Provides information about card type and it's rank.
     */
    private final CardType cardType;
}
