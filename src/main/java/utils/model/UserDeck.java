package utils.model;

import cards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Player's deck of cards.
 */
public class UserDeck {

    /**
     * List of cards.
     */
    private List<Card> cards;

    /**
     * Constructor, creates list for cards.
     */
    public UserDeck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Constructor, sets list with reference to given list.
     * @param cards Reference to cards list.
     */
    public UserDeck(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Getter for cards.
     * @return Cards list.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Setter for cards list.
     * @param cards Cards list.
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * Adds card to set.
     * @param cardToAdd Card to be added.
     */
    public void addCard(Card cardToAdd) {
        cards.add(cardToAdd);
    }

    /**
     * Removes card from set.
     * @param cardToRemove Card to be removed from set.
     * @return Boolean.
     */
    public boolean removeCard(Card cardToRemove) {
        return cards.remove(cardToRemove);
    }

    /**
     * Returns last card in deck.
     * @return Last card in deck.
     */
    public Optional<Card> getLast() {
        if (cards.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(cards.get(cards.size() - 1));
    }

    /**
     * Returns number of cards in deck.
     * @return Cards deck size.
     */
    public int getCardsCount() {
        return cards.size();
    }
}
