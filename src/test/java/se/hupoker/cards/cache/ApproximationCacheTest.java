package se.hupoker.cards.cache;

import org.junit.Before;
import org.junit.Test;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.ICombinatoricsVector;
import se.hupoker.cards.Card;
import se.hupoker.cards.DeckSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.handeval.HoleCardCombinations;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Nyberg
 */
public class ApproximationCacheTest {
    private CardSet board;
    private ApproximationCache approximationCache;

    @Before
    public void setUp() {
        board = CardSet.from("4s7d5c");
//        board = CardSet.from("4s7s5s");
        approximationCache = ApproximationCache.create(board);
    }

    /**
     * binomial(49,2) * binomial(47,2) = 1,271,256
     */
    @Test
    public void inspectHitsAndMissesForBoard() {
        HoleCardCombinations holeCardCombinations = new HoleCardCombinations();
        DeckSet deck = DeckSet.freshDeck();
        deck.removeAll(board);

        ICombinatoricsVector<Card> deckVector = Factory.createVector(deck);
        for (ICombinatoricsVector<Card> holes : Factory.createSimpleCombinationGenerator(deckVector, 4)) {
            for (Pair<HoleCards> pair : holeCardCombinations.get(holes.getVector())) {
                approximationCache.getAndAddCached(pair.getFirst(), pair.getSecond());
            }
        }

        assertTrue(approximationCache.getCacheHit() > approximationCache.getCacheMiss());
    }

    @Test
    public void cacheFindsInserted() {
        HoleCards one = HoleCards.from("2c3c");
        HoleCards two = HoleCards.from("4c2d");

        Pair<HoleCards> pair = approximationCache.getAndAddCached(one, two);
        assertNull(pair);

        pair = approximationCache.getAndAddCached(one, two);
        assertTrue(one.equals(pair.getFirst()));
        assertTrue(two.equals(pair.getSecond()));

        pair = approximationCache.getAndAddCached(two, one);
        assertTrue(two.equals(pair.getFirst()));
        assertTrue(one.equals(pair.getSecond()));
    }

    @Test
    public void cacheFindsIsomorphicHands() {
        HoleCards one = HoleCards.from("2c3c");
        HoleCards two = HoleCards.from("4c2d");

        Pair<HoleCards> pair = approximationCache.getAndAddCached(one, two);
        assertNull(pair);

        HoleCards isomorphicTwo = HoleCards.from("2c4d");
        HoleCards isomorphicOne = HoleCards.from("3c2c");

        pair = approximationCache.getAndAddCached(isomorphicTwo, isomorphicOne);
        assertTrue(two.equals(pair.getFirst()));
        assertTrue(one.equals(pair.getSecond()));
/*
        HoleCards fOne = HoleCards.from("3c2d");
        HoleCards fTwo = HoleCards.from("2c4c");

        pair = approximationCache.getAndAddCached(fOne, fTwo);
        assertTrue(one.equals(pair.getFirst()));
        assertTrue(two.equals(pair.getSecond()));*/
    }
}