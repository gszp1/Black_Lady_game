package utils.score;

import cards.Card;
import utils.model.Play;

import java.util.HashMap;
import java.util.Map;

public class NoTrickScoreComputer extends ScoreComputer {

    public NoTrickScoreComputer(Play play) {
        super(play);
    }

    @Override
    public Map<String, Integer> computeSinglePlayOut(Map<String, Card> cardsOnTable, Card firstCard, int playOutId) {
        final String trickPicker = PlayUtils.getTrickPicker(cardsOnTable, firstCard);
        Map<String, Integer> playOutScores = new HashMap<>();
        cardsOnTable.keySet().forEach((userId) -> playOutScores.put(userId, 0));
        playOutScores.put(trickPicker, -20);
        return playOutScores;
    }
}
