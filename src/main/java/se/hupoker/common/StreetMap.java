package se.hupoker.common;

import java.util.EnumMap;

/**
 * 
 * @author Alexander Nyberg
 *
 * @param <T>
 */
public class StreetMap<T> extends EnumMap<Street, T> {
	public StreetMap() {
		super(Street.class);
	}
}