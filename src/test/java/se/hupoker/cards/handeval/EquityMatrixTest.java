package se.hupoker.cards.handeval;

import org.junit.Test;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.DeckSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.common.DoubleMath;

import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 *
 * @author Alexander Nyberg
 */
public class EquityMatrixTest {
	@Test
	public void testBuildEquities() {
		CardSet board = CardSet.from("9c8h4s");

		System.out.println(board);
		EquityMatrix me = EquityMatrix.from(board);

        HoleCards first = HoleCards.from("9d9h");
        HoleCards second = HoleCards.from("8d8c");
        HoleCards third = HoleCards.from("4d4c");

        assertTrue(DoubleMath.equal(me.getEquity(first, second) + me.getEquity(second, first), 1.0));
        assertTrue(DoubleMath.equal(me.getEquity(first, second), me.getEquity(first, third)));
	}
	
	private CardSet getRandomBoard(int numberOfCards) {
        List<Card> shuffled = DeckSet.shuffledDeck();
        CardSet board = new CardSet(numberOfCards);

        board.addAll(shuffled.subList(0, numberOfCards));

		return board;
	}

    private void inspectEvaluationTime(int numberOfCards) {
        for (int i=0; i < 10; i++) {
            CardSet board = getRandomBoard(numberOfCards);

            long start = System.currentTimeMillis();
            EquityMatrix me = EquityMatrix.from(board);
            long end = System.currentTimeMillis();

            me.printStatistics();
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