package se.hupoker.cards;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * @author Alexander Nyberg
 */
public class CardSetTest {
    @Test (expected = IllegalArgumentException.class)
    public void throwsExceptionOnDuplicateCard() {
        CardSet set = CardSet.from("Ac4d4d");
    }

    @Test
    public void equalsForDifferentOrder() {
        CardSet set = CardSet.from("4d3dAc");
        CardSet duplicate = CardSet.from("4d3dAc");
        CardSet otherOrder = CardSet.from("Ac4d3d");

        assertEquals(set, duplicate);
        assertEquals(set, otherOrder);
    }

    @Test
    public void notEqualCardSetsOfSameSize() {
        CardSet set = CardSet.from("Ac4d3d");
        CardSet differentRank = CardSet.from("Ac4d5d");
        CardSet differentSuit = CardSet.from("Ad4d3d");

        assertNotSame(differentRank, set);
        assertNotSame(differentSuit, set);
    }

    @Test
    public void notEqualCardSetsOfDifferentSize() {
        CardSet set = CardSet.from("Ac4d3d");
        CardSet largerSet = CardSet.from("Ac4d3d3c");
        CardSet smallerSet = CardSet.from("Ac4d");

        assertNotSame(largerSet, set);
        assertNotSame(smallerSet, set);
    }
}