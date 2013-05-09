package se.hupoker.lut.builder;

import se.hupoker.cards.DeckSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.handeval.EquityVector;
import se.hupoker.common.DoubleMath;
import se.hupoker.common.Street;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutTable;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Alexander Nyberg
 */
final class TestTableUtility {
    public interface RandomRunner {
        public double eval(EquityVector ev);
    }

    public static RandomRunner handEvaluator = new RandomRunner() {
        @Override
        public double eval(EquityVector ev) {
            return ev.getHS();
        }
    };

    private static void compareTableEntryWithCalculation(LutTable table, CardSet board, HoleCards hole, RandomRunner evaluator) {
        EquityVector ev = new EquityVector();

        ev.buildEquities(board, hole);

        float tableValue = table.lookupOne(new LutKey(board, hole));
        boolean doubleEquals = DoubleMath.equal(tableValue, evaluator.eval(ev));
        if (!doubleEquals) {
            System.out.println("Cards are:" + board + ", " + hole);
            System.out.println("Evaluated table: " + tableValue + " and eval:" + evaluator.eval(ev));
            fail();
        }
    }

    public static void compareRandomEntry(LutTable hsTable, int numberOfBoardCards, RandomRunner evaluator) {
        List<Card> shuffled = DeckSet.shuffledDeck();

        HoleCards hole = HoleCards.of(shuffled.subList(0, HoleCards.TexasHoleCards));
        shuffled.removeAll(hole);
        CardSet board = new CardSet(shuffled.subList(0, numberOfBoardCards));

        compareTableEntryWithCalculation(hsTable, board, hole, evaluator);
    }

    public static void runRandomComparisons(LutTable hsTable, Street street, int iterations, RandomRunner evaluator) {
        for (int i = 0; i < iterations; i++) {
            compareRandomEntry(hsTable, street.numberOfBoardCards(), evaluator);
        }
    }
}