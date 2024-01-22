package utils.score;

import cards.Card;
import cards.CardSet;
import cards.CardType;
import utils.model.Play;

import java.util.HashMap;
import java.util.Map;

/**
 * Score computer for NoHeartsKiers game.
 */
public class NoHeartsKingScoreComputer extends ScoreComputer {

    /**
     * Constructor.
     * @param play Current game.
     */
    public NoHeartsKingScoreComputer(Play play) {
        super(play);
    }

    /**
     * Gets score for round.
     * @param cardsOnTable Cards put on table.
     * @param firstCard First card in trick.
     * @param playOutId ID of turn.
     * @return
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
        return card.getCardType().equals(CardType.KingCard) && card.getCardSet().equals(CardSet.Hearts) ? -150 : 0;
    }
}
