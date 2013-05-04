package se.hupoker.cards.handeval;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.ICombinatoricsVector;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.DeckSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardisomorphisms.Pair;
import se.hupoker.cards.boardisomorphisms.PairManager;

import java.util.Arrays;

/**
 * Build the table of equities of hole card combinations.
 * 13.4M = Binomial(52,2)^2 * 8 byte / 1024^2
 *
 * @author Alexander Nyberg
 */
final public class EquityMatrix {
    /*
     *  Equities when all cards run out
     *  TODO: 1) Half size from using float. 2) Half size from get(i,j) = 1 - get(j,i)
     *
     */
    private final double[][] equities;

    /*
     * Current status for flop & turn. Only needs 3 bits...
     */
    private double[][] ahead;

    /*
     * Cached holecards vs. other holecards on this board.
     */
    private final EquityMeasure measure = new EquityMeasure();
    private final EquityAdapter equityAdapter = new EquityAdapter();
    private final PairManager pairManager;
    private final CardSet board;

    private EquityMatrix(CardSet board) {
        this.pairManager = PairManager.create(board);
        this.board = board;

        equities = new double[HoleCards.TexasCombinations][HoleCards.TexasCombinations];
        if (board.size() != 5) {
            ahead = new double[HoleCards.TexasCombinations][HoleCards.TexasCombinations];
            for (double[] anAhead : ahead) {
                Arrays.fill(anAhead, EquityMeasure.BADEQUITY);
            }
        }

        for (double[] anEquity : equities) {
            Arrays.fill(anEquity, EquityMeasure.BADEQUITY);
        }
    }

    /**
     *
     * @param board
     * @return
     */
    public static EquityMatrix from(CardSet board) {
        EquityMatrix equityMatrix = new EquityMatrix(board);
        equityMatrix.calculate();
        return equityMatrix;
    }

    public void printStatistics() {
        System.out.println(pairManager.getStatistics());
    }

    private void calculate() {
        DeckSet deck = DeckSet.freshDeck();
        deck.removeAll(this.board);
        iterateHoleCards(deck, this.board);
    }

    protected double getEquity(HoleCards myHole, HoleCards opHole) {
        return equities[myHole.ordinal()][opHole.ordinal()];
    }

    public double getHs(HoleCards hole) {
        return measure.getHS(ahead[hole.ordinal()]);
    }

    public double getPpot(HoleCards hole) {
        return measure.getPPOT(ahead[hole.ordinal()], equities[hole.ordinal()]);
    }

    public double getNpot(HoleCards hole) {
        return measure.getPPOT(ahead[hole.ordinal()], equities[hole.ordinal()]);
    }

    // For river
    public double getAverageEquity(HoleCards hole) {
        return measure.getEquity(equities[hole.ordinal()]);
    }

    private double getEquityPair(HoleCards myHole, HoleCards opHole) {
        return equities[myHole.ordinal()][opHole.ordinal()];
    }

    private void setMatrixValue(double matrix[][], HoleCards myHole, HoleCards opHole, double value) {
        assert (!Double.isNaN(value));

        matrix[myHole.ordinal()][opHole.ordinal()] = value;
        matrix[opHole.ordinal()][myHole.ordinal()] = (1 - value);
    }

    private void setEquityPair(HoleCards myHole, HoleCards opHole, double equity) {
        setMatrixValue(equities, myHole, opHole, equity);
    }

    private void setAheadPair(HoleCards myHole, HoleCards opHole, double now) {
        setMatrixValue(ahead, myHole, opHole, now);
    }

    /**
     * Find & set the equity of hole vs. hole on some board.
     *
     * @param deck   Intersects with myHole & opHole!
     * @param board
     * @param myHole
     * @param opHole
     */
    private void evaluateHoleCards(DeckSet deck, CardSet board, HoleCards myHole, HoleCards opHole) {
        final int remainingCards = equityAdapter.getNumberOfRemainingCards(board);
        double equity;

        Pair<HoleCards> cached = pairManager.getAndAddCached(myHole, opHole);
        if (cached == null) {
            if (remainingCards > 0) {
                DeckSet newDeck = new DeckSet(deck);
                newDeck.removeAll(myHole);
                newDeck.removeAll(opHole);

                equity = equityAdapter.iterateBoards(remainingCards, newDeck, board, myHole, opHole);
            } else {
                equity = equityAdapter.get(board, myHole, opHole);
            }
        } else {
            equity = getEquityPair(cached.getFirst(), cached.getSecond());
        }

        setEquityPair(myHole, opHole, equity);

        // For flop & turn. Probably too fast to bother with separate cache.
        if (remainingCards != 0) {
            double now = equityAdapter.get(board, myHole, opHole);
            setAheadPair(myHole, opHole, now);
        }
    }

    private void iterateHoleCards(DeckSet deck, CardSet board) {
        final int[][] inner = {{0, 1, 2, 3}, {0, 2, 1, 3}, {0, 3, 1, 2}};
        ICombinatoricsVector<Card> initialVector = Factory.createVector(deck);

        for (ICombinatoricsVector<Card> comb : Factory.createSimpleCombinationGenerator(initialVector, 4)) {
            for (int tp[] : inner) {
                Card myOne = comb.getValue(tp[0]);
                Card myTwo = comb.getValue(tp[1]);
                HoleCards myHole = HoleCards.of(myOne, myTwo);

                Card opOne = comb.getValue(tp[2]);
                Card opTwo = comb.getValue(tp[3]);
                HoleCards opHole = HoleCards.of(opOne, opTwo);

                evaluateHoleCards(deck, board, myHole, opHole);
            }
        }
    }
}