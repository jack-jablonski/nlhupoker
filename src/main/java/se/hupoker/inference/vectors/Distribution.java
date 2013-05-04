package se.hupoker.inference.vectors;

import se.hupoker.common.DoubleMath;


/**
 * Probability distribution.
 * 
 * @author Alexander Nyberg
 * 
 */
public class Distribution extends DoubleVector {
	public Distribution(int num) {
		super(num);

		final double initial = (double) 1 / num;
		setAll(initial);
	}

	public Distribution(int num, double initial) {
		super(num);
		setAll(initial);
	}

	/**
	 * Normalize distribution (sum to 1).
	 */
	public void normalize() {
		double sum = getSum();

		divideAll(sum);
	}

	public void assertValues() {
		assertWithinBoundaries(0.0, 1.0);
	}

	public String getAsPercentages() {
		StringBuilder buffer = new StringBuilder("(");

        for (double d : getElements()) {
            buffer.append(DoubleMath.decimalFormat(d));
            buffer.append(", ");
        }

		buffer.append(")");

		return buffer.toString();
	}
}