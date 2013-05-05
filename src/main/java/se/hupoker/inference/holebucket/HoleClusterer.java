package se.hupoker.inference.holebucket;

import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.inference.states.GenericState;

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
     *
     * @param board
     * @param hole
     * @return Index describing where in HoleCluster we should look.
     */
    int getHoleClusterIndex(CardSet board, HoleCards hole);
}