package utils.score;

import cards.Card;
import exceptions.PlayException;
import utils.model.Play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RobberScoreComputer extends ScoreComputer{

    private List<ScoreComputer> scoreComputers = new ArrayList<>();

    public RobberScoreComputer(Play play) {
        super(play);
        scoreComputers.add(new NoTrickScoreComputer(play));
        scoreComputers.add(new NoKiersScoreComputer(play));
        scoreComputers.add(new NoWomenScoreComputer(play));
        scoreComputers.add(new NoManScoreComputer(play));
        scoreComputers.add(new NoHeartsKingScoreComputer(play));
        scoreComputers.add(new NoSevenLastTrickScoreComputer(play));
    }

    @Override
    public Map<String, Integer> computeSinglePlayOut(Map<String, Card> cardsOnTable, Card firstCard, int playOutId) {
        Map<String, Integer> scores = new HashMap<>();
        scoreComputers.forEach((computer) -> {
            try {
                Map<String, Integer> scoreAdjustment = computer.computeScores();
                scoreAdjustment.forEach((userId, score) -> {
                    if (!scores.containsKey(userId)) {
                        scores.put(userId, 0);
                    }
                    scores.put(userId, scores.get(userId) + scoreAdjustment.get(userId));
                });
            } catch (PlayException e) {
                System.out.println("Error when computing score!");
            }
        });
        return scores;
    }
}
