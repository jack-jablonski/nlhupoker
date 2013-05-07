package se.hupoker.cards.boardisomorphisms;

import com.google.common.math.IntMath;
import org.junit.Test;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Alexander Nyberg
 *
 */
public class IsomorphicHoleTest {
    @Test
    public void testNumberOfHoleCards() {
        Set<HoleCards> set = new HashSet<>();
        CardSet board = CardSet.from("4c5c6c");
        IsomorphicHole isoTransform = new IsomorphicHole(board);

        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            HoleCards isomorphic = isoTransform.getIsomorphic(hole);
            set.add(isomorphic);
        }

        // Flushes + pairs + flushdraw + nothing
        int isomorphisms = IntMath.binomial(10, 2) + IntMath.binomial(13, 1) + 10*13 + IntMath.binomial(13, 2);

        assertThat(isomorphisms, is(set.size()));
    }
}