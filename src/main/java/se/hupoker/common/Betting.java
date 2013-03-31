package se.hupoker.common;

import java.util.EnumSet;

/**
 * @author Alexander Nyberg
 */
public enum Betting {
    /**
     * Check/Bet. Always this state if amount to call is zero.
     */
    CB {
        @Override
        public EnumSet<ActionClassifier> available() {
            return EnumSet.of(ActionClassifier.CHECK, ActionClassifier.BET);
        }
    },
    /**
     * Fold/Call/Raise
     */
    FCR {
        @Override
        public EnumSet<ActionClassifier> available() {
            return EnumSet.of(ActionClassifier.FOLD, ActionClassifier.CALL, ActionClassifier.RAISE);
        }
    };

    public abstract EnumSet<ActionClassifier> available();

    public static Betting get(ActionClassifier classifier) {
        for (Betting betting : Betting.values()) {
            if (betting.available().contains(classifier)) {
                return betting;
            }
        }

        throw new IllegalArgumentException();
    }
}