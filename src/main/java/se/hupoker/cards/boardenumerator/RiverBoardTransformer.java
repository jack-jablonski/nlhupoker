package se.hupoker.cards.boardenumerator;

import se.hupoker.common.EnumCounter;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Rank;
import se.hupoker.cards.Suit;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * When there are no more boards to come (meaning for river).
 *
 * @author Alexander Nyberg
 */
class RiverBoardTransformer extends BoardTransformer {
    private final BoardTransformer flushTransform = new DrawBoardTransformer();

    @Override
    public CardSet getIsomorphic(CardSet board) {
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
    private CardSet flushIsomorphic(CardSet board) {
        return flushTransform.getIsomorphic(board);
    }

    /**
     * Perfect compression for no-flush-possible boards.
     *
     * @param board Does not have a possible flush.
     * @return The same suit pattern for every rank-similar board.
     */
    private CardSet rainbowIsomorphic(CardSet board) {
        CardSet isomorphic = new CardSet(board.size());
        Iterator<Suit> suitIterator = new CircularIterator<>(getSortedSuits());

        SortedSet<Card> sorted = new TreeSet<>(board);
        for (Card card : sorted) {
            Rank rank = card.rankOf();

            isomorphic.add(Card.from(rank, suitIterator.next()));
        }

        return isomorphic;
    }
}