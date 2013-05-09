package se.hupoker.inference.holebucket;

/**
 * @author Alexander Nyberg
 */
public class FlopCluster {
    private final HoleTuple holeTuple;
    private final HandStrength strength;

    public FlopCluster(HoleTuple holeTuple, HandStrength strength) {
        this.holeTuple = holeTuple;
        this.strength = strength;
    }

    public static class FlopClusterBuilder implements ClusterBuilder<FlopCluster> {
        public HandStrength strength = HandStrength.NONE;
        public double hs;
        public double ppot;
        public double npot;

        public FlopCluster build() {
            HoleTuple tuple = new HoleTuple(hs, ppot, npot);

            return new FlopCluster(tuple, strength);
        }
    }
}