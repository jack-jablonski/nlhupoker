package se.hupoker.cards.boardenumerator;

import org.junit.Before;
import org.junit.Test;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import se.hupoker.common.Street;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Nyberg
 */
public class IsomorphicFlopTest {
    private IsomorphicBoard isomorphicBoard;
    private final int NumberOfCards = Street.FLOP.numberOfBoardCards();
    private final int NumberOfIsomorphicBoards = 1833;

    @Before
    public void setUp() throws Exception {
        isomorphicBoard = new IsomorphicBoard(Street.FLOP);
    }

    @Test
    public void testRainbow() {
        CardSet board = CardSet.from("2c3d4h");
        CardSet isoBoard = CardSet.from("2d3h4s");

        assertFalse(isomorphicBoard.seenAndAddBoard(board));
        assertTrue(isomorphicBoard.seenAndAddBoard(board));
        assertTrue(isomorphicBoard.seenAndAddBoard(isoBoard));
        assertTrue(isomorphicBoard.seenAndAddBoard(CardSet.from("4d3h2s")));
        assertTrue(isomorphicBoard.seenAndAddBoard(CardSet.from("3s4h2d")));
        assertFalse(isomorphicBoard.seenAndAddBoard(CardSet.from("4s3s2d")));
    }

    @Test
    public void testFlushDraw() {
        CardSet board = CardSet.from("5c9dAd");
        CardSet isomBoard = CardSet.from("Ah5s9h");

        assertFalse(isomorphicBoard.seenAndAddBoard(board));
        assertTrue(isomorphicBoard.seenAndAddBoard(board));
        assertTrue(isomorphicBoard.seenAndAddBoard(isomBoard));
    }

    @Test
    public void testMonotone() {
        CardSet board = CardSet.from("2c3c4c");
        CardSet isoBoard = CardSet.from("2d3d4d");

        assertFalse(isomorphicBoard.seenAndAddBoard(board));
        assertTrue(isomorphicBoard.seenAndAddBoard(board));
        assertTrue(isomorphicBoard.seenAndAddBoard(isoBoard));
    }
    @Test
    public void testNumberOfBoards() {
        ICombinatoricsVector<Card> initialVector = Factory.createVector(Card.allOf());
        Generator<Card> boardGenerator = Factory.createSimpleCombinationGenerator(initialVector, NumberOfCards);

        for (ICombinatoricsVector<Card> comb : boardGenerator) {
            CardSet board = new CardSet(comb.getVector());

            isomorphicBoard.seenAndAddBoard(board);
        }

        assertEquals(NumberOfIsomorphicBoards, isomorphicBoard.size());
    }
}
