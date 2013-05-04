package se.hupoker.inference;

/**
 * @author Alexander Nyberg
 */
public class BetPathDescription {
    // TODO name ipTocall, oopTocall
    private double ipTocall, oopTocall;

    // For yamlbeans
    public BetPathDescription() {}

    public BetPathDescription(float ip, float oop) {
        this.ipTocall = ip;
        this.oopTocall = oop;
    }

    public double getIpTocall() {
        return ipTocall;
    }

    public void setIpTocall(double ipTocall) {
        this.ipTocall = ipTocall;
    }

    public double getOopTocall() {
        return oopTocall;
    }

    public void setOopTocall(double oopTocall) {
        this.oopTocall = oopTocall;
    }
}