package fr.ippon.codingdojo.model;

import static fr.ippon.codingdojo.model.enums.Suit.HEARTS;
import static fr.ippon.codingdojo.model.enums.Suit.SPADES;
import static fr.ippon.codingdojo.model.enums.Value.ACE;
import static java.util.Collections.EMPTY_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionThrownBy;

import com.google.common.collect.Lists;
import fr.ippon.codingdojo.model.exception.EmptyDeckException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

class DeckTest {

    @Test
    void shouldHaveRightSize() {
        final List<Card> cards = Lists.newArrayList(new Card(SPADES, ACE), new Card(HEARTS, ACE));
        final Deck myDeck = new Deck(cards);

        assertThat(myDeck.getSize()).isEqualTo(2);
    }

    @Test
    void shouldPickFirstCard() {
        final Card aceOfSpades = new Card(SPADES, ACE);
        final List<Card> cards = Lists.newArrayList(aceOfSpades, new Card(HEARTS, ACE));
        final Deck myDeck = new Deck(cards);

        assertThat(myDeck.pick()).isEqualTo(aceOfSpades);
    }

    @Test
    void shouldPickDifferentCards() {
        final Card aceOfSpades = new Card(SPADES, ACE);
        final List<Card> cards = Lists.newArrayList(aceOfSpades, new Card(HEARTS, ACE));
        final Deck myDeck = new Deck(cards);
        final Card firstPick = myDeck.pick();
        final Card secondPick = myDeck.pick();

        assertThat(firstPick).isNotEqualTo(secondPick);
    }

    @Test
    void shouldFailWhenThereIsNoCardLeftToPick() {
        final Deck myDeck = new Deck(EMPTY_LIST);

        assertThatExceptionThrownBy(myDeck::pick)
            .isInstanceOf(EmptyDeckException.class);
    }

    @Test
    void shouldRemoveCardFromDeckAfterPick() {
        final List<Card> cards = Lists.newArrayList(new Card(SPADES, ACE), new Card(HEARTS, ACE));
        final Deck myDeck = new Deck(cards);
        myDeck.pick();

        assertThat(myDeck.getSize()).isEqualTo(1);
    }

    @Test
    void shouldPeekFirstCard() {
        final Card aceOfSpades = new Card(SPADES, ACE);
        final List<Card> cards = Lists.newArrayList(aceOfSpades, new Card(HEARTS, ACE));
        final Deck myDeck = new Deck(cards);

        assertThat(myDeck.peek()).isEqualTo(aceOfSpades);
    }

    @Test
    void shouldPeekSameCard() {
        final Card aceOfSpades = new Card(SPADES, ACE);
        final List<Card> cards = Lists.newArrayList(aceOfSpades, new Card(HEARTS, ACE));
        final Deck myDeck = new Deck(cards);
        final Card firstPeek = myDeck.peek();
        final Card secondPeek = myDeck.peek();

        assertThat(firstPeek).isEqualTo(secondPeek);
    }

    @Test
    void shouldFailWhenThereIsNoCardLeftToPeek() {
        final Deck myDeck = new Deck(EMPTY_LIST);

        assertThatExceptionThrownBy(myDeck::peek)
            .isInstanceOf(EmptyDeckException.class);
    }

    @Test
    void shouldNotRemoveCardFromDeckAfterPeek() {
        final List<Card> cards = Lists.newArrayList(new Card(SPADES, ACE), new Card(HEARTS, ACE));
        final Deck myDeck = new Deck(cards);
        myDeck.peek();

        assertThat(myDeck.getSize()).isEqualTo(2);
    }

    @Test
    void shouldShuffle() throws NoSuchFieldException, IllegalAccessException {
        final List<Card> cards = Lists.newArrayList(new Card(SPADES, ACE), new Card(HEARTS, ACE));
        final Deck myDeck = new Deck(cards);

        // Very simple statistical analysis of shuffling : test that a deck has been re-ordered at least once
        // after 10 shuffles (1à is completely arbitrary)
        // This test may fail 1 time out 2^10 tries, with 2 cards.

        final HashSet<Integer> hashCodes = new HashSet<>();
        for (int i=0; i<10; i++) {
            myDeck.shuffle();
            hashCodes.add(calculateHash(myDeck));
        }
        assertThat(hashCodes.size()).isGreaterThan(1);
    }

    private int calculateHash(Deck deck) throws NoSuchFieldException, IllegalAccessException {
        // FIXME : Black Magic. The following code is only used for test. There is no need to expose the deck's "cards"
        // in the runtime program, so I'm using reflection here for testing purpose only. How would you do otherwise ?
        final Field cardsField = Deck.class.getDeclaredField("cards");
        cardsField.setAccessible(true);
        final List<Card> cards = (List<Card>) cardsField.get(deck);
        cardsField.setAccessible(false);

        return cards.stream()
            .map(Card::hashCode)
            .reduce((a, b) -> a - b) // subtraction is not commutative
            .orElse(0);
    }
}
