package se.hupoker.common;

import java.util.Collections;
import java.util.EnumMap;

/**
 * @author Alexander Nyberg
 */
public class EnumCounter<K extends Enum<K>> {
    private final EnumMap<K, Integer> counter;

    public EnumCounter(Class<K> clazz) {
        counter = new EnumMap<>(clazz);
    }

    public int get(K k) {
        return counter.get(k);
    }

    public int maximum() {
        return Collections.max(counter.values());
    }

    /**
     *
     * @return Count after increment.
     */
    public int increment(K k) {
        if (counter.containsKey(k)) {
            int nextValue = counter.get(k) + 1;
            counter.put(k, nextValue);
        } else {
            counter.put(k, 1);
        }

        return counter.get(k);
    }
}