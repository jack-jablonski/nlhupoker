package se.hupoker.inference.actiondistribution;

import se.hupoker.common.Betting;
import se.hupoker.common.Action;
import se.hupoker.inference.vectors.Distribution;
import se.hupoker.inference.vectors.DoubleVector;

import java.util.EnumSet;

/**
 * 
 * @author Alexander Nyberg
 * 
 */
abstract public class ActionDistribution {
    /**
     * Factory all creation should go through.
     */
    public static ActionDistribution from(Betting type, String desc, EnumSet<ActionDistOptions> options) {
        if (type == Betting.CB) {
            return new CBDistribution(desc, options);
        } else {
            return new FCRDistribution(desc, options);
        }
    }

    /**
     *
     * @param act
     * @return Sub type mapping of action.
     */
    abstract protected int getActionIndex(Action act);

    /**
     *
     * @return The number of action types distribution defines
     */
    abstract protected int getNumberOfActions();


    /*
     *
     */
	private final EnumSet<ActionDistOptions> optionSet;
	private final Distribution probability;
	private final DoubleVector gradient;
	private final String description;

	protected ActionDistribution(String desc, EnumSet<ActionDistOptions> options) {
		description = desc;
		optionSet = options;

		probability = new Distribution(getNumberOfActions());
		gradient = new DoubleVector(getNumberOfActions());

		if (options.contains(ActionDistOptions.NOFOLD)) {
			probability.set(0, 0.0);
			probability.normalize();
		}
	}

	public synchronized void addDerivative(Action act, double adder) {
		int index = getActionIndex(act);
		gradient.add(index, adder);
	}

	/*
	 * The closest distance to boundary.
	 */
	private final double BOUNDARY = 0.01;

	/**
	 * TODO: Will only hit boundary in one variable setUp giving up.
	 * 
	 * @param var
	 * @param updates
	 * @return Maximum allowed step size setUp we hit boundary
	 */
	private double getMaxBoundaryStep(DoubleVector var, DoubleVector updates) {
		assert var.size() == updates.size();
        assert gradient.size() == probability.size();
		double maxStep = Double.MAX_VALUE;

		for (int i=0; i < gradient.size(); i++) {
			double g = gradient.get(i);

			double borderStep;
			if (g < 0) {
				borderStep = -(probability.get(i) - BOUNDARY) / g;
			} else {
				borderStep = ((1-BOUNDARY) - probability.get(i)) / g;
			}
			maxStep = Math.min(maxStep, borderStep);
		}

		return maxStep;
	}

	/**
	 * TODO: Update other variables even if some variable has hit constraint.
	 * 
	 */
	public synchronized void adjustAndClearDerivatives() {
		final double LAMBDA = 0.1;

		gradient.assertNonNegative();

		int numActive = probability.size();
		if (optionSet.contains(ActionDistOptions.NOFOLD)) {
			gradient.set(0, 0.0);
			numActive--;
		}

		/*
		 * Adjust.
		 */
		double derivSum = gradient.getSum();
		if (derivSum > 0) {
			gradient.divideAll(derivSum);

            final double normalizer = (double) 1 / numActive;
            gradient.minusAll(normalizer);
			if (optionSet.contains(ActionDistOptions.NOFOLD)) {
				gradient.set(0, 0.0);
			}

			double boundaryStep = getMaxBoundaryStep(probability, gradient);
			double step = Math.min(LAMBDA, boundaryStep);

			gradient.multiplyAll(step);

			/*
			 * Add back to getProbability.
			 */
			probability.addAll(gradient);
		}

		probability.assertValues();

		/*
		 * Clear derivatives.
		 */
		gradient.setAll(0.0);
	}

	public synchronized double getProbability(Action act) {
		return probability.get(getActionIndex(act));
	}

	public synchronized String probString() {
		return probability.getAsPercentages();
	}

	public synchronized String derivativeString() {
		return gradient.toString();
	}
	
	@Override
	public synchronized String toString() {
		return description;
	}
}