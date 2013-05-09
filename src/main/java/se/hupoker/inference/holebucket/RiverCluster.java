package se.hupoker.inference.holebucket;

/**
 * @author Alexander Nyberg
 */
public class RiverCluster implements Comparable<RiverCluster> {
    private final String name;
    private final HandStrength strength;
    private final double hs;

    public RiverCluster(String name, HandStrength strength, double hs) {
        this.name = name;
        this.strength = strength;
        this.hs = hs;
    }

    @Override
    public int compareTo(RiverCluster arg) {
        return Double.compare(getHs(), arg.getHs());
    }

    public String getName() {
        return name;
    }

    public HandStrength getStrength() {
        return strength;
    }

    public double getHs() {
        return hs;
    }

    public static class RiverClusterBuilder implements ClusterBuilder<RiverCluster> {
        public String name;
        public HandStrength strength;
        public double hs;

        public RiverCluster build() {
            return new RiverCluster(name, strength, hs);
        }
    }
}