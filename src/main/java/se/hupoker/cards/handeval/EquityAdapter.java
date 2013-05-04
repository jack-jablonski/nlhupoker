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

/**
 * Adapter for evaluating hole cards on boards.
 *
 * @author Alexander Nyberg
 */
class EquityAdapter {
    /**
     *
     * @param numberOfCards Extra cards to lay out.
     * @param deck
     * @param board
     * @param myHole
     * @param opHole
     * @return The equity in 'myHole' vs 'opHole' on this board.
     */
    protected double iterateBoards(int numberOfCards, DeckSet deck, CardSet board, HoleCards myHole, HoleCards opHole) {
        final int numHoleIterations = IntMath.binomial(deck.size(), numberOfCards);
        double eqSum = 0;

        ICombinatoricsVector<Card> initialVector = Factory.createVector(deck);
        for (ICombinatoricsVector<Card> extra : Factory.createSimpleCombinationGenerator(initialVector, numberOfCards)) {
            eqSum += getWithExtra(board, myHole, opHole, extra);
        }

        double equity = eqSum / numHoleIterations;
        //System.out.println(myHole.toString() + " vs. " + opHole.toString() + " : " + equity);
        return equity;
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

    /**
     * For river only!
     *
     * @return
     */
    private double getWithExtra(CardSet board, HoleCards myHole, HoleCards opHole, ICombinatoricsVector<Card> extra) {
        int myValue;
        int opValue;

        int numberOfBoardCards = board.size() + extra.getSize();
        assert numberOfBoardCards==5;

/*        Iterable<Card> myCards = Iterables.concat(board, myHole, extra);
        Iterable<Card> opCards = Iterables.concat(board, opHole, extra);

        myValue = riverWrapper(myCards);
        opValue = riverWrapper(opCards);*/

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

    private int flopWrapper(Iterable<Card> iterable) {
        Iterator<Card> iterator = iterable.iterator();
        return FastEval.eval5(iterator.next().ordinal(), iterator.next().ordinal(),
                iterator.next().ordinal(), iterator.next().ordinal(), iterator.next().ordinal());
    }

    private int turnWrapper(Iterable<Card> iterable) {
        Iterator<Card> iterator = iterable.iterator();
        return FastEval.eval6(iterator.next().ordinal(), iterator.next().ordinal(),
                iterator.next().ordinal(), iterator.next().ordinal(),
                iterator.next().ordinal(), iterator.next().ordinal());
    }

    private int riverWrapper(Iterable<Card> iterable) {
        Iterator<Card> iterator = iterable.iterator();
        return FastEval.eval7(iterator.next().ordinal(), iterator.next().ordinal(),
                iterator.next().ordinal(), iterator.next().ordinal(), iterator.next().ordinal(),
                iterator.next().ordinal(), iterator.next().ordinal());
    }

    protected double get(CardSet board, HoleCards myHole, HoleCards opHole) {
        int myValue;
        int opValue;

//        Iterable<Card> myCards = Iterables.concat(board, myHole);
//        Iterable<Card> opCards = Iterables.concat(board, opHole);

        if (board.size() == 3) {
//            myValue = flopWrapper(myCards);
            myValue = FastEval.eval5(myHole.first().ordinal(), myHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal());

//            opValue = flopWrapper(opCards);
            opValue = FastEval.eval5(opHole.first().ordinal(), opHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal());
        } else if (board.size() == 4) {
//            myValue = turnWrapper(myCards);
            myValue = FastEval.eval6(myHole.first().ordinal(), myHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(),
                    board.at(2).ordinal(), board.at(3).ordinal());

//            opValue = turnWrapper(opCards);
            opValue = FastEval.eval6(opHole.first().ordinal(), opHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(),
                    board.at(2).ordinal(), board.at(3).ordinal());
        } else if (board.size() == 5) {
//            myValue = riverWrapper(myCards);
            myValue = FastEval.eval7(myHole.first().ordinal(), myHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    board.at(3).ordinal(), board.at(4).ordinal());

//            opValue = riverWrapper(opCards);
            opValue = FastEval.eval7(opHole.first().ordinal(), opHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    board.at(3).ordinal(), board.at(4).ordinal());
        } else {
            throw new IllegalArgumentException(board.toString());
        }

        return getValue(myValue, opValue);
    }
}