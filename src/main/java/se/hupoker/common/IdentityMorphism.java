package se.hupoker.common;

/**
 * @author Alexander Nyberg
 */
public class IdentityMorphism<T> implements Endomorphism<T> {
    @Override
    public T apply(T input) {
        return input;
    }
}