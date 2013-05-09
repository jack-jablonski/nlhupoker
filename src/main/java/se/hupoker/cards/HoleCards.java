package se.hupoker.cards;

import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.google.common.math.IntMath;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.ICombinatoricsVector;


import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * Specialized CardSet:
 * -> Immutable, sorted Texas Holdem hole cards.
 * -> Implements an ordinal.
 *
 * @author Alexander Nyberg
 */
public class HoleCards extends CardSet {
    public static final int TexasHoleCards = 2;
    public static final int TexasCombinations = IntMath.binomial(Card.NumberOfCards, TexasHoleCards);

    /**
     * @param cards May only create HoleCards through immutable lists!
     */
    private HoleCards(ImmutableList<Card> cards) {
        super(cards);
    }

    private static HoleCards factory(Collection<Card> cards) {
        checkArgument(cards.size() == TexasHoleCards);
        List<Card> sorted = new ArrayList<>(cards);
        Collections.sort(sorted);

        return new HoleCards(ImmutableList.copyOf(sorted));
    }

    public static HoleCards of(Card one, Card two) {
        return table.get(one, two);
    }

    public static HoleCards of(List<Card> list) {
        checkArgument(list.size() == TexasHoleCards);
        return of(list.get(0), list.get(1));
    }

    public static HoleCards from(String rep) {
        checkArgument(rep.length() == 4);
        Card one = Card.from(rep.substring(0, 2));
        Card two = Card.from(rep.substring(2, 4));

        return of(one, two);
    }

    public Card first() {
        return at(0);
    }

    public Card last() {
        return at(1);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof HoleCards)) {
            return false;
        }

        final HoleCards otherCards = (HoleCards) other;
        return first().equals(otherCards.first()) && last().equals(otherCards.last());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(first(), last());
    }

    /**
     * In a true Collection-oriented solution this method would not be required. The real world
     * however looks different. By far the biggest user is the LUT.
     *
     * f: HoleCards -> [0, TexasCombinations)
     *
     * @return Unique index of this.
     */
    public int ordinal() {
        return indexMap.get(this);
    }

    /**
     * @return Complete space of texas holdem hole card combinations.
     */
    public static Collection<HoleCards> allOf() {
        return indexMap.keySet();
    }

    /**
     * Builds on underlying hashSet & Equals. Somewhat of a memory optimization...
     */
    private static final Table<Card, Card, HoleCards> table = HashBasedTable.create();
    /**
     * Create unique Map: HoleCards -> unique integer.
     */
    private static final Map<HoleCards, Integer> indexMap = new HashMap<>();

    static {
        DeckSet deck = DeckSet.freshDeck();
        int currentIndex = 0;

        ICombinatoricsVector<Card> initialVector = Factory.createVector(deck);
        for (ICombinatoricsVector<Card> holeCombination : Factory.createSimpleCombinationGenerator(initialVector, TexasHoleCards)) {
            final HoleCards hole = factory(holeCombination.getVector());

            table.put(hole.first(), hole.last(), hole);
            table.put(hole.last(), hole.first(), hole);

            indexMap.put(hole, currentIndex);

            currentIndex++;
        }
        checkState(indexMap.size() == TexasCombinations);
    }
}