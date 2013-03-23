package se.hupoker.common;

/**
 * @author Alexander Nyberg
 */
final public class Computation {
    static public int units() {
        return Runtime.getRuntime().availableProcessors();
    }
}