package se.hupoker.cards;

import com.google.common.math.IntMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Nyberg
 */
public class RankIndexTest {
    private RankIndex rankIndex;

    @Before
    public void setUp() throws Exception {
        rankIndex = new RankIndex();
    }

    @Test
    public void maximumOrdinal() {
        int maximum = 0;

        for (Rank outer : Rank.values()) {
            for (Rank inner : Rank.values()) {
                maximum = Math.max(maximum, rankIndex.get(outer, inner));
            }
        }

        final int maximumCombinationIndex = IntMath.binomial(Rank.values().length + 2 - 1, 2) - 1;
        assertEquals(maximumCombinationIndex, maximum);
    }

    @Test
    public void testRank() {
        assertTrue(rankIndex.get(Rank.ACE, Rank.EIGHT) == rankIndex.get(Rank.EIGHT, Rank.ACE));
    }
}
