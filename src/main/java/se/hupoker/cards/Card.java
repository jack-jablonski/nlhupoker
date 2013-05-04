package se.hupoker.cards;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * An immutable poker card.
 *
 * @author Steve Brecher
 * @author Alexander Nyberg
 * @version 2006Dec05.0
 */
public class Card implements Comparable<Card> {
    public static final int NumberOfCards = 52;
    private static final CardTable table = new CardTable();
    private final Rank rank;
    private final Suit suit;

    /**
     * Constructs a card of the specified rank and suit.
     *
     * @param rank a {@link Rank}
     * @param suit a {@link Suit}
     */
    private Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * @param rank
     * @param suit
     * @return Immutable existing card from table.
     */
    public static Card from(Rank rank, Suit suit) {
        return table.get(rank, suit);
    }


    /**
     * Constructs a card of the specified rank and suit.
     *
     * @param rs a {@link String} of length 2, where the first character is in {@link Rank#RANK_CHARS} and
     *           the second is in {@link Suit#SUIT_CHARS} (case insensitive).
     * @throws IllegalArgumentException on the first character in rs which is not found in the respective string.
     */
    public static Card from(String rs) {
        checkArgument(rs.length() == 2, "'" + rs + "'");

        Rank rank = Rank.fromChar(rs.charAt(0));
        Suit suit = Suit.fromChar(rs.charAt(1));
        return Card.from(rank, suit);
    }

    /**
     * @return Complete space of cards.
     */
    public static Collection<Card> allOf() {
        return table.allOf();
    }

    /**
     * @return a {@link String} of length 2 containing a character in {@link Rank#RANK_CHARS} denoting this
     *         card&#39;s rank followed by a character in {@link Suit#SUIT_CHARS} denoting this
     *         card&#39;s suit.
     */
    @Override
    public String toString() {
        return String.format("%c%c", rank.toChar(), suit.toChar());
    }

    /**
     * Returns the {@link Rank} of this card.
     *
     * @return the {@link Rank} of this card.
     */
    public Rank rankOf() {
        return rank;
    }

    /**
     * Returns the {@link Suit} of this card.
     *
     * @return the {@link Suit} of this card.
     */
    public Suit suitOf() {
        return suit;
    }

    /**
     * Compares the parameter to this card.
     *
     * @return <code>true</code> if the parameter is a {@link Card} of the same rank and suit
     *         as this card; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Card)) {
            return false;
        }

        final Card otherCard = (Card) other;
        return ordinal() == otherCard.ordinal();
    }

    @Override
    public int hashCode() {
        return ordinal();
    }

    /**
     * @return Unique integer representation of this Card.
     */
    public int ordinal() {
        return rankOf().ordinal() * 4 + suitOf().ordinal();
    }

    @Override
    public int compareTo(Card other) {
        if (this.ordinal() < other.ordinal()) {
            return -1;
        } else if (this.ordinal() == other.ordinal()) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Essentially turning Card into an enum.
     */
    private static class CardTable {
        private final ImmutableTable<Rank, Suit, Card> table;

        public Card get(Rank rank, Suit suit) {
            return table.get(rank, suit);
        }

        public Collection<Card> allOf() {
            return table.values();
        }

        private CardTable() {
            Table<Rank, Suit, Card> localTable = ArrayTable.create(Rank.allOf(), Suit.allOf());

            for (Suit s : Suit.values()) {
                for (Rank r : Rank.values()) {
                    localTable.put(r, s, new Card(r, s));
                }
            }

            table = ImmutableTable.copyOf(localTable);
        }
    }
}