package se.hupoker.inference.actiondistribution;

import java.util.EnumSet;

/**
 *
 * @author Alexander Nyberg
 *
 */
public enum ActionDistOptions {
    NOFOLD;

    public static EnumSet<ActionDistOptions> empty() {
		return EnumSet.noneOf(ActionDistOptions.class);
	}
}
