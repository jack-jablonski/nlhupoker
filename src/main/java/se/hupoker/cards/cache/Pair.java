package se.hupoker.cards.cache;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 
 * @author Alexander Nyberg
 *
 * @param <T>
 */
public class Pair<T> {
	private final T first, second;

    public Pair(T one, T two) {
        checkNotNull(one);
        checkNotNull(two);
        first = one;
        second = two;
    }

    public T getFirst() { return first; }

    public T getSecond() { return second; }
}