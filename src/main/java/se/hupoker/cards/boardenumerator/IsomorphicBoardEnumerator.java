package se.hupoker.cards.boardenumerator;

import com.google.common.math.IntMath;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.common.DoubleMath;
import se.hupoker.poker.Street;

/**
 * @author Alexander Nyberg
 */
public class IsomorphicBoardEnumerator {
    private final BoardRunner boardRunner;
    private final Street street;
    private final IsomorphicBoardCache isomorphicBoardCache;
    // Statistics
    private int numEvaluated, numSkipped;

    public IsomorphicBoardEnumerator(BoardRunner runner, Street street) {
        this.boardRunner = runner;
        this.street = street;
        this.isomorphicBoardCache = new IsomorphicBoardCache(street);
    }

    /**
     * Iterate all boards that are not isomorphically equivalent.
     * For example, this function will not iterate both "AsKsQs" and "AdKdQd" since they
     * are isomorphically equivalent (just change suit 'spade' to 'diamond' and you have same board).
     */
    public void enumerate() {
        ICombinatoricsVector<Card> initialVector = Factory.createVector(Card.allOf());
        Generator<Card> boardGenerator = Factory.createSimpleCombinationGenerator(initialVector, street.numberOfBoardCards());
        final long combinations = IntMath.binomial(Card.NumberOfCards, street.numberOfBoardCards());

        for (ICombinatoricsVector<Card> comb : boardGenerator) {
            CardSet board = new CardSet(comb.getVector());

            if (!isomorphicBoardCache.seenAndAddBoard(board)) {
                numEvaluated++;
                boardRunner.evaluateBoard(board);
            } else {
                numSkipped++;
            }

            printIterationStatus(combinations);
        }
    }

    public void printCompletedStats() {
        System.out.println(street + " Evaluated #" + numEvaluated + " and skipped #" + numSkipped);
    }

    private void printIterationStatus(long combinations) {
        final long finished = numEvaluated + numSkipped;
        final long step = Math.round(combinations * 0.05);

        if (finished % step == 0) {
            String formatted = DoubleMath.decimalFormat((double) finished / combinations);
            System.out.println("IsomorphicBoardEnumerator finished " + formatted);
        }
    }
}