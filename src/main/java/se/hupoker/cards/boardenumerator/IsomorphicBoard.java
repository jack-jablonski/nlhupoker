package se.hupoker.cards.boardenumerator;

import se.hupoker.common.Street;
import se.hupoker.cards.CardSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Has an isomorphic board been seen?
 *
 * @author Alexander Nyberg
 */
class IsomorphicBoard {
    private final Set<CardSet> cache = new HashSet<>();
    private final BoardTransformer boardTransformer;

    public IsomorphicBoard(Street street) {
        boardTransformer = BoardTransformer.from(street);
    }

    /**
     *
     * @param board
     * @return Have we seen this (isomorphic equal) board before?
     */
    public boolean seenAndAddBoard(CardSet board) {
        CardSet isomorphic = boardTransformer.getIsomorphicBoard(board);

        return !cache.add(isomorphic);
    }

    public int size() {
        return cache.size();
    }
}