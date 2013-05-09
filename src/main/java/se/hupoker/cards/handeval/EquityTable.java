package se.hupoker.cards.handeval;

import se.hupoker.cards.HoleCards;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Alexander Nyberg
 */
class EquityTable implements Serializable {
    /*
     *  Equities when all cards run out
     *  TODO: 1) Half size from using float. 2) Half size from get(i,j) = 1 - get(j,i)
     *
     */
    private final double[][] equities = new double[HoleCards.TexasCombinations][HoleCards.TexasCombinations];
    /*
     * Current status for flop & turn. Only has {behind,ahead,tie}
     */
    private final double[][] ahead = new double[HoleCards.TexasCombinations][HoleCards.TexasCombinations];

    public EquityTable() {
        for (double[] anAhead : ahead) {
            Arrays.fill(anAhead, EquityMeasure.BADEQUITY);
        }

        for (double[] anEquity : equities) {
            Arrays.fill(anEquity, EquityMeasure.BADEQUITY);
        }
    }

    public double[][] getEquities() {
        return equities;
    }

    public double[][] getAhead() {
        return ahead;
    }
}
