package se.hupoker.cards;

import java.util.EnumSet;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Steve Brecher
 * @version 2006Dec05.0
 */
public enum Suit {
    CLUB,
    DIAMOND,
    HEART,
    SPADE;

    /**
     * @return the character in {@link #SUIT_CHARS} denoting this suit.
     */
    public char toChar() {
        return SUIT_CHARS.charAt(this.ordinal());
    }

    /**
     * @param c a character present in {@link #SUIT_CHARS} (case insensitive)
     * @return the Suit denoted by the character.
     * @throws IllegalArgumentException if c not in {@link #SUIT_CHARS}
     */
    public static Suit fromChar(char c) {
        int i = SUIT_CHARS.indexOf(Character.toLowerCase(c));
        checkArgument(i >= 0, "'" + c + "'");

        return Suit.values()[i];
    }

    public static EnumSet<Suit> allOf() {
        return EnumSet.allOf(Suit.class);
    }

    private static final String SUIT_CHARS = "cdhs";
}