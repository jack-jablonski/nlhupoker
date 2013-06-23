package se.hupoker.cards.boardenumerator;

import se.hupoker.cards.isomorphisms.BoardTransform;
import se.hupoker.poker.Street;
import se.hupoker.cards.CardSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Has an isomorphic board been seen?
 *
 * @author Alexander Nyberg
 */
class IsomorphicBoardCache {
    private final Set<CardSet> cache = new HashSet<>();
    private final BoardTransform boardTransform;

    public IsomorphicBoardCache(Street street) {
        boardTransform = BoardTransform.create(street);
    }

    /**
     *
     * @param board
     * @return Have we seen this (isomorphic equal) board before?
     */
    public boolean seenAndAddBoard(CardSet board) {
        CardSet isomorphic = boardTransform.getIsomorphic(board);

        return !cache.add(isomorphic);
    }

    public int size() {
        return cache.size();
    }
}