package se.hupoker.common;

/**
 * @author Alexander Nyberg
 */
public enum Betting {
    /**
     * Check/Bet. Always this state if amount to call is zero.
     */
    CB,
    /**
     * Fold/Call/Raise
     */
    FCR;

    public static Betting fromToCall(float toCall) {
        return (toCall > 0) ? FCR : CB;
    }
}