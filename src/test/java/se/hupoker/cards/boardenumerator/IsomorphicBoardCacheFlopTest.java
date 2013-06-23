package se.hupoker.cards.boardenumerator;

import org.junit.Before;
import org.junit.Test;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import se.hupoker.poker.Street;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Nyberg
 */
public class IsomorphicBoardCacheFlopTest {
    private IsomorphicBoardCache isomorphicBoardCache;
    private final int NumberOfCards = Street.FLOP.numberOfBoardCards();
    private final int NumberOfIsomorphicBoards = 1833;

    @Before
    public void setUp() throws Exception {
        isomorphicBoardCache = new IsomorphicBoardCache(Street.FLOP);
    }

    @Test
    public void testRainbow() {
        CardSet board = CardSet.from("2c3d4h");
        CardSet isoBoard = CardSet.from("2d3h4s");

        assertFalse(isomorphicBoardCache.seenAndAddBoard(board));
        assertTrue(isomorphicBoardCache.seenAndAddBoard(board));
        assertTrue(isomorphicBoardCache.seenAndAddBoard(isoBoard));
        assertTrue(isomorphicBoardCache.seenAndAddBoard(CardSet.from("4d3h2s")));
        assertTrue(isomorphicBoardCache.seenAndAddBoard(CardSet.from("3s4h2d")));
        assertFalse(isomorphicBoardCache.seenAndAddBoard(CardSet.from("4s3s2d")));
    }

    @Test
    public void testFlushDraw() {
        CardSet board = CardSet.from("5c9dAd");
        CardSet isomBoard = CardSet.from("Ah5s9h");

        assertFalse(isomorphicBoardCache.seenAndAddBoard(board));
        assertTrue(isomorphicBoardCache.seenAndAddBoard(board));
        assertTrue(isomorphicBoardCache.seenAndAddBoard(isomBoard));
    }

    @Test
    public void testMonotone() {
        CardSet board = CardSet.from("2c3c4c");
        CardSet isoBoard = CardSet.from("2d3d4d");

        assertFalse(isomorphicBoardCache.seenAndAddBoard(board));
        assertTrue(isomorphicBoardCache.seenAndAddBoard(board));
        assertTrue(isomorphicBoardCache.seenAndAddBoard(isoBoard));
    }
    @Test
    public void testNumberOfBoards() {
        ICombinatoricsVector<Card> initialVector = Factory.createVector(Card.allOf());
        Generator<Card> boardGenerator = Factory.createSimpleCombinationGenerator(initialVector, NumberOfCards);

        for (ICombinatoricsVector<Card> comb : boardGenerator) {
            CardSet board = new CardSet(comb.getVector());

            isomorphicBoardCache.seenAndAddBoard(board);
        }

        assertEquals(NumberOfIsomorphicBoards, isomorphicBoardCache.size());
    }
}
