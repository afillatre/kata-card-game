package fr.ippon.codingdojo.model;

import fr.ippon.codingdojo.model.enums.Suit;
import fr.ippon.codingdojo.model.enums.Value;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
public class Card {

    @Getter
    private final Suit suit;
    @Getter
    private final Value value;

    public boolean isGreaterThan(Card otherCard) {
        return this.getValue().ordinal() > otherCard.getValue().ordinal();
    }

    public boolean isEqualTo(Card otherCard) {
        return getValue() == otherCard.getValue();
    }

    public String toString() {
        final String symbol;
        switch (suit) {
            case HEARTS:
                symbol = "♥";
                break;
            case SPADES:
                symbol = "♠";
                break;
            case DIAMONDS:
                symbol = "♦";
                break;
            default:
                symbol = "♣";
                break;
        }
        return "[" + this.value.getDisplayValue() + symbol + "]";
    }
}
