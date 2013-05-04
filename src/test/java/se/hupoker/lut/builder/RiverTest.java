package se.hupoker.lut.builder;

import org.junit.Before;
import org.junit.Test;
import se.hupoker.cards.HoleCards;
import se.hupoker.common.Street;
import se.hupoker.lut.LutPath;
import se.hupoker.lut.RiverTable;
import se.hupoker.cards.CardSet;

/**
 * @author Alexander Nyberg
 */
public class RiverTest {
    private TestAllSet testAllSet;
    private RiverTable riverTable;

    @Before
    public void setUp() {
        riverTable = new RiverTable();
        riverTable.load(LutPath.getRiverHs());
        testAllSet = new TestAllSet();
    }

    @Test
    public void allSet() {
        testAllSet.testAllHandCombinationsSet(riverTable, Street.RIVER);
    }

    @Test
    public void otherSpecific() {
        CardSet board = CardSet.from("2c2d2s3c4c");
        HoleCards hole = HoleCards.from("JhJs");

        testAllSet.testCombinationSet(riverTable, board, hole);
    }

    @Test
    public void specific() {
        // 4c5c6c7c2d|2c3c
        // 4c5c6c2d2h|2c3c
        CardSet board = CardSet.from("4c5c6c2d2h");
        HoleCards hole = HoleCards.from("2c3c");

        testAllSet.testCombinationSet(riverTable, board, hole);
    }

    @Test
    public void randomHS() {
        int NumberOfIterations = 1000;

        TestTableUtility test = new TestTableUtility(TestTableUtility.hsRunner);
        test.runAll(riverTable, Street.RIVER, NumberOfIterations);
    }
}