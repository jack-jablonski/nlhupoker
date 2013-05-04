package se.hupoker.inference.vectors;

import se.hupoker.inference.handinformation.HolePossible;
import se.hupoker.cards.HoleCards;


/**
 * 
 * @author Alexander Nyberg
 *
 */
public class HoleDistribution extends Distribution {
	/**
	 * Distribution with all impossible combinations removed.
	 * 
	 * @param possible
	 */
	public HoleDistribution(HolePossible possible) {
		super(HoleCards.TexasCombinations);

        setAll(0.0);

        // Normalize afterwards so 1.0 or whatever
		for (HoleCards hole : possible) {
            set(hole.ordinal(), 1.0);
        }

		normalize();
	}
}