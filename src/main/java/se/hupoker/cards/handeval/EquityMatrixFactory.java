package se.hupoker.cards.handeval;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.ICombinatoricsVector;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.DeckSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.cache.Pair;
import se.hupoker.cards.cache.PairManager;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Alexander Nyberg
 */
public class EquityMatrixFactory {
    private final EquityTable equityTable = new EquityTable();
    private final EquityAdapter equityAdapter = new EquityAdapter();
    private final PairManager pairManager;

    private EquityMatrixFactory(PairManager pairManager) {
        this.pairManager = pairManager;
    }

    /**
     * @param board The board on which to build holeCards vs. holeCards equities on.
     * @return holeCards vs. holeCards equity matrix
     */
    public static EquityMatrix factory(CardSet board) {
        PairManager pairManager = PairManager.create(board);

        EquityMatrixFactory equityMatrixFactory = new EquityMatrixFactory(pairManager);
        equityMatrixFactory.buildEquityTable(board);

        return new EquityMatrix(equityMatrixFactory.getEquityTable());
    }

    private EquityTable getEquityTable() {
        return equityTable;
    }

    public void printStatistics() {
        System.out.println(pairManager.getStatistics());
    }

    private void buildEquityTable(CardSet board) {
        DeckSet deck = DeckSet.freshDeck();
        deck.removeAll(board);
        iterateHoleCards(deck, board);
    }

    private double getEquityPair(HoleCards myHole, HoleCards opHole) {
        return equityTable.getEquities()[myHole.ordinal()][opHole.ordinal()];
    }

    private void setMatrixValue(float matrix[][], HoleCards myHole, HoleCards opHole, double value) {
        checkArgument(!Double.isNaN(value));

        float approximated = (float) value;

        matrix[myHole.ordinal()][opHole.ordinal()] = approximated;
        matrix[opHole.ordinal()][myHole.ordinal()] = (1 - approximated);
    }

    private void setEquityPair(HoleCards myHole, HoleCards opHole, double equity) {
        setMatrixValue(equityTable.getEquities(), myHole, opHole, equity);
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
    }

    /**
     * This is a really nasty function but the iteration is messy and really slow when done in a nice manner.
     *
     * @param deck
     * @param board
     */
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