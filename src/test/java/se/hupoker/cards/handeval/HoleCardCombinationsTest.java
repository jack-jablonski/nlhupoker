package se.hupoker.cards.handeval;

import com.google.common.collect.Lists;
import org.junit.Test;
import se.hupoker.cards.Card;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.Rank;
import se.hupoker.cards.Suit;
import se.hupoker.cards.cache.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Alexander Nyberg
 */
public class HoleCardCombinationsTest {
    private HoleCardCombinations holeCardCombinations = new HoleCardCombinations();
    private List<Card> randomCards = Lists.newArrayList(
            Card.from(Rank.ACE, Suit.SPADE),
            Card.from(Rank.KING, Suit.SPADE),
            Card.from(Rank.QUEEN, Suit.SPADE),
            Card.from(Rank.JACK, Suit.SPADE)
    );

    @Test (expected = IllegalArgumentException.class)
    public void failsOnEmptySet() {
        holeCardCombinations.get(new ArrayList<Card>());
    }

    @Test
    public void allCombinationsExist() {
        Set<Pair<HoleCards>> pairSet = holeCardCombinations.get(randomCards);
        assertTrue(pairSet.size() == 3);
    }
}