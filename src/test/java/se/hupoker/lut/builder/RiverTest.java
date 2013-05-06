package se.hupoker.lut.builder;

import org.junit.Before;
import org.junit.Test;
import se.hupoker.cards.HoleCards;
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
    private RiverTable riverTable = RiverTable.create(LutPath.getRiverHs());

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
    public void testCalculatedValue() {
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