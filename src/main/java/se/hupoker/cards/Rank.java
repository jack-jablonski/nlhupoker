package se.hupoker.cards;

import java.util.EnumSet;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Steve Brecher
 * @version 2006Dec05.0
 */
public enum Rank {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

    /**
     * @return the character in {@link #RANK_CHARS} denoting this rank.
     */
    public char toChar() {
        return RANK_CHARS.charAt(this.ordinal());
    }

    /**
     * @param c a character present in {@link #RANK_CHARS} (case insensitive)
     * @return the Rank denoted by character.
     * @throws IllegalArgumentException if c not in {@link #RANK_CHARS}
     */
    public static Rank fromChar(char c) {
        int i = RANK_CHARS.indexOf(Character.toUpperCase(c));
        checkArgument(i >= 0, "'" + c + "'");

        return fromOrdinal(i);
    }

    public static Rank fromOrdinal(int ordinal) {
        return Rank.values()[ordinal];
    }

    public static EnumSet<Rank> allOf() {
        return EnumSet.allOf(Rank.class);
    }

    private static final String RANK_CHARS = "23456789TJQKA";
}