package se.hupoker.lut.builder;

import org.junit.Before;
import org.junit.Test;
import se.hupoker.cards.HoleCards;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.RiverTable;
import se.hupoker.cards.CardSet;

/**
 * @author Alexander Nyberg
 */
public class RiverEmptyTable {
    private TestAllSet testAllSet;
    private RiverTable table;

    @Before
    public void setUp() {
        table = new RiverTable();
        testAllSet = new TestAllSet();
    }

    @Test
    public void testSingleCombination() {
        CardSet board = CardSet.from("2c2d3c3h5c");
        HoleCards hole = HoleCards.from("QcQh");

        table.setManually(new LutKey(board, hole), (float) hole.ordinal() / HoleCards.TexasCombinations);
        testAllSet.testCombinationSet(table, board, hole);
    }

    @Test
    public void setBoardInTable() {
        //CardSet board = CardSet.from("AsAh3c6c7c");
        //CardSet board = CardSet.from("4c5c6c2d2h");
        CardSet board = CardSet.from("QhQsQc8h3h");

        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            table.setManually(new LutKey(board, hole), (float) hole.ordinal() / HoleCards.TexasCombinations);
        }
        testAllSet.testAllHoleCardsSet(table, board);
    }
}
