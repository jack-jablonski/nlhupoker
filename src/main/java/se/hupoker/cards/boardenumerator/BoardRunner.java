package se.hupoker.cards.boardenumerator;

import se.hupoker.cards.CardSet;

/**
 *
 * @author Alexander Nyberg
 */
public interface BoardRunner {
    /**
     * 
     * @param board Evaluate on this board
     */
    public void evaluateBoard(CardSet board);
}