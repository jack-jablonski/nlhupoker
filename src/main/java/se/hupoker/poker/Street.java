package se.hupoker.poker;

import java.util.EnumSet;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Texas Holdem street.
 *
 * @author Alexander Nyberg
 */
public enum Street {
    /*
     * Defined in specific order.
     */
	PREFLOP,
    FLOP,
    TURN,
    RIVER;

	/**
	 * 
	 * @return First street (always preflop).
	 */
	public static Street first() {
		return PREFLOP;
	}

    public static EnumSet<Street> allOf() {
        return EnumSet.allOf(Street.class);
    }

    public boolean hasNext() {
        return this != RIVER;
    }

    /**
	 * 
	 * @return The next street.
	 */
	public Street next() {
		if (this == PREFLOP) {
			return FLOP;
		} else if (this == FLOP) {
			return TURN;
		} else if (this == TURN) {
			return RIVER;
		} else {
			throw new IllegalArgumentException("No street after river!");
		}
	}

    public boolean equals(Street street) {
        return this == street;
    }

    /**
     *
     * @return Total number of cards on board for this street.
     */
    public int numberOfBoardCards() {
        switch (this) {
            case FLOP : return 3;
            case TURN : return 4;
            case RIVER : return 5;
        }

        throw new IllegalArgumentException("Don't call for no-board-streets");
    }

	/**
	 * @return The number of new board cards for this street.
	 */
	public int numberOfNewBoardCards() {
		switch (this) {
			case FLOP : return 3;
			case TURN : return 1;
			case RIVER : return 1;
		}

		throw new IllegalArgumentException("Don't call for no-board-streets");
	}
}