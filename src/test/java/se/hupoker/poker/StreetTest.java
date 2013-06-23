package se.hupoker.poker;

import org.junit.Test;

/**
 * @author Alexander Nyberg
 */
public class StreetTest {
    @Test(expected = RuntimeException.class)
    public void nextStreetOnLastStreetThrows() {
        Street next = Street.RIVER.next();
    }
}