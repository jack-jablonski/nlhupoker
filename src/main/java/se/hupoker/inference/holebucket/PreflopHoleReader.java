package se.hupoker.inference.holebucket;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.handeval.EquityMatrix;
import se.hupoker.cards.handeval.EquityRepository;
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

            ActionDistribution ad = ActionDistribution.from(descriptor.getBetting(), descriptor.toString(), options);

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
    public Map<HoleCards, Integer> getHoleCluster(EquityRepository equityRepository, CardSet board) {
        return ImmutableMap.copyOf(map);
    }

    private void verify() {
        checkState(map.size() == HoleCards.TexasCombinations);

        List<HoleCards> allHoles = new ArrayList<>();
        for (PreflopCluster preflop : preflopClusters) {
            allHoles.addAll(preflop.getCluster());
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

            for (HoleCards hole : bucket.getCluster()) {
                map.put(hole, index);
            }
        }
    }
}