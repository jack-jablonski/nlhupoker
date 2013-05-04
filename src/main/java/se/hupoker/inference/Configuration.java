package se.hupoker.inference;

/**
 * @author Alexander Nyberg
 */
public class Configuration {
    public final static boolean DEBUG = true;

    public final static int OptimizationIterations = 44;

    public void println(String string) {
        if (Configuration.DEBUG) {
            System.out.println(string);
        }
    }
}