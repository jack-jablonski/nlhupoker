package se.hupoker.cards;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
			assertFalse(set.contains(hole.ordinal()));
			set.add(hole.ordinal());
		}

        assertEquals(new Integer(0), set.first());
        assertEquals(new Integer(HoleCards.TexasCombinations -1), set.last());
	}
}