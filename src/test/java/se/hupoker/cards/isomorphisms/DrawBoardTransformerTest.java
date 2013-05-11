package se.hupoker.cards.isomorphisms;

import org.junit.Before;
import org.junit.Test;
import se.hupoker.cards.CardSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * @author Alexander Nyberg
 */
public class DrawBoardTransformerTest {
    private DrawBoardTransformer transform;

    @Before
    public void setUp() {
        transform = new DrawBoardTransformer();
    }

    @Test
    public void testSameMonotoneBoard() throws Exception {
        final CardSet simple = transform.getIsomorphic(CardSet.from("2c7dKh"));
        final CardSet isomorphic = transform.getIsomorphic(CardSet.from("2h7dKc"));

        assertEquals(simple, isomorphic);
    }

    @Test
    public void testSameFlushingBoard() throws Exception {
        final CardSet simple = transform.getIsomorphic(CardSet.from("2c7dKd"));
        final CardSet isomorphic = transform.getIsomorphic(CardSet.from("2d7cKc"));

        assertEquals(simple, isomorphic);
    }

    @Test
    public void testDifferentSuitBoard() throws Exception {
        final CardSet simple = transform.getIsomorphic(CardSet.from("2c7hKh"));
        final CardSet isomorphic = transform.getIsomorphic(CardSet.from("2h7dKc"));

        assertNotSame(simple, isomorphic);
    }
}