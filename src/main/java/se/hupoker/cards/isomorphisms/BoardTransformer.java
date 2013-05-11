package se.hupoker.cards.isomorphisms;

import com.google.common.collect.ImmutableSortedSet;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Suit;
import se.hupoker.common.Street;

import java.util.SortedSet;

import static se.hupoker.common.Street.RIVER;

/**
 * @author Alexander Nyberg
 */
public abstract class BoardTransformer {
    /**
     * @param street (flop,turn) or river.
     * @return The transformer for the street
     */
    public static BoardTransformer from(Street street) {
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
     * @param board Arbitrary input.
     * @return The isomorphic equivalent of 'board'
     */
    public abstract CardSet getIsomorphic(CardSet board);
}