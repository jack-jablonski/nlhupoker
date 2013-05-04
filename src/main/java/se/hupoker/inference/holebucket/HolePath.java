package se.hupoker.inference.holebucket;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Alexander Nyberg
 */
class HolePath {
    private static InputStreamReader toStream(String where) {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(where);
        return new InputStreamReader(in);
    }

    protected static String getPreflopPath() {
        return "Configuration/preflophole/big.yml";
    }

    private static InputStreamReader getPreflop() {
        return toStream(getPreflopPath());
    }

    protected static String getFlopPath() {
        return "Configuration/flophole.yml";
    }

    private static InputStreamReader getFlop() {
        return toStream(getFlopPath());
    }

    protected static String getTurnPath() {
        return "Configuration/turnhole.yml";
    }

    private static InputStreamReader getTurn() {
        return toStream(getTurnPath());
    }

    protected static String getRiverPath() {
        return "Configuration/riverhole.yml";
    }

    private static InputStreamReader getRiver() {
        return toStream(getRiverPath());
    }
}