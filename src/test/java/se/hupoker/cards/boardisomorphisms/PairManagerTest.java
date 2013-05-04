package se.hupoker.cards.boardisomorphisms;

import org.junit.Before;
import org.junit.Test;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.CardSet;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Nyberg
 */
public class PairManagerTest {
    private PairManager pairManager;

    @Before
    public void setUp() {
        CardSet board = CardSet.from("4s7d5c");
        pairManager = PairManager.create(board);
    }

    @Test
    public void cacheFindsInserted() {
        HoleCards one = HoleCards.from("2c3c");
        HoleCards two = HoleCards.from("4c2d");

        Pair<HoleCards> pair = pairManager.getAndAddCached(one, two);
        assertNull(pair);

        pair = pairManager.getAndAddCached(one, two);
        assertTrue(one.equals(pair.getFirst()));
        assertTrue(two.equals(pair.getSecond()));

        pair = pairManager.getAndAddCached(two, one);
        assertTrue(two.equals(pair.getFirst()));
        assertTrue(one.equals(pair.getSecond()));
    }

    @Test
    public void cacheFindsIsomorphicHands() {
        HoleCards one = HoleCards.from("2c3c");
        HoleCards two = HoleCards.from("4c2d");

        Pair<HoleCards> pair = pairManager.getAndAddCached(one, two);
        assertNull(pair);

        HoleCards isomorphicTwo = HoleCards.from("2c4d");
        HoleCards isomorphicOne = HoleCards.from("3c2c");

        pair = pairManager.getAndAddCached(isomorphicTwo, isomorphicOne);
        assertTrue(two.equals(pair.getFirst()));
        assertTrue(one.equals(pair.getSecond()));
/*
        HoleCards fOne = HoleCards.from("3c2d");
        HoleCards fTwo = HoleCards.from("2c4c");

        pair = pairManager.getAndAddCached(fOne, fTwo);
        assertTrue(one.equals(pair.getFirst()));
        assertTrue(two.equals(pair.getSecond()));*/
    }
}