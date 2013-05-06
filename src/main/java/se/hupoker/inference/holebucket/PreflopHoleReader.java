package se.hupoker.inference.holebucket;

import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.inference.actiondistribution.ActionDistOptions;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.inference.states.GenericState;

import java.util.*;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Alexander Nyberg
 */
public class PreflopHoleReader implements HoleClusterer {
    private final List<PreflopCluster> preflopClusters;
    private final Map<HoleCards, Integer> map = new HashMap<>();

    public PreflopHoleReader(Collection<PreflopCluster> preflopClusters) {
        this.preflopClusters = new ArrayList<>(preflopClusters);

        buildMap();
        verify();
    }

    /**
     * Factory
     *
     * @param descriptor
     * @return
     */
    @Override
    public HoleCluster getHoleClusters(GenericState descriptor) {
        HoleCluster bm = new HoleCluster();

        for (PreflopCluster pf : preflopClusters) {
            EnumSet<ActionDistOptions> options = getOptions(descriptor, pf);

            ActionDistribution ad = ActionDistribution.from(descriptor.getBetting(), pf.range, options);

            bm.add(ad);
        }

        return bm;
    }

    /**
     *
     *
     * @param descriptor
     * @param preflopCluster
     * @return Combination of State & Cluster
     */
    private EnumSet<ActionDistOptions> getOptions(GenericState descriptor, PreflopCluster preflopCluster) {
        return ActionDistOptions.empty();
    }

    @Override
    public int getHoleClusterIndex(CardSet board, HoleCards hole) {
        return map.get(hole);
    }

    private void verify() {
        checkState(map.size() == HoleCards.TexasCombinations);

        List<HoleCards> allHoles = new ArrayList<>();
        for (PreflopCluster preflop : preflopClusters) {
            allHoles.addAll(preflop.holes);
        }

        checkState(allHoles.size() == HoleCards.TexasCombinations);

        Set<HoleCards> uniqueHoles = new HashSet<>(allHoles);
        checkState(uniqueHoles.size() == HoleCards.TexasCombinations);
    }

    /**
     * Construct the map: hole index -> bucket index
     */
    private void buildMap() {
        for (int index = 0; index < preflopClusters.size(); index++) {
            PreflopCluster bucket = preflopClusters.get(index);

            for (HoleCards hole : bucket.holes) {
                map.put(hole, index);
            }
        }
    }


}