package se.hupoker.cards.handeval;

import org.junit.Test;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.CardSet;

/**
 *
 * @author Alexander Nyberg
 *
 */
public class EquityAdapterTest {
	@Test
	public void test() {
		CardSet board = CardSet.from("Ts9s4h");
		HoleCards hole = HoleCards.from("QsJs");

		EquityVector ev = new EquityVector();
		ev.buildEquities(board, hole);
		
		System.out.println(ev.getHS());
		System.out.println(ev.getPPOT());
		System.out.println(ev.getNPOT());
	}
}