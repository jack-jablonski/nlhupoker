package se.hupoker.cards.handeval;

import com.google.common.math.IntMath;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.ICombinatoricsVector;
import pokerai.game.eval.klaatu.FastEval;
import se.hupoker.cards.DeckSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;

import java.util.Iterator;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Adapter for evaluating hole cards on boards according to how {@link FastEval} wants its input. Not very
 * Collection-like but evaluating hands is a complicated piece of work.
 *
 * @author Alexander Nyberg
 */
class EquityAdapter {
    /**
     *
     * @param numberOfCards Extra cards to lay out
     * @param deck Remaining deck
     * @param board The current board
     * @param myHole Player 1 hole cards
     * @param opHole Player 2 hole cards
     * @return The equity in 'myHole' vs 'opHole' on this board.
     */
    protected double iterateBoards(EquityEvaluator evaluator, int numberOfCards, DeckSet deck, CardSet board, HoleCards myHole, HoleCards opHole) {
        final int numHoleIterations = IntMath.binomial(deck.size(), numberOfCards);
        double eqSum = 0;

        ICombinatoricsVector<Card> initialVector = Factory.createVector(deck);
        for (ICombinatoricsVector<Card> extra : Factory.createSimpleCombinationGenerator(initialVector, numberOfCards)) {
            eqSum += evaluator.evaluateWithExtra(board, myHole, opHole, extra.getVector());
        }

        return eqSum / numHoleIterations;
    }

    protected int getNumberOfRemainingCards(CardSet board) {
        if (board.size() == 3) {
            return 2;
        } else if (board.size() == 4) {
            return 1;
        } else if (board.size() == 5) {
            return 0;
        } else {
            throw new IllegalArgumentException("Bad board size for EquityMatrix!");
        }
    }

}