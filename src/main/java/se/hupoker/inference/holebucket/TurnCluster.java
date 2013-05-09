package se.hupoker.inference.holebucket;

/**
 * @author Alexander Nyberg
 */
public class TurnCluster  {
    private final HoleTuple holeTuple;
    private final HandStrength strength;

    public TurnCluster(HoleTuple tuple, HandStrength strength) {
        this.holeTuple = tuple;
        this.strength = strength;
    }

    public static class TurnClusterBuilder implements ClusterBuilder<TurnCluster> {
        public HandStrength strength = HandStrength.NONE;
        public double hs;
        public double ppot;
        public double npot;

        public TurnCluster build() {
            HoleTuple tuple = new HoleTuple(hs, ppot, npot);

            return new TurnCluster(tuple, strength);
        }
    }
}