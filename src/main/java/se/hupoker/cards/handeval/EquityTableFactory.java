package se.hupoker.cards.handeval;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.ICombinatoricsVector;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.DeckSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.cache.ApproximationCache;
import se.hupoker.cards.cache.Pair;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Alexander Nyberg
 */
public class EquityTableFactory {
    private final EquityTable equityTable = new EquityTable();
    private final EquityAdapter equityAdapter = new EquityAdapter();
    private final ApproximationCache approximationCache;
    private final EquityEvaluator equityEvaluator;

    private EquityTableFactory(EquityEvaluator equityEvaluator, ApproximationCache approximationCache) {
        this.equityEvaluator = equityEvaluator;
        this.approximationCache = approximationCache;
    }

    /**
     * @param board The board on which to build holeCards vs. holeCards equities on.
     * @return holeCards vs. holeCards equity matrix
     */
    public static EquityTable calculate(CardSet board) {
        ApproximationCache approximationCache = ApproximationCache.create(board);

        EquityTableFactory equityTableFactory = new EquityTableFactory(new KlaatuEvaluator(), approximationCache);
        return equityTableFactory.buildEquityTable(board);
    }

    public static EquityTable calculateWithEvaluator(EquityEvaluator equityEvaluator, CardSet board) {
        ApproximationCache approximationCache = ApproximationCache.create(board);

        EquityTableFactory equityTableFactory = new EquityTableFactory(equityEvaluator, approximationCache);
        return equityTableFactory.buildEquityTable(board);
    }

    private EquityTable buildEquityTable(CardSet board) {
        DeckSet deck = DeckSet.freshDeck();
        deck.removeAll(board);
        iterateHoleCards(deck, board);

        System.out.println("EquityTableFactory built " + board.toString());

        return equityTable;
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

        Pair<HoleCards> cached = approximationCache.getAndAddCached(myHole, opHole);
        if (cached == null) {
            if (remainingCards > 0) {
                DeckSet newDeck = new DeckSet(deck);
                newDeck.removeAll(myHole);
                newDeck.removeAll(opHole);

                equity = equityAdapter.iterateBoards(equityEvaluator, remainingCards, newDeck, board, myHole, opHole);
            } else {
                equity = equityEvaluator.evaluate(board, myHole, opHole);
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
        HoleCardCombinations holeCardCombinations = new HoleCardCombinations();
        ICombinatoricsVector<Card> initialVector = Factory.createVector(deck);

        for (ICombinatoricsVector<Card> comb : Factory.createSimpleCombinationGenerator(initialVector, 4)) {
            Set<Pair<HoleCards>> pairSet = holeCardCombinations.get(comb.getVector());

            for (Pair<HoleCards> pair : pairSet) {
                evaluateHoleCards(deck, board, pair.getFirst(), pair.getSecond());
            }
        }
    }
}