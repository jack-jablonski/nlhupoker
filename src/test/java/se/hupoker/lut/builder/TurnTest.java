package se.hupoker.lut.builder;

import org.junit.Before;
import org.junit.Test;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardenumerator.BoardRunner;
import se.hupoker.cards.boardenumerator.IsomorphicBoardEnumerator;
import se.hupoker.common.Street;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutPath;
import se.hupoker.lut.TurnTable;

/**
 * @author Alexander Nyberg
 */
public class TurnTest {
    private TestAllSet testAllSet = new TestAllSet();
    private TurnTable turnTable;

    @Before
    public void setUp() {
        testAllSet = new TestAllSet();
        turnTable = new TurnTable();
    }

    private BoardRunner setBoards = new BoardRunner() {
        @Override
        public void evaluateBoard(CardSet board) {
            for (HoleCards hole : HoleCards.allOf()) {
                if (board.containsAny(hole)) {
                    continue;
                }

                final LutKey key = new LutKey(board, hole);
                turnTable.setManually(key, (float) hole.ordinal() / HoleCards.TexasCombinations);
            }
        }
    };

    @Test
    public void allEntriesInTableShouldBeSetAfterEnumeration() {
        IsomorphicBoardEnumerator turnEnumeration = new IsomorphicBoardEnumerator(setBoards, Street.TURN);
        turnEnumeration.enumerate();
        testAllSet.testAllHandCombinationsSet(turnTable, Street.TURN);
    }

    @Test
    public void allSetInExistingTable() {
        turnTable = TurnTable.create(LutPath.getTurnHs());

        testAllSet.testAllHandCombinationsSet(turnTable, Street.TURN);
    }
    @Test
    public void compareRandomTableEntriesWithFreshCalculationValues() {
        int numberOfIterations = 1000;
        turnTable = TurnTable.create(LutPath.getTurnHs());

        TestTableUtility .runRandomComparisons(turnTable, Street.TURN, numberOfIterations, TestTableUtility.handEvaluator);
    }
}