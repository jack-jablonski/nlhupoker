package se.hupoker.cards.handeval;

import org.junit.Test;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.DeckSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.common.DoubleMath;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 *
 * @author Alexander Nyberg
 */
public class EquityMatrixTest {
	@Test
	public void testFlopNutEquities() {
		CardSet board = CardSet.from("9c8h4s");

		System.out.println(board);
		EquityMatrix me = EquityMatrixFactory.calculate(board);

        HoleCards first = HoleCards.from("9d9h");
        HoleCards second = HoleCards.from("8d8c");
        HoleCards third = HoleCards.from("4d4c");

        assertTrue(DoubleMath.equal(me.getEquity(first, second) + me.getEquity(second, first), 1.0));
        assertTrue(DoubleMath.equal(me.getEquity(first, second), me.getEquity(first, third)));
	}

    @Test
    public void testSpecificRiverHandStrength() {
        CardSet board = CardSet.from("6sAsKh7s2s");
        HoleCards hole = HoleCards.from("9sJd");

        /**
         * There are 4 higher spades (T,J,Q,K). Remove those + known cards
         * then there are 52-5-2-4=41 remaining. 4*41+nchoosek(4,2) hands beat me out of
         * nchoosek(45,2).
         */
        final double hsValue = 0.828282828282828282;

        EquityMatrix me = EquityMatrixFactory.calculate(board);

         // TODO: Fails because of how EquityMatrix caches isomorphisms right now.
        boolean equal = DoubleMath.equal(hsValue, me.getAverageEquity(hole));
        assertTrue(equal);
    }

	private CardSet getRandomBoard(int numberOfCards) {
        List<Card> shuffled = DeckSet.shuffledDeck();
        CardSet board = new CardSet();

        board.addAll(shuffled.subList(0, numberOfCards));

		return board;
	}

    private void inspectEvaluationTime(int numberOfCards) {
        for (int i=0; i < 10; i++) {
            CardSet board = getRandomBoard(numberOfCards);

            long start = System.currentTimeMillis();
            EquityMatrix me = EquityMatrixFactory.calculate(board);
            long end = System.currentTimeMillis();

//            me.printStatistics();
            System.out.println(board + " execution time was "+(end-start)+" ms.");
        }
    }

	@Test
	public void inspectFlopEvaluationTime() {
        final int numberOfCards = 3;
		System.out.println("Flop EquityMatrix times");
        inspectEvaluationTime(numberOfCards);
	}

    @Test
    public void inspectTurnEvaluationTime() {
        final int numberOfCards = 4;
        System.out.println("Turn EquityMatrix times");
        inspectEvaluationTime(numberOfCards);
    }

    @Test
    public void inspectRiverEvaluationTime() {
        final int numberOfCards = 5;
        System.out.println("River EquityMatrix times");
        inspectEvaluationTime(numberOfCards);
    }
}