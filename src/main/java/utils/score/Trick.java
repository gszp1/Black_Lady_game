package utils.score;

import cards.Card;
import lombok.Data;

@Data
public class Trick {
    private Card first;
    private Card second;
    private Card third;
    private Card fourth;
}
