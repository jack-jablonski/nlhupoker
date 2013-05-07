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
    protected double iterateBoards(int numberOfCards, DeckSet deck, CardSet board, HoleCards myHole, HoleCards opHole) {
        final int numHoleIterations = IntMath.binomial(deck.size(), numberOfCards);
        double eqSum = 0;

        ICombinatoricsVector<Card> initialVector = Factory.createVector(deck);
        for (ICombinatoricsVector<Card> extra : Factory.createSimpleCombinationGenerator(initialVector, numberOfCards)) {
            eqSum += getWithExtra(board, myHole, opHole, extra);
        }

        return eqSum / numHoleIterations;
    }

    /**
     * For river evaluation only!
     *
     * @return The equity of 'myHole' versus 'opHole'
     */
    private double getWithExtra(CardSet board, HoleCards myHole, HoleCards opHole, ICombinatoricsVector<Card> extra) {
        int myValue;
        int opValue;

        int numberOfBoardCards = board.size() + extra.getSize();
        checkArgument(numberOfBoardCards==5);

        if (extra.getSize() == 1) {
            myValue = FastEval.eval7(myHole.first().ordinal(), myHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    board.at(3).ordinal(),
                    extra.getValue(0).ordinal());

            opValue = FastEval.eval7(opHole.first().ordinal(), opHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    board.at(3).ordinal(),
                    extra.getValue(0).ordinal());
        } else if (extra.getSize() == 2) {
            myValue = FastEval.eval7(myHole.first().ordinal(), myHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    extra.getValue(0).ordinal(), extra.getValue(1).ordinal());

            opValue = FastEval.eval7(opHole.first().ordinal(), opHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    extra.getValue(0).ordinal(), extra.getValue(1).ordinal());
        } else {
            throw new IllegalArgumentException("Extra must have 1 or 2 cards");
        }

        return getValue(myValue, opValue);
    }

    protected double get(CardSet board, HoleCards myHole, HoleCards opHole) {
        int myValue;
        int opValue;

        if (board.size() == 3) {
            myValue = FastEval.eval5(myHole.first().ordinal(), myHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal());

            opValue = FastEval.eval5(opHole.first().ordinal(), opHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal());
        } else if (board.size() == 4) {
            myValue = FastEval.eval6(myHole.first().ordinal(), myHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(),
                    board.at(2).ordinal(), board.at(3).ordinal());

            opValue = FastEval.eval6(opHole.first().ordinal(), opHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(),
                    board.at(2).ordinal(), board.at(3).ordinal());
        } else if (board.size() == 5) {
            myValue = FastEval.eval7(myHole.first().ordinal(), myHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    board.at(3).ordinal(), board.at(4).ordinal());

            opValue = FastEval.eval7(opHole.first().ordinal(), opHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    board.at(3).ordinal(), board.at(4).ordinal());
        } else {
            throw new IllegalArgumentException(board.toString());
        }

        return getValue(myValue, opValue);
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

    /**
     *
     * @param myValue Equivalence class value of the best five-card high poker hand of Player 1.
     * @param opValue Equivalence class value of the best five-card high poker hand of Player 2.
     * @return The corresponding equity value.
     */
    private double getValue(int myValue, int opValue) {
        if (myValue > opValue) {
            return EquityMeasure.AHEAD;
        } else if (myValue == opValue) {
            return EquityMeasure.TIED;
        } else {
            return EquityMeasure.BEHIND;
        }
    }
}