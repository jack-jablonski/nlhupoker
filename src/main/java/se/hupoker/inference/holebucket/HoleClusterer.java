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
     * @return The cluster universe of this node.
     */
    public HoleCluster getClusterUniverse(GenericState descriptor);

    /**
     * Complete information for mapping to a hole card cluster. A function that maps every HoleCards
     * (given the board) onto ClusterUniverse.
     *
     * @param equityRepository
     * @param board
     * @return Map describing which cluster each hole holecards belongs to. If holeCards don't exist map to null.
     */
    Map<HoleCards, Integer> getClustering(EquityRepository equityRepository, CardSet board);
}