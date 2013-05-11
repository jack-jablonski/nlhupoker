package se.hupoker.cards.handeval;

import se.hupoker.cards.HoleCards;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A container for the table of equities of hole card vs. hole card combinations.
 * 13.4M = Binomial(52,2)^2 * 8 byte / 1024^2
 *
 * @author Alexander Nyberg
 */
public class EquityMatrix implements Serializable {
    private final EquityTable equityTable;

    protected EquityMatrix(EquityTable equityTable) {
        this.equityTable = equityTable;
    }

    protected double getEquity(HoleCards myHole, HoleCards opHole) {
        return equityTable.getEquities()[myHole.ordinal()][opHole.ordinal()];
    }

    public float getApproximateHs(HoleCards hole) {
        return EquityMeasure.getApproximateHs(equityTable.getEquities()[hole.ordinal()]);
    }

    public float getApproximatePpot(HoleCards hole) {
        return EquityMeasure.getApproximatePpot(equityTable.getEquities()[hole.ordinal()]);
    }

    public float getApproximateNpot(HoleCards hole) {
        return EquityMeasure.getApproximatePpot(equityTable.getEquities()[hole.ordinal()]);
    }

    // For river
    public float getAverageEquity(HoleCards hole) {
        return EquityMeasure.getEquity(equityTable.getEquities()[hole.ordinal()]);
    }
}