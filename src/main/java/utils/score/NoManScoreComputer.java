package utils.score;

import cards.Card;
import cards.CardSet;
import cards.CardType;
import utils.model.Play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoManScoreComputer extends ScoreComputer {

    public NoManScoreComputer(Play play) {
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
        return isCardTypeMan(card) ? -30 : 0;
    }

    private boolean isCardTypeMan(Card card) {
        List<CardType> manCardTypes = new ArrayList<>();
        manCardTypes.add(CardType.KingCard);
        manCardTypes.add(CardType.KnavesCard);
        return manCardTypes.contains(card.getCardType());
    }
}
