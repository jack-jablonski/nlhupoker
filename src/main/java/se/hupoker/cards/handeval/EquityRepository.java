package se.hupoker.cards.handeval;

import se.hupoker.cards.CardSet;
import se.hupoker.poker.Street;

/**
 * Has responsibility of providing the information required to cluster hole cards given some board.
 *
 * @author Alexander Nyberg
 */
public interface EquityRepository {
    /**
     *
     * @param Street
     * @param board
     * @return Fetches
     */
    public EquityMatrix get(Street Street, CardSet board);
}