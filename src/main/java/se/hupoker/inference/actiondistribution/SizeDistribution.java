package se.hupoker.inference.actiondistribution;

import se.hupoker.inference.vectors.Distribution;

/**
 * 
 * @author Alexander Nyberg
 *
 */
public class SizeDistribution extends Distribution {
	private static final int NUMBUCKETS = 5;
	
	public SizeDistribution() {
		super(NUMBUCKETS);
	}

	/**
	 * pot bet => 
	 * absurd allin => 1
	 * 
	 * @param pot
	 * @param tocall
	 * @return (0,1] map 
	 */
	private double map(double pot, double tocall) {
		return tocall / (pot + tocall);
	}

	public double getProbability(double pot, double tocall) {
		return 0.2;
	}
}
