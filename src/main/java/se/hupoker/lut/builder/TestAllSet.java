package se.hupoker.lut.builder;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import se.hupoker.cards.HoleCards;
import se.hupoker.common.Street;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutTable;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 *
 * @author Alexander Nyberg
 */
final class TestAllSet {
    protected void testCombinationSet(LutTable table, CardSet board, HoleCards hole) {
        float value = table.lookupOne(new LutKey(board, hole));

        assertFalse(Float.isNaN(value));
        assertFalse(Float.isInfinite(value));

        if (value < 0 || value > 1) {
            System.out.println(board + "|" + hole + "=" + value);
            fail("Bad range");
        }
    }

    protected void testAllHoleCardsSet(LutTable table, CardSet board) {
        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            testCombinationSet(table, board, hole);
        }
    }

    /**
     * Brute force iteration over all possible (board, hole card) combinations.
     *
     * @param table
     * @param street
     */
    protected void testAllHandCombinationsSet(LutTable table, Street street) {
        ICombinatoricsVector<Card> initialVector = Factory.createVector(Card.allOf());
        Generator<Card> boardGenerator = Factory.createSimpleCombinationGenerator(initialVector, street.numberOfBoardCards());

        for (ICombinatoricsVector<Card> boardComb : boardGenerator) {
            CardSet board = new CardSet(boardComb.getVector());
            testAllHoleCardsSet(table, board);
        }
    }
}