package se.hupoker.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author Alexander Nyberg
 *
 */
public class DoubleMath {
    private final static double epsilon = 1e-6;
    private DoubleMath() {}

	public static boolean isNegative(BigDecimal decimal) {
		return decimal.signum() < 0;
	}

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return dividend.divide(divisor, 4, RoundingMode.HALF_UP);
    }

    public static boolean isZero(double a) {
        return equal(a, 0.0);
    }

    public static boolean equal(double a, double b) {
        return Math.abs(a - b) < epsilon;
    }

    private static final DecimalFormat pct = new DecimalFormat(".#");
    public static String decimalFormat(double d) {
        return pct.format(100 * d) + "%";
    }
}