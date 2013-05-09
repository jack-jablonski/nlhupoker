package se.hupoker.inference.holebucket;

import java.text.DecimalFormat;

/**
 * Base for flop & turn
 *
 * @author Alexander Nyberg
 */
class HoleTuple {
    private final double hs;
    private final double ppot;
    private final double npot;

    public HoleTuple(double hs, double ppot, double npot) {
        this.hs = hs;
        this.ppot = ppot;
        this.npot = npot;
    }

    private double f(double a, double b) {
        return Math.pow(a - b, 2);
    }

    public double getDistance(HoleTuple ht) {
        return f(getHs(), ht.getHs()) + f(getPpot(), ht.getPpot()) + f(getNpot(), ht.getNpot());
    }

    private static final DecimalFormat d = new DecimalFormat("#.###");

    @Override
    public String toString() {
        return d.format(getHs()) + ", " + d.format(getPpot()) + ", " + d.format(getNpot());
    }

    public double getHs() {
        return hs;
    }
    public double getPpot() {
        return ppot;
    }

    public double getNpot() {
        return npot;
    }
}