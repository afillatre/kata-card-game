package fr.ippon.codingdojo.model;

import fr.ippon.codingdojo.model.exception.EmptyDeckException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Deck {

    private final List<Card> cards;

    public int getSize() {
        return cards.size();
    }

    public Card pick() {
        if (getSize() == 0) {
            throw new EmptyDeckException();
        }

        final Iterator<Card> iterator = cards.iterator();
        final Card peekedCard = iterator.next();
        if (peekedCard != null) {
            iterator.remove();
        }
        return peekedCard;
    }

    public Card peek() {
        if (getSize() == 0) {
            throw new EmptyDeckException();
        }

        return cards.iterator().next();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void add(Card card) {
        cards.add(card);
    }
}
