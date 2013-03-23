package se.hupoker.common;

/**
 * Headsup positions.
 *
 * @author Alexander Nyberg
 */
public enum Position {
    /**
     * In Position (Small blind)
     */
    IP,
    /**
     * Out of position (Big blind)
     */
    OOP;

    public static int numberOf() {
        return Position.values().length;
    }
}