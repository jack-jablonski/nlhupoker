package se.hupoker.cards.isomorphisms;

import com.google.common.math.IntMath;
import org.junit.Test;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Alexander Nyberg
 *
 */
public class HoleTransformTest {
    @Test
    public void testNumberOfHoleCards() {
        Set<HoleCards> set = new HashSet<>();
        CardSet board = CardSet.from("4c5c6c");

        HoleTransform holeTransform = new HoleTransform(board);

        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            HoleCards isomorphic = holeTransform.apply(hole);
            set.add(isomorphic);
        }

        // Flushes + pairs + flushdraw + nothing
        int isomorphisms = IntMath.binomial(10, 2) + IntMath.binomial(13, 1) + 10*13 + IntMath.binomial(13, 2);

        assertThat(isomorphisms, is(set.size()));
    }

    @Test
    public void monotoneBoardsHaveIdenticalHoleCards() {
        HoleTransform first = new HoleTransform(CardSet.from("2c6cAc"));
        HoleCards firstHole = first.apply(HoleCards.from("KcJc"));

        HoleTransform second = new HoleTransform(CardSet.from("2d6dAd"));
        HoleCards secondHole = second.apply(HoleCards.from("KdJd"));

        assertEquals(firstHole, secondHole);
    }
}