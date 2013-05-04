package se.hupoker.cards.boardenumerator;

import com.google.common.collect.ImmutableSortedSet;
import se.hupoker.common.Street;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Suit;

import java.util.*;

import static se.hupoker.common.Street.RIVER;

/**
 *
 * @author Alexander Nyberg
 */
abstract class BoardTransformer {
    private final SortedSet<Suit> immutableSuits;

    protected BoardTransformer() {
        immutableSuits = ImmutableSortedSet.copyOf(Suit.values());
    }

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

    protected SortedSet<Suit> getImmutableSuits() {
        return immutableSuits;
    }

    /**
     *
     * @param board
     * @return Isomorphic board.
     */
    abstract CardSet getIsomorphicBoard(CardSet board);
}