package se.hupoker.inference.vectors;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * I hate this class and all its sub classes but need it because of memory constraints.
 *
 * @author Alexander Nyberg
 * 
 */
public class DoubleVector {
	private final double elements[];

    public double[] getElements() {
        return elements;
    }

	public DoubleVector(int num) {
		elements = new double[num];
	}

    private void verifyNewValue(double value) {
        checkArgument(!Double.isNaN(value));
        checkArgument(!Double.isInfinite(value));
    }

    public void setAll(double value) {
        verifyNewValue(value);
        Arrays.fill(elements, value);
    }

    public void minusAll(double minus) {
        verifyNewValue(minus);
		for (int i=0; i < size(); i++) {
			elements[i] -= minus;
		}
	}

    public void divideAll(double divider) {
        checkArgument(divider > 0, "Bad division!");
        verifyNewValue(divider);

		for (int i=0; i < size(); i++) {
			elements[i] /= divider;
		}
	}

    public void multiplyAll(double factor) {
        verifyNewValue(factor);
		for (int i=0; i < size(); i++) {
			elements[i] *= factor;
		}
	}

    public void addAll(DoubleVector other) {
        checkArgument(size() == other.size());

        for (int i=0; i < size(); i++) {
            double otherValue = other.get(i);
            verifyNewValue(otherValue);
            add(i, otherValue);
        }
    }

    public void multiplyOne(int index, double value) {
        verifyNewValue(value);
		elements[index] *= value;
	}

    public void assertNonNegative() {
		for (double d : elements) {
            checkState(d >= 0.0);
		}
	}

    protected void assertWithinBoundaries(double low, double high) {
		for (double d : elements) {
            checkState(d >= low);
            checkState(d <= high);
		}
	}

    public int size() {
		return elements.length;
	}

    public double get(int index) {
        return elements[index];
	}

	public void set(int index, double value) {
        verifyNewValue(value);
		elements[index] = value;
	}

    public void add(int index, double value) {
        verifyNewValue(value);
		elements[index] += value;
	}

    public double getSum() {
		double sum = 0;
		
		for (double d : elements) {
			sum += d;
		}
		
		return sum;
	}

    protected int countPositive() {
		int count = 0;

        for (double d  : elements) {
            count += d > 0 ? 1 : 0;
        }

		return count;
	}

	@Override
	public String toString() {
		return Arrays.toString(elements);
	}
}