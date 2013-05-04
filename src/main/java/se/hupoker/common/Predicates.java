package se.hupoker.common;

/**
 * @author Alexander Nyberg
 */
public class Predicates {
    public static boolean allTrue(boolean ...values) {
        boolean value = true;

        for (boolean bool : values) {
            value &= bool;
        }

        return value;
    }
}