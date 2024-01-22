package utils.score;

import cards.Card;
import exceptions.PlayException;
import utils.model.Play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Class for score calculation.
 */
public abstract class ScoreComputer {

    /**
     * Current tour.
     */
    private final Play play;

    /**
     * Constructor, sets play.
     * @param play Tour.
     */
    public ScoreComputer(Play play) {
        this.play = play;
    }

    public Map<String, Integer> computeScores() throws PlayException {
        assertCardsPutLengthEqual();
        final int playOutCount = new ArrayList<>(play.getCardsPut().values()).get(0).getCardsCount();
        System.out.printf("Computing score for %s\n", playOutCount);
        final Map<String, Integer> scores = getInitialScores();
        for (int i = 0; i < playOutCount; i++) {
            adjustScores(scores, computeSinglePlayOut(getCardsOnTable(i), getFirstCard(i), i));
        }
        return scores;
    }

    private void adjustScores(
            Map<String, Integer> base,
            Map<String, Integer> playOutScore
    ) {
        playOutScore.forEach((userId, score) -> {
            if (!base.containsKey(userId)) {
                base.put(userId, 0);
            }
            base.put(userId, base.get(userId) + score);
        });
    }

    private Map<String, Integer> getInitialScores() {
        return play.getUserIds().stream()
                .collect(Collectors.toMap(Function.identity(), (userId) -> 0));
    }

    private Map<String, Card> getCardsOnTable(int i) {
        Map<String, Card> cardsOnTable = new HashMap<>();
        play.getUserIds().forEach((userId) -> {
            cardsOnTable.put(userId, play.getCardsPut().get(userId).getCards().get(i));
        });
        return cardsOnTable;
    }

    private Card getFirstCard(int i) {
        return play.getFirstCards().get(i);
    }

    public abstract Map<String, Integer> computeSinglePlayOut(Map<String, Card> cardsOnTable, Card firstCard, int playOutId);

    private void assertCardsPutLengthEqual() throws PlayException {
        boolean areCardDeckSizesEqual = play.getCardsPut().values().stream()
                .map((userDeck) -> userDeck.getCards().size())
                .distinct()
                .count() == 1;
        if (!areCardDeckSizesEqual) {
            throw new PlayException("Called compute score on not yet finished round");
        }
    }

    protected boolean isTrickPicker(String userId, Map<String, Card> cardsOnTable, Card firstCard) {
        return userId.equals(PlayUtils.getTrickPicker(cardsOnTable, firstCard));
    }
}
