package se.hupoker.inference.holebucket;

import java.text.DecimalFormat;

/**
 * Base for flop & turn
 *
 * @author Alexander Nyberg
 */
class HoleTuple {
    private double hs;
    private double ppot;
    private double npot;

     // Required because of yamlbeans.
    public HoleTuple() {
    }

    public HoleTuple(double hs, double ppot, double npot) {
        this.setHs(hs);
        this.setPpot(ppot);
        this.setNpot(npot);
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

    public void setHs(double hs) {
        this.hs = hs;
    }

    public double getPpot() {
        return ppot;
    }

    public void setPpot(double ppot) {
        this.ppot = ppot;
    }

    public double getNpot() {
        return npot;
    }

    public void setNpot(double npot) {
        this.npot = npot;
    }
}