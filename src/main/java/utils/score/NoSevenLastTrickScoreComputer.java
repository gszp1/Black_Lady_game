package utils.score;

import cards.Card;
import utils.model.Play;

import java.util.HashMap;
import java.util.Map;

/**
 * Score computer for NoSevenLastTick game.
 */
public class NoSevenLastTrickScoreComputer extends ScoreComputer{

    /**
     * Constructor.
     * @param play Current game.
     */
    public NoSevenLastTrickScoreComputer(Play play) {
        super(play);
    }

    /**
     * Gets score for round.
     * @param cardsOnTable Cards put on table.
     * @param firstCard First card in trick.
     * @param playOutId ID of turn.
     * @return scores
     */
    @Override
    public Map<String, Integer> computeSinglePlayOut(Map<String, Card> cardsOnTable, Card firstCard, int playOutId) {
        final String trickPicker = PlayUtils.getTrickPicker(cardsOnTable, firstCard);
        Map<String, Integer> scores = new HashMap<>();
        cardsOnTable.forEach((userId, card) -> {
            scores.put(userId, userId.equals(trickPicker) && playOutId == 7 || playOutId == 13 ? -75 : 0);
        });
        return scores;
    }
}
