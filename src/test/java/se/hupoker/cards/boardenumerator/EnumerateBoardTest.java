package se.hupoker.cards.boardenumerator;

import org.junit.Test;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.boardisomorphisms.IsomorphicHole;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Alexander Nyberg
 *
 */
public class EnumerateBoardTest {
    private BoardRunner runner = new BoardRunner() {
        @Override
        public void evaluateBoard(CardSet board) {
            // Do nothing
        }
    };

    @Test
    public void testNumberOfHoleCards() {
        Set<HoleCards> set = new HashSet<>();
        CardSet board = CardSet.from("4c5d6h");
        IsomorphicHole isoTransform = new IsomorphicHole(board);

        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            HoleCards isomorphic = isoTransform.getIsomorphic(hole);
            set.add(isomorphic);
        }

        System.out.println("Have #" + set.size());
    }
}