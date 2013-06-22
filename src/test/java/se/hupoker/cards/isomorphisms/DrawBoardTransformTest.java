package se.hupoker.cards.isomorphisms;

import org.junit.Test;
import se.hupoker.cards.CardSet;
import se.hupoker.common.Street;

import static org.junit.Assert.assertEquals;

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
}