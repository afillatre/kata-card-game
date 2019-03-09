package fr.ippon.codingdojo.model;

import static fr.ippon.codingdojo.model.enums.Suit.HEARTS;
import static fr.ippon.codingdojo.model.enums.Suit.SPADES;
import static fr.ippon.codingdojo.model.enums.Value.ACE;
import static fr.ippon.codingdojo.model.enums.Value.TWO;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    void shouldHaveRightProperties() {
        final Card aceOfHearts = new Card(HEARTS, ACE);

        assertThat(aceOfHearts.getSuit()).isEqualTo(HEARTS);
        assertThat(aceOfHearts.getValue()).isEqualTo(ACE);
    }

    @Test
    void shouldBeEqual() {
        final Card aceOfHearts = new Card(HEARTS, ACE);
        final Card anotherAceOfHearts = new Card(HEARTS, ACE);

        assertThat(aceOfHearts).isEqualTo(anotherAceOfHearts);
    }

    @Test
    void shouldBeDifferent() {
        final Card aceOfHearts = new Card(HEARTS, ACE);
        final Card twoOfHearts = new Card(HEARTS, TWO);
        final Card twoOfSpades = new Card(SPADES, TWO);

        assertThat(aceOfHearts).isNotEqualTo(twoOfHearts);
        assertThat(aceOfHearts).isNotEqualTo(twoOfSpades);
        assertThat(twoOfHearts).isNotEqualTo(twoOfSpades);
    }

    @Test
    void shouldBeGreater() {
        final Card aceOfHearts = new Card(HEARTS, ACE);
        final Card twoOfHearts = new Card(HEARTS, TWO);

        assertThat(twoOfHearts.isGreaterThan(aceOfHearts));
    }

    @Test
    void shouldBeEqualValue() {
        final Card aceOfHearts = new Card(HEARTS, ACE);
        final Card aceOfSpades = new Card(SPADES, ACE);

        assertThat(aceOfSpades.isEqualTo(aceOfHearts));
    }
}
