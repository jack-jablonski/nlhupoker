package se.hupoker.cards;

import com.google.common.collect.ImmutableList;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A set of distinct {@link Card}s.
 * <p>This implementation is a wrapper on {@link ArrayList}<{@link Card}>
 * that allows no duplicates.  A CardSet's iterator will provide elements
 * in FIFO order -- the order in which the elements were added -- if the
 * instance's shuffle method has not been invoked.
 * <p>It also provides a 52-card deck.
 * <p>Methods not otherwise documented forward to {@link ArrayList}<{@link Card}> or perform
 * as specified by the {@link Set} interface.
 *
 * @author Steve Brecher
 * @version 2006Dec04.0
 */
public class CardSet implements Set<Card> {
    private final List<Card> cards;

    /**
     * @param representation Of form 'AcKd5s' or '4c5c6c7c2d'
     * @return Corresponding Cardset class representation
     */
    public static CardSet from(String representation) {
        CardSet board = new CardSet();
        int numCards = representation.length() / 2;

        for (int i = 0; i < numCards; i++) {
            String cardRepresentation = representation.substring(i * 2, i * 2 + 1 + 1);
            Card card = Card.from(cardRepresentation);

            board.add(card);
        }
        return board;
    }

    public CardSet() {
        cards = new ArrayList<>();
    }

    public CardSet(ImmutableList<Card> list) {
        cards = list;
    }

    public CardSet(List<Card> list) {
        cards = new ArrayList<>(list);
    }

    public CardSet(int initialCapacity) {
        cards = new ArrayList<>(initialCapacity);
    }

    /**
     * Copy constructor
     */
    public CardSet(CardSet source) {
        cards = new ArrayList<>(source.cards);
    }

    /**
     * For ease of use, supply to be able to get at(0), at(1), at(2) for the flop. May
     * not depend on internal ordering of the cards except that it stays constant.
     *
     * @return Card at given position
     */
    public Card at(int position) {
        return cards.get(position);
    }

    /**
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean add(Card c) {
        checkArgument(!contains(c));
        return cards.add(c);
    }

    /**
     * Returns <code>true</code> if this CardSet changed as a result of the call.
     *
     * @return <code>true</code> if this CardSet changed as a result of the call; <code>false</code>
     *         if all of the Cards in the specified Collection were already present in this CardSet.
     */
    public boolean addAll(Collection<? extends Card> coll) {
        boolean result = false;
        for (Card c : coll) {
            result |= add(c);
        }
        return result;
    }

    public void clear() {
        cards.clear();
    }

    public boolean contains(Object o) {
        return cards.contains(o);
    }

    public boolean containsAll(Collection<?> coll) {
        return cards.containsAll(coll);
    }

    public boolean containsAny(Collection<?> coll) {
        for (Object obj : coll) {
            if (contains(obj)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Set<?>) || ((Set) that).size() != cards.size()) {
            return false;
        }

        Set<?> other = (Set<?>) that;
        return this.containsAll(other) && other.containsAll(this);
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Card card : cards) {
            result += card.hashCode();
        }
        return result;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    public boolean remove(Object o) {
        return cards.remove(o);
    }

    public boolean removeAll(Collection<?> coll) {
        return cards.removeAll(coll);
    }

    public boolean retainAll(Collection<?> coll) {
        return cards.retainAll(coll);
    }

    public int size() {
        return cards.size();
    }

    public Card[] toArray() {
        return cards.toArray(new Card[cards.size()]);
    }

    public <Card> Card[] toArray(Card[] a) {
        return cards.toArray(a);
    }

    /**
     * @return a {@link String} containing a comma-space-separated list of cards,
     *         each the result of {@link Card#toString()}.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Card card : cards) {
            sb.append(card);
        }

        return sb.toString();
    }
}