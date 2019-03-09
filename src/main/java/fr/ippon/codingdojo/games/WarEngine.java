package fr.ippon.codingdojo.games;

import fr.ippon.codingdojo.model.Card;
import fr.ippon.codingdojo.model.Deck;
import fr.ippon.codingdojo.model.exception.EmptyDeckException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WarEngine {

    private final Deck initialDeck;
    private final List<Deck> playerDecks;
    private final int cardsCount;

    public WarEngine(Deck initialDeck) {
        this.initialDeck = initialDeck;
        this.cardsCount = initialDeck.getSize();
        this.playerDecks = new ArrayList<>(2);
    }

    public void distribute() {
        initialDeck.shuffle();
        final int initialDeckSize = initialDeck.getSize();
        for (int i=0; i<initialDeckSize; i++) {
            addCartToDeck(i % 2, initialDeck.pick());
        }
        log.info("Starting Game...");
    }

    private void addCartToDeck(int playerNumber, Card card) {
        Deck playerDeck;
        try {
            playerDeck = playerDecks.get(playerNumber);
        } catch (IndexOutOfBoundsException e) {
            playerDeck = new Deck(new ArrayList<>());
            playerDecks.add(playerNumber, playerDeck);
        }
        playerDeck.add(card);
    }

    public int play() {
        while (hasAnyoneWon()) {
            final List<Card> playedCards = new ArrayList<>();
            final int handWinner = getHandWinner(playedCards);
            log.info("Player " + (handWinner+1) + " wins the hand");
            playedCards.forEach(card -> playerDecks.get(handWinner).add(card));
        }

        final int gameWinner = playerDecks.get(0).getSize() > 0 ? 0 : 1;
        log.info("Player " + (gameWinner+1) + " wins the game!");
        return gameWinner;
    }

    private int getHandWinner(List<Card> playedCards) {
        try {
            return getWinner(playedCards);
        } catch (EmptyDeckException e) {
            final int handWinner = Math.abs(playedCards.size() % 2 - 1);

            if (!playedCards.isEmpty() && handWinner == 0) {
                log.info("Hand: " + playedCards.get(playedCards.size()-1) + " Vs -");
            }
            return handWinner;
        }
    }

    private int getWinner(List<Card> currentHand) {
        playCards(currentHand);

        log.info("Hand: " + currentHand.get(currentHand.size()-2) + " Vs " + currentHand.get(currentHand.size()-1));

        final int stackSize = currentHand.size();
        final Card player1Card = currentHand.get(stackSize - 2);
        final Card player2Card = currentHand.get(stackSize - 1);
        final int roundWinner;

        if (player1Card.isGreaterThan(player2Card)) {
            roundWinner = 0;
        } else if (player2Card.isGreaterThan(player1Card)) {
            roundWinner = 1;
        } else {
            // War
            log.info("War!");
            playCards(currentHand); // Hidden cards
            log.info("Hand: [hidden] Vs [hidden]");
            roundWinner = getWinner(currentHand);
        }
        return roundWinner;
    }

    private boolean hasAnyoneWon() {
        return playerDecks.get(0).getSize() > 0 && playerDecks.get(0).getSize() != cardsCount;
    }

    private void playCards(List<Card> currentHand) {
        currentHand.add(playerDecks.get(0).pick());
        currentHand.add(playerDecks.get(1).pick());
    }
}
