package se.hupoker.cards.boardisomorphisms;

/**
 * 
 * @author Alexander Nyberg
 *
 * @param <T>
 */
public class Pair<T> {
	private final T first, second;

    public T getFirst() { return first; }
    public T getSecond() { return second; }

	public Pair(T one, T two) {
		first = one;
		second = two;
	}
}