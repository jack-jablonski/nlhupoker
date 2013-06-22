package se.hupoker.cards.isomorphisms;

import org.junit.Test;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Suit;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Nyberg
 */
public class RiverBoardTransformerTest {
    private RiverBoardTransform transform = new RiverBoardTransform();

    @Test
    public void suitMappingForRainbowMapsAllSuits() {
        Map<Suit, Suit> map = transform.getSuitMapping(CardSet.from("2c7dKhKd5h"));

        assertTrue(map.keySet().containsAll(Suit.allOf()));
    }

    @Test
    public void suitMappingForFlushDrawMapsAllSuits() {
        Map<Suit, Suit> map = transform.getSuitMapping(CardSet.from("2c7dKdKc5c"));

        assertTrue(map.keySet().containsAll(Suit.allOf()));
    }
}