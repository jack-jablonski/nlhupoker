package se.hupoker.lut.builder;

import org.junit.Test;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardenumerator.BoardRunner;
import se.hupoker.cards.boardenumerator.IsomorphicBoardEnumerator;
import se.hupoker.common.Street;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutPath;
import se.hupoker.lut.RiverTable;
import se.hupoker.cards.CardSet;

import static junit.framework.Assert.assertEquals;

/**
 * @author Alexander Nyberg
 */
public class RiverTest {
    private TestAllSet testAllSet = new TestAllSet();
    private RiverTable riverTable;

    private BoardRunner setBoards = new BoardRunner() {
        @Override
        public void evaluateBoard(CardSet board) {
            for (HoleCards hole : HoleCards.allOf()) {
                if (board.containsAny(hole)) {
                    continue;
                }

                LutKey key = new LutKey(board, hole);
                riverTable.setManually(key, (float) hole.ordinal() / HoleCards.TexasCombinations);
            }
        }
    };

    @Test
    public void allEntriesInTableShouldBeSetAfterEnumeration() {
        IsomorphicBoardEnumerator river = new IsomorphicBoardEnumerator(setBoards, Street.RIVER);

        river.enumerate();
        testAllSet.testAllHandCombinationsSet(riverTable, Street.RIVER);
    }

    @Test
    public void allSetInExistingTable() {
        testAllSet.testAllHandCombinationsSet(riverTable, Street.RIVER);
        riverTable = new RiverTable();
    }

    @Test
    public void otherSpecific() {
        riverTable = RiverTable.create(LutPath.getRiverHs());
        CardSet board = CardSet.from("2c2d2s3c4c");
        HoleCards hole = HoleCards.from("JhJs");

        testAllSet.testCombinationSet(riverTable, board, hole);
    }

    @Test
    public void testCalculatedValue() {
        riverTable = RiverTable.create(LutPath.getRiverHs());
        CardSet board = CardSet.from("6sAsKh7s2s");
        HoleCards hole = HoleCards.from("9sJd");

        /**
         * There are 4 higher spades (T,J,Q,K). Remove those + known cards
         * then there are 52-5-2-4=41 remaining. 4*41+nchoosek(4,2) hands beat me out of
         * nchoosek(45,2).
         */
        double hsValue = 0.828282828282828282;

        LutKey key = new LutKey(board,hole);
        assertEquals(hsValue, riverTable.lookupOne(key));
    }

    @Test
    public void testSpecificTableEntry() {
        riverTable = RiverTable.create(LutPath.getRiverHs());
        // 4c5c6c7c2d|2c3c
        // 4c5c6c2d2h|2c3c
        CardSet board = CardSet.from("4c5c6c2d2h");
        HoleCards hole = HoleCards.from("2c3c");

        testAllSet.testCombinationSet(riverTable, board, hole);
    }

    @Test
    public void compareRandomTableEntriesWithFreshCalculationValues() {
        int numberOfIterations = 1000;
        riverTable = RiverTable.create(LutPath.getRiverHs());

        TestTableUtility.runRandomComparisons(riverTable, Street.RIVER, numberOfIterations, TestTableUtility.handEvaluator);
    }
}