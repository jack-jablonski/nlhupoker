package se.hupoker.common;

import java.util.*;

/**
 * @author Alexander Nyberg
 *
 * @param <T>
 */
public class StreetHistory<T> {
	private final Map<Street, List<T>> map = new EnumMap<>(Street.class);

	public StreetHistory() {
		for (Street st : Street.values()) {
			map.put(st, new LinkedList<T>());
		}
	}

	public List<T> get(Street st) {
		return Collections.unmodifiableList(map.get(st));
	}

	public void add(Street st, T elem) {
		List<T> list = map.get(st);
		list.add(elem);
	}
}
