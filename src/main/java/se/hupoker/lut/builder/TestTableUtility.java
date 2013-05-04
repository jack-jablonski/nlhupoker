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

final class TestTableUtility {
    public interface RandomRunner {
        public double eval(EquityVector ev);
    }

    public static RandomRunner hsRunner = new RandomRunner() {
        @Override
        public double eval(EquityVector ev) {
            return ev.getHS();
        }
    };

    private final RandomRunner runner;

    public TestTableUtility(RandomRunner rr) {
        runner = rr;
    }

    private void testOneRandom(LutTable hsTable, CardSet board, HoleCards hole) {
        EquityVector ev = new EquityVector();

        ev.buildEquities(board, hole);

        if (hsTable != null) {
            boolean doubleEquals = DoubleMath.equal(hsTable.lookupOne(new LutKey(board, hole)), runner.eval(ev));
            assertTrue(doubleEquals);
        }
    }

    public void runOne(LutTable hsTable, int numberOfBoardCards) {
        List<Card> shuffled = DeckSet.shuffledDeck();

        HoleCards hole = HoleCards.of(shuffled.subList(0, HoleCards.TexasHoleCards));
        shuffled.removeAll(hole);
        CardSet board = new CardSet(shuffled.subList(0, numberOfBoardCards));

        testOneRandom(hsTable, board, hole);
    }

    public void runAll(LutTable hsTable, Street street, int iterations) {
        for (int i = 0; i < iterations; i++) {
            runOne(hsTable, street.numberOfBoardCards());
        }
    }
}