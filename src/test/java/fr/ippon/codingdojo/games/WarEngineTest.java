package fr.ippon.codingdojo.games;

import static fr.ippon.codingdojo.model.enums.Suit.DIAMONDS;
import static fr.ippon.codingdojo.model.enums.Suit.HEARTS;
import static fr.ippon.codingdojo.model.enums.Suit.SPADES;
import static fr.ippon.codingdojo.model.enums.Value.*;
import static fr.ippon.codingdojo.util.TestUtils.calculateHash;
import static fr.ippon.codingdojo.util.TestUtils.getField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import com.google.common.collect.Lists;
import fr.ippon.codingdojo.model.Card;
import fr.ippon.codingdojo.model.Deck;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WarEngineTest {

    private static final String PLAYER_DECKS = "playerDecks";

    @Test
    void shouldCreateDeckForEachPlayer() throws NoSuchFieldException, IllegalAccessException {
        final List<Card> cards = Lists.newArrayList(
            new Card(SPADES, ACE), new Card(SPADES, TWO), new Card(SPADES, THREE),
            new Card(HEARTS, ACE), new Card(HEARTS, TWO), new Card(HEARTS, THREE));
        final WarEngine warEngine = new WarEngine(new Deck(cards));

        warEngine.distribute();

        final List<Deck> playerDecks = getField(warEngine, PLAYER_DECKS);
        assertThat(playerDecks).hasSize(2);
    }

    @Test
    void shouldHaveSameNumberOfCards() throws NoSuchFieldException, IllegalAccessException {
        final List<Card> cards = Lists.newArrayList(
            new Card(SPADES, ACE), new Card(SPADES, TWO), new Card(SPADES, THREE),
            new Card(HEARTS, ACE), new Card(HEARTS, TWO), new Card(HEARTS, THREE));
        final WarEngine warEngine = new WarEngine(new Deck(cards));

        warEngine.distribute();

        final List<Deck> playerDecks = getField(warEngine, PLAYER_DECKS);
        playerDecks.forEach(deck -> assertThat(deck.getSize()).isEqualTo(3));

    }

    @Test
    void shouldHaveAtMostOneMoreCardForFirstPlayers() throws NoSuchFieldException, IllegalAccessException {
        final List<Card> cards = Lists.newArrayList(
            new Card(SPADES, ACE), new Card(SPADES, TWO), new Card(SPADES, THREE),
            new Card(HEARTS, ACE), new Card(HEARTS, TWO), new Card(HEARTS, THREE),
            new Card(DIAMONDS, ACE));
        final WarEngine warEngine = new WarEngine(new Deck(cards));

        warEngine.distribute();

        final List<Deck> playerDecks = getField(warEngine, PLAYER_DECKS);
        playerDecks.forEach(deck -> assertThat(deck.getSize()).isBetween(3, 4));

    }

    @Test
    void shouldShuffleCards() throws NoSuchFieldException, IllegalAccessException {
        // Very simple statistical analysis of shuffling : test that a deck has been re-ordered at least once
        // after 10 shuffles (10 is completely arbitrary)
        // This test may fail 1 time out 2^10 tries, with 2 cards.

        final Set<Integer> hashCodes = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            final List<Card> cards = Lists.newArrayList(
                new Card(SPADES, ACE), new Card(SPADES, TWO), new Card(SPADES, THREE),
                new Card(HEARTS, ACE), new Card(HEARTS, TWO), new Card(HEARTS, THREE),
                new Card(DIAMONDS, ACE), new Card(DIAMONDS, TWO));
            final WarEngine warEngine = new WarEngine(new Deck(cards));

            warEngine.distribute();

            final List<Deck> playerDecks = getField(warEngine, PLAYER_DECKS);
            hashCodes.add(calculateHash(playerDecks.get(0)));
        }

        assertThat(hashCodes.size()).isGreaterThan(1);
    }

    @Test
    void shouldWin() {
        final List<Card> cards = Lists.newArrayList(new Card(SPADES, TWO), new Card(SPADES, THREE));
        final Deck warDeck = spy(new Deck(cards));
        final WarEngine warEngine = new WarEngine(warDeck);
        // Prevents shuffling
        doNothing().when(warDeck).shuffle();
        warEngine.distribute();

        final int winner = warEngine.play();

        assertThat(winner).isEqualTo(1);
    }

    @Test
    void shouldGetLooserCards() throws NoSuchFieldException, IllegalAccessException {
        final List<Card> cards = Lists.newArrayList(new Card(SPADES, TWO), new Card(SPADES, THREE));
        final Deck warDeck = spy(new Deck(cards));
        final WarEngine warEngine = new WarEngine(warDeck);
        // Prevents shuffling
        doNothing().when(warDeck).shuffle();
        warEngine.distribute();

        warEngine.play();

        final List<Deck> finalDecks = getField(warEngine, PLAYER_DECKS);
        assertThat(finalDecks.get(1).getSize()).isEqualTo(2);
    }

    @Test
    void shouldPlayMultipleTurns() throws NoSuchFieldException, IllegalAccessException {
        final List<Card> cards = Lists
            .newArrayList(new Card(SPADES, TWO), new Card(SPADES, THREE), new Card(SPADES, FOUR));
        final Deck warDeck = spy(new Deck(cards));
        final WarEngine warEngine = new WarEngine(warDeck);
        // Prevents shuffling
        doNothing().when(warDeck).shuffle();
        warEngine.distribute();

        final int winner = warEngine.play();

        final List<Deck> finalDecks = getField(warEngine, PLAYER_DECKS);
        assertThat(finalDecks.get(0).getSize()).isEqualTo(3);
        assertThat(winner).isEqualTo(0);
    }

    @Test
    void shouldHandleWar() {
        final List<Card> cards = Lists.newArrayList(
            new Card(HEARTS, THREE), new Card(SPADES, THREE), new Card(SPADES, KING),
            new Card(HEARTS, TWO), new Card(SPADES, TWO), new Card(HEARTS, FOUR)
        );
        final Deck warDeck = spy(new Deck(cards));
        final WarEngine warEngine = new WarEngine(warDeck);
        // Prevents shuffling
        doNothing().when(warDeck).shuffle();
        warEngine.distribute();

        final int winner = warEngine.play();

        // Without war handling, the player with the highest card in the deck always wins
        assertThat(winner).isEqualTo(1);
    }

    @Test
    void shouldWinHandIfOtherPlayerHasNotEnoughCards() {
        final List<Card> cards = Lists.newArrayList(
            new Card(HEARTS, THREE), new Card(SPADES, THREE), new Card(HEARTS, TWO),
            new Card(SPADES, KING), new Card(SPADES, TWO)
        );
        final Deck warDeck = spy(new Deck(cards));
        final WarEngine warEngine = new WarEngine(warDeck);
        // Prevents shuffling
        doNothing().when(warDeck).shuffle();
        warEngine.distribute();

        final int winner = warEngine.play();

        assertThat(winner).isEqualTo(0);
    }
}