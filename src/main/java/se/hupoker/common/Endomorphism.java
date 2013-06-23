package se.hupoker.common;

import com.google.common.base.Function;

/**
 * @author Alexander Nyberg
 */
public interface Endomorphism<T> extends Function<T, T> {
    public T apply(T input);
}