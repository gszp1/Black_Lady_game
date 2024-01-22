package utils.score;

import cards.Card;
import cards.CardType;
import utils.model.Play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Score computer for NoMan game.
 */
public class NoManScoreComputer extends ScoreComputer {

    /**
     * Constructor.
     * @param play Current game.
     */
    public NoManScoreComputer(Play play) {
        super(play);
    }

    /**
     * Gets score for round.
     * @param cardsOnTable Cards put on table.
     * @param firstCard First card in trick.
     * @param playOutId ID of turn.
     * @return scores.
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
        return isCardTypeMan(card) ? -30 : 0;
    }

    /**
     * Checks if card is King or Knaves card.
     * @param card Checked card.
     * @return Boolean.
     */
    private boolean isCardTypeMan(Card card) {
        List<CardType> manCardTypes = new ArrayList<>();
        manCardTypes.add(CardType.KingCard);
        manCardTypes.add(CardType.KnavesCard);
        return manCardTypes.contains(card.getCardType());
    }
}
