package se.hupoker.cards.boardenumerator;

import com.google.common.collect.ImmutableSortedSet;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Suit;
import se.hupoker.common.Street;

import java.util.SortedSet;

import static se.hupoker.common.Street.RIVER;

/**
 *
 * @author Alexander Nyberg
 */
abstract class BoardTransformer {
    /**
     * Static factory according to (flop,turn) or river.
     *
     * @param street
     * @return
     */
    protected static BoardTransformer from(Street street) {
        if (street.equals(RIVER)) {
            return new RiverBoardTransformer();
        } else {
            return new DrawBoardTransformer();
        }
    }

    protected SortedSet<Suit> getSortedSuits() {
        return ImmutableSortedSet.copyOf(Suit.allOf());
    }

    /**
     *
     * @param board
     * @return Isomorphic board.
     */
    abstract CardSet getIsomorphic(CardSet board);
}