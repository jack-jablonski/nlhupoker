package se.hupoker.inference.states;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Alexander Nyberg
 */
class StatePath {
    private StatePath() {}

    private static InputStreamReader get(String where) {
//        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(where);
        InputStream in = StatePath.class.getResourceAsStream(where);
        return new InputStreamReader(in);
    }

    protected static String getPreflopPath() {
        return "Configuration/preflopstate.yml";
    }

    protected static InputStreamReader getPreflop() {
        return get(getPreflopPath());
    }

    protected static String getPostFlopPath() {
        return "Configuration/postflopstate.yml";
    }

    protected static InputStreamReader getPostFlop() {
        return get(getPostFlopPath());
    }
}