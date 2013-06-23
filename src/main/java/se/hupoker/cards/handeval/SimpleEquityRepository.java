package se.hupoker.cards.handeval;

import se.hupoker.cards.CardSet;
import se.hupoker.common.Street;

/**
 * @author Alexander Nyberg
 */
public class SimpleEquityRepository implements EquityRepository {
    @Override
    public EquityMatrix get(Street Street, CardSet board) {
        EquityTable table = EquityTableFactory.calculate(board);
        return new EquityMatrix(table);
    }
}