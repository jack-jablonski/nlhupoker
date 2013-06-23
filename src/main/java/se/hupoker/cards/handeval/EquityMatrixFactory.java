package se.hupoker.cards.handeval;

import se.hupoker.cards.CardSet;

/**
 * @author Alexander Nyberg
 */
public class EquityMatrixFactory {

    public static EquityMatrix calculate(CardSet board) {
        EquityTable table = EquityTableFactory.calculate(board);

        return new EquityMatrix(table);
    }
}
