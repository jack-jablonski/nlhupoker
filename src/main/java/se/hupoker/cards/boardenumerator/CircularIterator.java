package se.hupoker.cards.boardenumerator;

import java.util.Iterator;
import java.util.SortedSet;

/**
 * Iterator that ALWAYS has a next element!
 *
 * @author Alexander Nyberg
 *
 */
public class CircularIterator<T> implements Iterable<T>, Iterator<T> {
    /**
     * SortedSet to guarantee iteration order is same every time.
     */
    private final SortedSet<T> sortedSet;
    private Iterator<T> iterator;

    public CircularIterator(SortedSet<T> set) {
        sortedSet = set;
        iterator = sortedSet.iterator();
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            iterator = sortedSet.iterator();
            return iterator.next();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}