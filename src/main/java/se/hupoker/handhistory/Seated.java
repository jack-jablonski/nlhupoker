package se.hupoker.handhistory;

import se.hupoker.cards.HoleCards;

import java.math.BigDecimal;

/**
 * A seated player in a poker game.
 * 
 * @author Alexander Nyberg
 *
 */
public class Seated {
	private final String name;
	private final BigDecimal stack;
	private HoleCards hand;

	public Seated(String name, BigDecimal stack) {
		this.name = name;
		this.stack = stack;
	}

    public String getName() {
        return name;
    }

    public BigDecimal getStack() {
        return stack;
    }

    public HoleCards getHand() {
        return hand;
    }

    public void setHand(HoleCards hole) {
        this.hand = hole;
    }
}