package se.hupoker.inference.holebucket;

import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.handeval.EquityMatrix;
import se.hupoker.cards.handeval.EquityRepository;
import se.hupoker.inference.states.GenericState;

import java.util.Map;

/**
 * @author Alexander Nyberg
 */
public interface HoleClusterer {
    /**
     * @param descriptor
     * @return The distribution for this node.
     */
    public HoleCluster getHoleClusters(GenericState descriptor);

    /**
     * Complete information for mapping to a hole card cluster.
     *
     * @param equityRepository
     * @param board
     * @return Map describing which cluster each hole holecards belongs to.
     */
    Map<HoleCards, Integer> getHoleCluster(EquityRepository equityRepository, CardSet board);
}