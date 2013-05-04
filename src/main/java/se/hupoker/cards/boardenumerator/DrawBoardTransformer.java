package se.hupoker.cards.boardenumerator;

import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Suit;

import java.util.*;

/**
 *
 * @author Alexander Nyberg
 */
class DrawBoardTransformer extends BoardTransformer {
    /**
     * @return Isomorphic suit pattern of lowest form (according to the natural order of {@link Suit})
     */
    @Override
    public CardSet getIsomorphic(CardSet original) {
        Map<Suit, Suit> oldToNewSuits = new EnumMap<>(Suit.class);
        Iterator<Suit> lowestSuit = getSortedSuits().iterator();

        SortedSet<Card> sorted = new TreeSet<>(original);
        for (Card card : sorted) {
            Suit oldSuit = card.suitOf();

            if (!oldToNewSuits.containsKey(oldSuit)) {
                Suit next = lowestSuit.next();
                oldToNewSuits.put(oldSuit, next);
            }
        }

        CardSet isomorphic = new CardSet(sorted.size());
        for (Card card : sorted) {
            isomorphic.add(Card.from(card.rankOf(), oldToNewSuits.get(card.suitOf())));
        }

        return isomorphic;
    }
}