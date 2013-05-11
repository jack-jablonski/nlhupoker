package se.hupoker.cards.handeval;

import static com.google.common.base.Preconditions.checkArgument;

/**
* 
* @author Alexander Nyberg
*
*/
class EquityMeasure {
	public static final float BADEQUITY = Float.NaN;
    public static double AHEAD = 1, TIED = 0.5, BEHIND = 0;

    private static enum Now {
        Ahead(1.0),
        Tied(0.5),
        Behind(0.0);

        private final double equity;

        private Now(double equity) {
            this.equity = equity;
        }

        public double getEuity() {
            return equity;
        }
    }

    private static Now getNow(float equity) {
        if (equity > 0.5) {
            return Now.Ahead;
        } else if (equity == 0.5) {
            return Now.Tied;
        } else {
            return Now.Behind;
        }
    }

    /**
     * @return Average equity against the hand represented in equity
     */
	protected static float getEquity(float equity[]) {
        float size = 0;
        double equitySum = 0;

		for (double eq : equity) {
			if (!Double.isNaN(eq)) {
                equitySum += eq;
				size++;
			}
		}

        checkArgument(size > 0);
        return (float) (equitySum / size);
	}

    protected static float getApproximateHs(float equity[]) {
        float size = 0;
        double aheadSum = 0;

		for (float d : equity) {
			if (Double.isNaN(d)) {
				continue;
			}

			aheadSum += getNow(d).getEuity();
			size++;
		}

        checkArgument(size > 0);
        return (float) (aheadSum / size);
	}
	
	/**
	 * 
	 * @return Average equity against the hands we're behind now against.
	 */
    protected static float getApproximatePpot(float equities[]) {
		double normalizer = 0;
		double equityWhenBehindSum = 0;

        for (float equity : equities) {
            if (getNow(equity) == Now.Behind) {
                normalizer++;
                equityWhenBehindSum += equity;
            }

            if (getNow(equity) == Now.Tied) {
                normalizer++;
                equityWhenBehindSum += equity /2;
            }
        }

		if (normalizer > 0) {
			return (float) (equityWhenBehindSum / normalizer);
		} else {
			return 0;
		}
	}

    /**
     *
     * @return Average equity against the hands we're ahead right now against.
     */
	protected static float getApproximateNpot(float equities[]) {
		double normalizer = 0;
		double equitySum = 0;

        for (float equity : equities) {
            if (getNow(equity) == Now.Ahead) {
                normalizer++;
                equitySum += (1 - equity);
            }
            if (getNow(equity) == Now.Tied) {
                normalizer++;
                equitySum += (1-equity) / 2;
            }
        }

		if (normalizer > 0) {
			return (float) (equitySum / normalizer);
		} else {
			return 0;
		}
	}
}