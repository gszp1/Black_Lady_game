package utils.score;

import cards.Card;
import cards.CardSet;
import cards.CardType;
import utils.model.Play;

import java.util.HashMap;
import java.util.Map;

public class NoHeartsKingScoreComputer extends ScoreComputer {

    public NoHeartsKingScoreComputer(Play play) {
        super(play);
    }

    @Override
    public Map<String, Integer> computeSinglePlayOut(Map<String, Card> cardsOnTable, Card firstCard, int playOutId) {
        Map<String, Integer> scores = new HashMap<>();
        cardsOnTable.forEach((userId, card) -> {
            scores.put(userId, getScore(userId, card, cardsOnTable, firstCard));
        });
        return scores;
    }


    private int getScore(String userId, Card card, Map<String, Card> cardsOnTable, Card firstCard) {
        if (!isTrickPicker(userId, cardsOnTable, firstCard)) {
            return 0;
        }
        return card.getCardType().equals(CardType.KingCard) && card.getCardSet().equals(CardSet.Hearts) ? -150 : 0;
    }
}
