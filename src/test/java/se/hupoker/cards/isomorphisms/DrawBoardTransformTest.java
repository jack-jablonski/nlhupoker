package se.hupoker.cards.isomorphisms;

import org.junit.Test;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Rank;
import se.hupoker.cards.Suit;

import java.util.Map;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author Alexander Nyberg
 */
public class DrawBoardTransformTest {
    private BoardTransform drawTransformer = new DrawBoardTransform();

    @Test
    public void flopTransformOfMonotoneBoards() {
        CardSet first = drawTransformer.getIsomorphic(CardSet.from("2c6cAc"));
        CardSet second = drawTransformer.getIsomorphic(CardSet.from("2d6dAd"));

        assertEquals(first, second);
    }

    @Test
    public void flopTransformOfRainbowBoard() {
        CardSet first = drawTransformer.getIsomorphic(CardSet.from("5c6dAs"));
        CardSet second = drawTransformer.getIsomorphic(CardSet.from("6dAc5h"));

        assertEquals(first, second);
    }

    @Test
    public void flopTransformOfPairedBoard() {
        CardSet first = drawTransformer.getIsomorphic(CardSet.from("5c5dAs"));
        CardSet second = drawTransformer.getIsomorphic(CardSet.from("5dAc5h"));
        CardSet third = drawTransformer.getIsomorphic(CardSet.from("As5d5h"));

        assertEquals(first, second);
        assertEquals(first, third);
    }

    @Test
    public void flopTransformOfPairedBoardIsDifferentForFlushDraw() {
        CardSet first = drawTransformer.getIsomorphic(CardSet.from("5c5dAs"));
        CardSet second = drawTransformer.getIsomorphic(CardSet.from("5dAc5c"));

        assertThat(first, not(second));
    }


    @Test
    public void suitMappingForRainbowMapsAllSuits() {
        Map<Suit, Suit> map = drawTransformer.getSuitMapping(CardSet.from("2c7dKh"));

        assertTrue(map.keySet().containsAll(Suit.allOf()));
    }

    @Test
    public void suitMappingForFlushDrawMapsAllSuits() {
        Map<Suit, Suit> map = drawTransformer.getSuitMapping(CardSet.from("2c7dKd"));

        assertTrue(map.keySet().containsAll(Suit.allOf()));
    }

    @Test
    public void suitMappingForMonotoneMapsAllSuits() {
        Map<Suit, Suit> map = drawTransformer.getSuitMapping(CardSet.from("2d7dKd"));

        assertTrue(map.keySet().containsAll(Suit.allOf()));
    }

    @Test
    public void testSameMonotoneBoard() {
        final CardSet simple = drawTransformer.getIsomorphic(CardSet.from("2c7dKh"));
        final CardSet isomorphic = drawTransformer.getIsomorphic(CardSet.from("2h7dKc"));

        assertEquals(simple, isomorphic);
    }

    @Test
    public void testSameFlushingBoard() {
        final CardSet simple = drawTransformer.getIsomorphic(CardSet.from("2c7dKd"));
        final CardSet isomorphic = drawTransformer.getIsomorphic(CardSet.from("2d7cKc"));

        assertEquals(simple, isomorphic);
    }

    @Test
    public void testDifferentSuitBoard() {
        final CardSet simple = drawTransformer.getIsomorphic(CardSet.from("2c7hKh"));
        final CardSet isomorphic = drawTransformer.getIsomorphic(CardSet.from("2h7dKc"));

        assertNotSame(simple, isomorphic);
    }

    @Test
    public void testRanksAreSorted() {
        CardSet simple = drawTransformer.getIsomorphic(CardSet.from("2cKh7h"));

        assertThat(simple.at(0).rankOf(), is(Rank.TWO));
        assertThat(simple.at(1).rankOf(), is(Rank.SEVEN));
        assertThat(simple.at(2).rankOf(), is(Rank.KING));
    }
}