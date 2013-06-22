package se.hupoker.cards;

import org.junit.Test;

import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Nyberg
 */
public class CardTest {
    @Test
    public void testFactoryReturnsSameObject() {
        Card first = Card.from(Rank.ACE, Suit.SPADE);
        Card second = Card.from(Rank.ACE, Suit.SPADE);

        assertTrue(first == second);
        assertEquals(first, second);
    }

    @Test
    public void testEqualsIsFalseForDifferentCards() {
        Card first = Card.from(Rank.ACE, Suit.SPADE);
        Card second = Card.from(Rank.ACE, Suit.CLUB);

        assertNotSame(first, second);
    }

    @Test
    public void testOrdinalIsUniqueAndWithoutGaps() {
        SortedSet<Integer> set = new TreeSet<>();

        for (Card card: Card.allOf()) {
            boolean setAffected = set.add(card.ordinal());
            assertTrue(setAffected);
        }

        // No auto-boxing :(
        assertEquals(new Integer(0), set.first());
        assertEquals(new Integer(Card.NumberOfCards -1), set.last());
    }
}
