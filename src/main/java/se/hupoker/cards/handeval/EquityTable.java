package se.hupoker.cards.handeval;

import se.hupoker.cards.HoleCards;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Equities when all cards are run out on a given board.
 *
 * @author Alexander Nyberg
 */
class EquityTable implements Serializable {
    /*
     *  TODO: Half size from get(i,j) = 1 - get(j,i)
     *  TODO: Should ideally be a Table<HoleCards, HoleCards, Float> but would be gigantic?
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
