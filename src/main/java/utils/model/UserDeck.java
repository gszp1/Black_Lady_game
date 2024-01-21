package utils.model;

import cards.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class UserDeck {

    private List<Card> cards = new ArrayList();

    public UserDeck() {
        this.cards = new ArrayList<>();
    }

    public UserDeck(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card cardToAdd) {
        cards.add(cardToAdd);
    }

    public boolean removeCard(Card cardToRemove) {
        return cards.remove(cardToRemove);
    }

    public Optional<Card> getLast() {
        if (cards.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(cards.get(cards.size() - 1));
    }

    public int getCardsCount() {
        return cards.size();
    }
}
