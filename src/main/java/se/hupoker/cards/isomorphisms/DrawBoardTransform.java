package se.hupoker.cards.isomorphisms;

import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Suit;
import se.hupoker.common.HarshMap;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Alexander Nyberg
 */
class DrawBoardTransform extends BoardTransform {
    @Override
    public Map<Suit, Suit> getSuitMapping(CardSet board) {
        final Map<Suit, Suit> map = new HarshMap<>();
        Iterator<Suit> lowestSuit = getSortedSuits().iterator();

        SortedSet<Card> sorted = new TreeSet<>(board);
        for (Card card : sorted) {
            Suit oldSuit = card.suitOf();

            if (!map.containsKey(oldSuit)) {
                Suit next = lowestSuit.next();
                map.put(oldSuit, next);
            }
        }

        for (Suit suit : Suit.allOf()) {
            if (!map.containsKey(suit)) {
                map.put(suit, lowestSuit.next());
            }
        }

        return map;
    }
}