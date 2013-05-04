package se.hupoker.cards.boardenumerator;

import org.junit.Before;
import org.junit.Test;
import se.hupoker.cards.CardSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * @author Alexander Nyberg
 */
public class DrawBoardTransformerTest {
    DrawBoardTransformer transform;

    @Before
    public void setUp() {
        transform = new DrawBoardTransformer();
    }

    @Test
    public void testSameBoard() throws Exception {
        final CardSet simple = transform.getIsomorphic(CardSet.from("2c7dKh"));
        final CardSet isomorphic = transform.getIsomorphic(CardSet.from("2h7dKc"));

        assertEquals(simple, isomorphic);
    }

    @Test
    public void testDifferentBoard() throws Exception {
        final CardSet simple = transform.getIsomorphic(CardSet.from("2c7hKh"));
        final CardSet isomorphic = transform.getIsomorphic(CardSet.from("2h7dKc"));

        assertNotSame(simple, isomorphic);
    }
}