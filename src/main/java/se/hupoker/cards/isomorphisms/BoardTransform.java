package se.hupoker.cards.isomorphisms;

import com.google.common.collect.ImmutableSortedSet;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.Suit;
import se.hupoker.common.Endomorphism;
import se.hupoker.common.Street;

import java.util.*;

import static se.hupoker.common.Street.RIVER;

/**
 * Provides a function that returns an isomorphic board wrt. ordering of
 * cards & suits.
 *
 * @author Alexander Nyberg
 */
public abstract class BoardTransform {
    /**
     * @param street
     * @return The transformer for board.
     */
    public static BoardTransform create(Street street) {
        if (street == RIVER) {
            return new RiverBoardTransform();
        } else {
            return new DrawBoardTransform();
        }
    }

    protected SortedSet<Suit> getSortedSuits() {
        return ImmutableSortedSet.copyOf(Suit.allOf());
    }

    /**
     * @return Suit mapping used to transform board & hole cards.
     */
    protected abstract Map<Suit, Suit> getSuitMapping(CardSet board);

    /**
     * @return The isomorphic equivalent of 'board'
     */
    public CardSet getIsomorphic(CardSet board) {
        Map<Suit, Suit> map = getSuitMapping(board);
        List<Card> cards = new ArrayList<>();

        for (Card card : board) {
            Card newCard = Card.from(card.rankOf(), map.get(card.suitOf()));
            cards.add(newCard);
        }

        Collections.sort(cards);

        return new CardSet(cards);
    }
}