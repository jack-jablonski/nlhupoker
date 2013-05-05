package se.hupoker.inference.holebucket;

/**
 * @author Alexander Nyberg
 */
public class RiverCluster implements Comparable<RiverCluster> {
    private String name;
    private HandStrength strength = HandStrength.NONE;
    private double hs;

    @Override
    public int compareTo(RiverCluster arg) {
        return Double.compare(getHs(), arg.getHs());
    }

    public String getName() {
     return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HandStrength getStrength() {
        return strength;
    }

    public void setStrength(HandStrength strength) {
        this.strength = strength;
    }

    public double getHs() {
        return hs;
    }

    public void setHs(double hs) {
        this.hs = hs;
    }
}
