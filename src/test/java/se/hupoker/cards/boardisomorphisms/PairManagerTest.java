package se.hupoker.cards.boardisomorphisms;

import org.junit.Test;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.CardSet;

import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Nyberg
 */
public class PairManagerTest {
    @Test
    public void testGetCached() {
        CardSet board = CardSet.from("4s7d5c");
        PairManager im = new PairManager(board);

        HoleCards one = HoleCards.from("2c3c");
        HoleCards two = HoleCards.from("4c2d");

        Pair<HoleCards> pair;
        pair = im.getAndAddCached(one, two);
        assertTrue(pair == null);

        HoleCards nextOne = HoleCards.from("2c4c");
        HoleCards nextTwo = HoleCards.from("3c2c");

        pair = im.getAndAddCached(nextOne, nextTwo);
        assertTrue(one.equals(pair.getFirst()));
        assertTrue(two.equals(pair.getSecond()));

        HoleCards fOne = HoleCards.from("3c2d");
        HoleCards fTwo = HoleCards.from("2c4c");

        pair = im.getAndAddCached(fOne, fTwo);
        assertTrue(one.equals(pair.getFirst()));
        assertTrue(two.equals(pair.getSecond()));
    }
}