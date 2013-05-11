package se.hupoker.cards.isomorphisms;

import com.google.common.collect.ImmutableSortedSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.isomorphisms.CircularIterator;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Suit;

import java.util.*;

/**
 * @author Alexander Nyberg
 */
public class IsomorphicHole {
    private final CardSet board;
    private final SortedSet<Suit> immutableSuits;

    public IsomorphicHole(CardSet board) {
        this.board = board;
        immutableSuits = ImmutableSortedSet.copyOf(Suit.values());
    }

    private Map<Suit, Suit> getSuitMap(CardSet original) {
        Map<Suit, Suit> map = new EnumMap<>(Suit.class);
        Iterator<Suit> it = immutableSuits.iterator();

        SortedSet<Card> sorted = new TreeSet<>(original);
        for (Card card : sorted) {
            Suit suit = card.suitOf();

            if (!map.containsKey(suit)) {
                Suit next = it.next();
                map.put(suit, next);
            }
        }
        return map;
    }

    /**
     *
     * @param original
     * @return The isomorphic equivalent wrt. board
     */
    public HoleCards getIsomorphic(HoleCards original) {
        Map<Suit, Suit> map = getSuitMap(board);
        SortedSet<Suit> remaining = new TreeSet<>(immutableSuits);
        remaining.removeAll(map.values());
        Iterator<Suit> suitIterator = new CircularIterator<>(remaining);

        List<Card> isomorphic = new ArrayList<>(HoleCards.TexasHoleCards);

        // HoleCards guaranteed to be sorted.
        for (Card card : original) {
            Suit newSuit;

            if (map.containsKey(card.suitOf())) {
                newSuit = map.get(card.suitOf());
            } else {
                newSuit = suitIterator.next();
            }

            isomorphic.add(Card.from(card.rankOf(), newSuit));
        }

        HoleCards isoCards = HoleCards.of(isomorphic);
//        System.out.println("Mapped " + original + " -> " + isoCards);
        return isoCards;
    }
}