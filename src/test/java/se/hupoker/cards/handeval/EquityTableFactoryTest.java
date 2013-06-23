package se.hupoker.cards.handeval;

import com.google.common.math.IntMath;
import org.junit.Test;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Alexander Nyberg
 */
public class EquityTableFactoryTest {
    @Test
    public void flopNumberOfCalculatedValues() {
        final int numberOfCombinations = IntMath.binomial(49,2 ) * IntMath.binomial(47, 2);
        CardSet board = CardSet.from("9c8h4s");

        int numberOfSet = countNumberOfSetEntries(board);
        assertThat(numberOfSet, is(numberOfCombinations));
    }

    @Test
    public void turnNumberOfCalculatedValues() {
        final int numberOfCombinations = IntMath.binomial(48,2 ) * IntMath.binomial(46, 2);
        CardSet board = CardSet.from("9c8h4s3s");

        int numberOfSet = countNumberOfSetEntries(board);
        assertThat(numberOfSet, is(numberOfCombinations));
    }

    @Test
    public void riverNumberOfCalculatedValues() {
        final int numberOfCombinations = IntMath.binomial(47,2 ) * IntMath.binomial(45, 2);
        CardSet board = CardSet.from("9c8h4s3s3d");

        int numberOfSet = countNumberOfSetEntries(board);
        assertThat(numberOfSet, is(numberOfCombinations));
    }

    private EquityEvaluator evaluator = new EquityEvaluator() {
        @Override
        public double evaluate(CardSet board, HoleCards myHole, HoleCards opHole) {
            return 1.0;
        }

        @Override
        public double evaluateWithExtra(CardSet board, HoleCards myHole, HoleCards opHole, List<Card> extra) {
            return 1.0;
        }
    };

    private int countNumberOfSetEntries(CardSet board) {
        EquityTable table = EquityTableFactory.calculateWithEvaluator(evaluator, board);
        float[][] equities = table.getEquities();

        int numberOfSet = 0;
        for (float [] inner : equities) {
            for (double number : inner) {
                if (!Double.isNaN(number)) {
                    numberOfSet++;
                }
            }
        }
        return numberOfSet;
    }
}