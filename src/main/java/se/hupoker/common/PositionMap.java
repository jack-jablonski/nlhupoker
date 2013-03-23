package se.hupoker.common;

import java.util.EnumMap;


/**
 * 
 * @author Alexander Nyberg
 *
 * @param <T> Type to store for each Position element.
 */
public class PositionMap<T> extends EnumMap<Position, T> {
	public PositionMap() {
		super(Position.class);
	}

    /**
     *
     * @param initial Better be immutable bitch!
     */
    public PositionMap(T initial) {
        this();

        for (Position pos : Position.values()) {
            put(pos, initial);
        }
    }

    /**
     *
     * @param clazz Initialize to default constructor of this class.
     */
    public PositionMap(Class<T> clazz) {
        this();

        for (Position pos : Position.values()) {
            try {
                T instance = clazz.newInstance();
                put(pos, instance);
            } catch (Exception e) {
                throw new AssertionError("Could not create default object from " + clazz.getName());
            }
        }
    }
}