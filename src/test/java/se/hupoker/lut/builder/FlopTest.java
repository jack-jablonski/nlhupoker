package se.hupoker.lut.builder;

import org.junit.Before;
import org.junit.Test;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardenumerator.BoardRunner;
import se.hupoker.cards.boardenumerator.IsomorphicBoardEnumerator;
import se.hupoker.common.Street;
import se.hupoker.lut.FlopTable;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutPath;

/**
 *
 * @author Alexander Nyberg
 *
 */
public class FlopTest {
    private TestAllSet testAllSet = new TestAllSet();
    private FlopTable flopTable;

    @Before
    public void setUp() {
        flopTable = new FlopTable();
    }

    private BoardRunner setBoards = new BoardRunner() {
        @Override
        public void evaluateBoard(CardSet board) {
            for (HoleCards hole : HoleCards.allOf()) {
                if (board.containsAny(hole)) {
                    continue;
                }

                final LutKey key = new LutKey(board, hole);
                flopTable.setManually(key, (float) hole.ordinal() / HoleCards.TexasCombinations);
            }
        }
    };

    @Test
    public void allEntriesInTableShouldBeSetAfterEnumeration() {
        IsomorphicBoardEnumerator flop = new IsomorphicBoardEnumerator(setBoards, Street.FLOP);
        flop.enumerate();
        testAllSet.testAllHandCombinationsSet(flopTable, Street.FLOP);
    }

    @Test
    public void allSetInExistingTable() {
        flopTable = FlopTable.create(LutPath.getFlopHs());

        testAllSet.testAllHandCombinationsSet(flopTable, Street.FLOP);
    }

    @Test
    public void compareRandomTableEntriesWithFreshCalculationValues() {
        final int iterations = 100;
        flopTable = FlopTable.create(LutPath.getFlopHs());

        TestTableUtility.runRandomComparisons(flopTable, Street.FLOP, iterations, TestTableUtility.handEvaluator);
    }
}
