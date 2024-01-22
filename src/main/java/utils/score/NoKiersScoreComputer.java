package utils.score;

import cards.Card;
import cards.CardSet;
import utils.model.Play;

import java.util.HashMap;
import java.util.Map;

/**
 * Score computer for NoKiers game.
 */

public class NoKiersScoreComputer extends ScoreComputer {

    /**
     * Constructor.
     * @param play Current game.
     */
    public NoKiersScoreComputer(Play play) {
        super(play);
    }

    /**
     * Gets score for round.
     * @param cardsOnTable Cards put on table.
     * @param firstCard First card in trick.
     * @param playOutId ID of turn.
     * @return Map binding userID with score.
     */
    @Override
    public Map<String, Integer> computeSinglePlayOut(Map<String, Card> cardsOnTable, Card firstCard, int playOutId) {
        Map<String, Integer> scores = new HashMap<>();
        cardsOnTable.forEach((userId, card) -> {
            scores.put(userId, getScore(userId, card, cardsOnTable, firstCard));
        });
        return scores;
    }

    /**
     * Gets score for given card.
     * @param userId ID of user.
     * @param card Card.
     * @param cardsOnTable Carts on table.
     * @param firstCard First card of trick.
     * @return Score.
     */
    private int getScore(String userId, Card card, Map<String, Card> cardsOnTable, Card firstCard) {
        if (!isTrickPicker(userId, cardsOnTable, firstCard)) {
            return 0;
        }
        return card.getCardSet().equals(CardSet.Hearts) ? -20 : 0;
    }
}
