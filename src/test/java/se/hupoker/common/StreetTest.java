package se.hupoker.common;

import org.junit.Test;

/**
 * @author Alexander Nyberg
 */
public class StreetTest {
    @Test(expected = RuntimeException.class)
    public void nextStreetIsNull() {
        Street next = Street.RIVER.next();
    }
}