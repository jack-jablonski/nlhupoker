package se.hupoker.inference.holebucket;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.HoleRange;

import java.util.Collection;
import java.util.List;

/**
 * @author Alexander Nyberg
 */
public class PreflopCluster {
    private final String range;
    private final HandStrength strength;
    private final ImmutableCollection<HoleCards> holes;

    public PreflopCluster(String range, HandStrength strength, ImmutableCollection<HoleCards> holes) {
        this.range = range;
        this.strength = strength;
        this.holes = holes;
    }

    public String getDescription() {
        return range;
    }

    public HandStrength getStrength() {
        return strength;
    }

    public Collection<HoleCards> getCluster() {
        return holes;
    }

    public static class PreflopClusterClusterBuilder implements ClusterBuilder<PreflopCluster> {
        private final HoleRange holeRange = new HoleRange();
        public String range;
        public HandStrength strength = HandStrength.NONE;

        public PreflopCluster build() {
            List<HoleCards> bucketCards = holeRange.parse(range);

            return new PreflopCluster(range, strength, ImmutableList.copyOf(bucketCards));
        }
    }
}