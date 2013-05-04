package se.hupoker.inference.handinformation;

import java.util.HashMap;

/**
 * 
 * @author Alexander Nyberg
 *
 */
public class PlayerDB extends HashMap<String, Integer> {
	private int currentId = 0;

	public Integer add(String name) {
		if (containsKey(name)) {
			return get(name);
		} else {
			Integer nextValue = ++currentId;

			put(name, nextValue);

			return nextValue;
		}
	}
}