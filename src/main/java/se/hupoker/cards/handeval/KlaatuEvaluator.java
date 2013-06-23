package se.hupoker.cards.handeval;

import pokerai.game.eval.klaatu.FastEval;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Alexander Nyberg
 */
public class KlaatuEvaluator implements EquityEvaluator {
    /**
     * For river evaluation only!
     *
     * @return The equity of 'myHole' versus 'opHole'
     */
    public double evaluateWithExtra(CardSet board, HoleCards myHole, HoleCards opHole, List<Card> extra) {
        int myValue;
        int opValue;

        int numberOfBoardCards = board.size() + extra.size();
        checkArgument(numberOfBoardCards==5);

        if (extra.size() == 1) {
            myValue = FastEval.eval7(myHole.first().ordinal(), myHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    board.at(3).ordinal(),
                    extra.get(0).ordinal());

            opValue = FastEval.eval7(opHole.first().ordinal(), opHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    board.at(3).ordinal(),
                    extra.get(0).ordinal());
        } else if (extra.size() == 2) {
            myValue = FastEval.eval7(myHole.first().ordinal(), myHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    extra.get(0).ordinal(), extra.get(1).ordinal());

            opValue = FastEval.eval7(opHole.first().ordinal(), opHole.last().ordinal(),
                    board.at(0).ordinal(), board.at(1).ordinal(), board.at(2).ordinal(),
                    extra.get(0).ordinal(), extra.get(1).ordinal());
        } else {
            throw new IllegalArgumentException("Extra must have 1 or 2 cards");
        }

        return getValue(myValue, opValue);
    }

    public double evaluate(CardSet board, HoleCards myHole, HoleCards opHole) {
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