package se.hupoker.cards.isomorphisms;

import org.junit.Test;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Suit;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Nyberg
 */
public class DrawBoardTransformerTest {
    private DrawBoardTransform transform = new DrawBoardTransform();

    @Test
    public void suitMappingForRainbowMapsAllSuits() {
        Map<Suit, Suit> map = transform.getSuitMapping(CardSet.from("2c7dKh"));

        assertTrue(map.keySet().containsAll(Suit.allOf()));
    }

    @Test
    public void suitMappingForFlushDrawMapsAllSuits() {
        Map<Suit, Suit> map = transform.getSuitMapping(CardSet.from("2c7dKd"));

        assertTrue(map.keySet().containsAll(Suit.allOf()));
    }

    @Test
    public void suitMappingForMonotoneMapsAllSuits() {
        Map<Suit, Suit> map = transform.getSuitMapping(CardSet.from("2d7dKd"));

        assertTrue(map.keySet().containsAll(Suit.allOf()));
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