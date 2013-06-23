package se.hupoker.cards.handeval;

import se.hupoker.cards.HoleCards;
import se.hupoker.common.Endomorphism;
import se.hupoker.common.IdentityMorphism;

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
    private final Endomorphism<HoleCards> map;

    protected EquityMatrix(EquityTable equityTable) {
        this.equityTable = equityTable;
        map = new IdentityMorphism<>();
    }

    protected EquityMatrix(EquityTable equityTable, Endomorphism<HoleCards> map) {
        this.equityTable = equityTable;
        this.map = map;
    }

    protected double getEquity(HoleCards myHole, HoleCards opHole) {
        HoleCards myMorphism = map.apply(myHole);
        HoleCards opMorphism = map.apply(opHole);
        return equityTable.getEquities()[myMorphism.ordinal()][opMorphism.ordinal()];
    }

    public float getApproximateHs(HoleCards hole) {
        HoleCards morphism = map.apply(hole);
        return EquityMeasure.getApproximateHs(equityTable.getEquities()[morphism.ordinal()]);
    }

    public float getApproximatePpot(HoleCards hole) {
        HoleCards morphism = map.apply(hole);
        return EquityMeasure.getApproximatePpot(equityTable.getEquities()[morphism.ordinal()]);
    }

    public float getApproximateNpot(HoleCards hole) {
        HoleCards morphism = map.apply(hole);
        return EquityMeasure.getApproximatePpot(equityTable.getEquities()[morphism.ordinal()]);
    }

    // For river
    public float getAverageEquity(HoleCards hole) {
        HoleCards morphism = map.apply(hole);
        return EquityMeasure.getEquity(equityTable.getEquities()[morphism.ordinal()]);
    }
}