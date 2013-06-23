package se.hupoker.cards.handeval;

import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;

import java.util.List;

/**
 * @author Alexander Nyberg
 */
public interface EquityEvaluator {
    /**
     *
     * @param board The public board
     * @param myHole
     * @param opHole
     * @return The equity of myHole versus opHole
     */
    double evaluate(CardSet board, HoleCards myHole, HoleCards opHole);

    /**
     *
     * @param board The public board
     * @param myHole
     * @param opHole
     * @param extra Extra cards (that are run out) that belong to the board.
     * @return The equity of myHole versus opHole
     */
    double evaluateWithExtra(CardSet board, HoleCards myHole, HoleCards opHole, List<Card> extra);
}