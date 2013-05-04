package se.hupoker.handhistory;

import java.math.BigDecimal;

/**
 *
 * @author Alexander Nyberg
 *
 */
public class Formatter  {
	private Formatter() {}

	private static String clearDecimalString(String input) {
		return input.replaceAll(",", "");
	}

    public static BigDecimal toDecimal(String input) {
        return new BigDecimal(clearDecimalString(input));
    }

	/**
	 * Remove whitespace & commas
	 * 
	 * @param cards
	 * @return
	 */
	public static String trimCards(String cards) {
		String b = cards.replaceAll(" ", "");
		return b.replaceAll(",", "");
	}
}