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
     *  TODO: Half size from get(i,j) = 1 - get(j,i)
     *
     */
    private final float[][] equities = new float[HoleCards.TexasCombinations][HoleCards.TexasCombinations];

    public EquityTable() {
        for (float[] anEquity : equities) {
            Arrays.fill(anEquity, EquityMeasure.BADEQUITY);
        }
    }

    public float[][] getEquities() {
        return equities;
    }
}
