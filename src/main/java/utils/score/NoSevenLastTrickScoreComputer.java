package utils.score;

import cards.Card;
import cards.CardSet;
import utils.model.Play;

import java.util.HashMap;
import java.util.Map;

public class NoSevenLastTrickScoreComputer extends ScoreComputer{
    public NoSevenLastTrickScoreComputer(Play play) {
        super(play);
    }

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
