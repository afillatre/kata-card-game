package fr.ippon.codingdojo.util;

import fr.ippon.codingdojo.model.Card;
import fr.ippon.codingdojo.model.Deck;
import java.lang.reflect.Field;
import java.util.List;

public class TestUtils {

    public static <T,U> T getField(U instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        final Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        final T value = (T) field.get(instance);
        field.setAccessible(false);
        return value;
    }

    public static int calculateHash(Deck deck) throws NoSuchFieldException, IllegalAccessException {
        // FIXME : Black Magic. The following code is only used for test. There is no need to expose the deck's "cards"
        // in the runtime program, so I'm using reflection here for testing purpose only. How would you do otherwise ?
        final List<Card> cards = getField(deck, "cards");

        return cards.stream()
            .map(Card::hashCode)
            .reduce((a, b) -> a - b) // subtraction is not commutative
            .orElse(0);
    }

}
