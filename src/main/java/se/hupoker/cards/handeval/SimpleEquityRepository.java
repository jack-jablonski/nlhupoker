package se.hupoker.cards.handeval;

import se.hupoker.cards.CardSet;
import se.hupoker.common.Street;

/**
 * @author Alexander Nyberg
 */
public class SimpleEquityRepository implements EquityRepository {
    @Override
    public EquityMatrix get(Street Street, CardSet board) {
        return EquityMatrix.factory(board);
    }
}