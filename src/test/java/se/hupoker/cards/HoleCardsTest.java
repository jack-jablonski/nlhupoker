package se.hupoker.cards;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * 
 * @author Alexander Nyberg
 *
 */
public class HoleCardsTest {
	private void assertAllEqual(Collection<HoleCards> set) {
		for (HoleCards outer : set) {
			for (HoleCards inner : set) {
				assertTrue(outer.equals(inner));
				assertTrue(inner.equals(outer));
			}
		}
	}

    @Test
    public void cardOrderDoesNotMatter() {
        HoleCards first = HoleCards.from("2c4d");
        HoleCards second = HoleCards.from("4d2c");

        assertEquals(first, second);
        assertTrue(first.ordinal() == second.ordinal());
    }

    @Test
    public void factoryReturnsSameObject() {
        HoleCards first = HoleCards.from("2c4d");
        HoleCards second = HoleCards.from("4d2c");

        assertSame(first, second);
    }

	@Test
	public void testHoleCardsEquals()  {
		List<HoleCards> same = Arrays.asList(
				HoleCards.from("2c2d"),
				HoleCards.from("2c2d"),
				HoleCards.from("2d2c"),
				HoleCards.of(Card.from(Rank.TWO, Suit.CLUB), Card.from(Rank.TWO, Suit.DIAMOND)),
				HoleCards.of(Card.from(Rank.TWO, Suit.DIAMOND), Card.from(Rank.TWO, Suit.CLUB))
		);

		assertAllEqual(same);
	}

	@Test
	public void testOrdinalIsUniqueAndWithoutGaps() {
		SortedSet<Integer> set = new TreeSet<>();

		for (HoleCards hole : HoleCards.allOf()) {
			boolean setAffected = set.add(hole.ordinal());
            assertTrue(setAffected);
		}

        // No auto-boxing :(
        assertEquals(new Integer(0), set.first());
        assertEquals(new Integer(HoleCards.TexasCombinations -1), set.last());
	}
}