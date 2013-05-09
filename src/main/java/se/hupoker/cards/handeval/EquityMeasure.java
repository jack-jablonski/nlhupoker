package se.hupoker.cards.handeval;

import se.hupoker.cards.HoleCards;

import java.io.Serializable;

/**
* 
* @author Alexander Nyberg
*
*/
class EquityMeasure {
	public static final double BADEQUITY = Float.NaN;
	public static final double AHEAD=1, TIED=0.5, BEHIND=0;

	protected double getEHS(double now[], double equity[]) {
		final double hs = getHS(now);

		return hs + (1-hs) * getPPOT(now, equity) - hs * getNPOT(now, equity);
	}

    /**
     *
     * @return Average equity against the hand represented in equity
     */
	protected static double getEquity(double equity[]) {
		double size = 0;
		double sum = 0;

		for (double eq : equity) {
			if (!Double.isNaN(eq)) {
				sum += eq;
				size++;
			}
		}

		if (size > 0) {
			return sum / size;
		} else {
			return BADEQUITY;
		}
	}
	
	/**
	 * 
	 * @return Current "hand strength".
	 */
    protected static double getHS(double now[]) {
		double size = 0;
		double summ = 0;

		for (double d : now) {
			if (Double.isNaN(d)) {
				continue;
			}

			summ += d;
			size++;
		}

		if (size > 0) {
			return summ / size;
		} else {
			return BADEQUITY;
		}
	}
	
	/**
	 * 
	 * @return Average equity against the hands we're behind now against.
	 */
    protected static double getPPOT(double now[], double equity[]) {
		double normalizer = 0;
		double equitySum = 0;

		for (int i=0; i < HoleCards.TexasCombinations; i++) {
			if (now[i] == BEHIND) {
				normalizer++;
				equitySum += equity[i];
			}

			if (now[i] == TIED) {
				normalizer++;
				equitySum += equity[i] /2;
			}
		}

		if (normalizer > 0) {
			return equitySum / normalizer;
		} else {
			return 0;
		}
	}

    /**
     *
     * @return Average equity against the hands we're ahead right now against.
     */
	protected static double getNPOT(double now[], double equity[]) {
		double normalizer = 0;
		double equitySum = 0;

		for (int i=0; i < HoleCards.TexasCombinations; i++) {
			if (now[i] == AHEAD) {
				normalizer++;
				equitySum += (1 - equity[i]);
			}

			if (now[i] == TIED) {
				normalizer++;
				equitySum += (1-equity[i]) / 2;
			}
		}

		if (normalizer > 0) {
			return equitySum / normalizer;
		} else {
			return 0;
		}
	}
}