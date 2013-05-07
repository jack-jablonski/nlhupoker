package se.hupoker.lut.builder;

import org.junit.Before;
import org.junit.Test;
import se.hupoker.common.Street;
import se.hupoker.lut.LutPath;
import se.hupoker.lut.TurnTable;

/**
 * @author Alexander Nyberg
 */
public class TurnTest {
    private TestAllSet testAllSet = new TestAllSet();

    @Before
    public void setUp() {
        testAllSet = new TestAllSet();
    }

    @Test
    public void allSet() {
        TurnTable turnTable = new TurnTable();
        turnTable.load(LutPath.getTurnHs());

        testAllSet.testAllHandCombinationsSet(turnTable, Street.TURN);
    }
    @Test
    public void randomHS() {
        TurnTable tt = new TurnTable();
        tt.load(LutPath.getTurnHs());

        TestTableUtility test = new TestTableUtility(TestTableUtility.hsRunner);
        int NUM_RANDOM_ITERATIONS = 1000;
        test.runAll(tt, Street.TURN, NUM_RANDOM_ITERATIONS);
    }
}