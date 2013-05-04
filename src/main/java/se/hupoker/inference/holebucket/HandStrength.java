package se.hupoker.inference.holebucket;

/**
 * Qualitative strength of some hand.
 *
 * @author Alexander Nyberg
 */
enum HandStrength {
    NUT,
    TOP,
    STRONG,
    MID,
    WEAK,
    NONE;

    /**
     * @param other
     * @return this stronger than or equal strength to other
     */
    public boolean strongerOrEqual(HandStrength other) {
        return this.ordinal() <= other.ordinal();
    }
}