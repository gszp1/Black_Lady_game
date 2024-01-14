package utils.model;

import cards.Card;

import java.util.ArrayList;
import java.util.List;

import static com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken.Optional;

public class UserDeck {

    private List<Card> cards = new ArrayList();

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
}
