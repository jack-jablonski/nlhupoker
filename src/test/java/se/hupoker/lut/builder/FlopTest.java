package se.hupoker.lut.builder;

import org.junit.Before;
import org.junit.Test;
import se.hupoker.common.Street;
import se.hupoker.lut.FlopTable;
import se.hupoker.lut.LutPath;

/**
 *
 * @author Alexander Nyberg
 *
 */
public class FlopTest {
    private TestAllSet testAllSet = new TestAllSet();

    @Before
    public void setUp() {
        testAllSet = new TestAllSet();
    }

    @Test
    public void allSet() {
        FlopTable flopTable = new FlopTable();
        flopTable.load(LutPath.getFlopHs());

        testAllSet.testAllHandCombinationsSet(flopTable, Street.FLOP);
    }

    @Test
    public void randomHS() {
        FlopTable ft = new FlopTable();
        ft.load(LutPath.getFlopHs());

        int NUM_RANDOM_ITERATIONS = 100;
        TestTableUtility test = new TestTableUtility(TestTableUtility.hsRunner);
        test.runAll(ft, Street.FLOP, NUM_RANDOM_ITERATIONS);
    }
}
