package se.hupoker.poker;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * @author Alexander Nyberg
 */
public enum Betting {
    /**
     * Check/Bet. Always this state if amount to call is zero.
     */
    CB {
        @Override
        public Set<ActionClassifier> available() {
            return ImmutableSet.of(ActionClassifier.CHECK, ActionClassifier.BET);
        }
    },
    /**
     * Fold/Call/Raise
     */
    FCR {
        @Override
        public Set<ActionClassifier> available() {
            return ImmutableSet.of(ActionClassifier.FOLD, ActionClassifier.CALL, ActionClassifier.RAISE);
        }
    };

    /**
     * @return The possible actions under given circumstance.
     */
    public abstract Set<ActionClassifier> available();

    public static Betting get(ActionClassifier classifier) {
        for (Betting betting : Betting.values()) {
            if (betting.available().contains(classifier)) {
                return betting;
            }
        }

        throw new IllegalArgumentException();
    }
}