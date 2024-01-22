package utils.score;

import cards.Card;
import lombok.Data;

/**
 * Class for trick of cards.
 */
@Data
public class Trick {
    /**
     * First picked card.
     */
    private Card first;

    /**
     * Second picked card.
     */
    private Card second;

    /**
     * Third picked card.
     */
    private Card third;

    /**
     * Fourth picked card.
     */
    private Card fourth;
}
