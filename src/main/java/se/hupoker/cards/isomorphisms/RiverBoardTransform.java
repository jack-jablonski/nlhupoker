package se.hupoker.cards.isomorphisms;

import se.hupoker.common.EnumCounter;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Suit;
import se.hupoker.common.HarshMap;

import java.util.*;

/**
 * When there are no more boards to come (meaning for river).
 *
 * @author Alexander Nyberg
 */
class RiverBoardTransform extends BoardTransform {
    @Override
    protected Map<Suit, Suit> getSuitMapping(CardSet board) {
        EnumCounter<Suit> suitCounter = new EnumCounter<>(Suit.class);

        for (Card card : board) {
            suitCounter.increment(card.suitOf());
        }

        if (suitCounter.maximum() >= 3) {
            return flushIsomorphic(board);
        } else {
            return rainbowIsomorphic(board);
        }
    }

    /**
     *
     * Strategy should be to find out what suit is the flush and replace all others using
     * the circularIterator with flush suit removed.
     *
     * We could skip some boards here as a minor optimization.
     *
     * @param board Contains completed flush draw.
     * @return
     */
    private Map<Suit, Suit> flushIsomorphic(CardSet board) {
        DrawBoardTransform drawBoardTransformer = new DrawBoardTransform();
        return drawBoardTransformer.getSuitMapping(board);
    }

    /**
     * Perfect compression for no-flush-possible boards.
     *
     * @param board Does not have a possible flush.
     * @return The same suit pattern for every rank-similar board
     */
    private Map<Suit, Suit> rainbowIsomorphic(CardSet board) {
        Iterator<Suit> suitIterator = new CircularIterator<>(getSortedSuits());
        Map<Suit, Suit> map = new HarshMap<>();

        for (Card card : board) {
            Suit suit = card.suitOf();

            if (!map.containsKey(suit)) {
                map.put(suit, suitIterator.next());
            }
        }

        for (Suit suit : Suit.allOf()) {
            if (!map.containsKey(suit)) {
                map.put(suit, suitIterator.next());
            }
        }

        return map;
    }
}