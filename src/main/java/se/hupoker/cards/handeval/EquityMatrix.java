package se.hupoker.cards.handeval;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.ICombinatoricsVector;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.DeckSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardisomorphisms.Pair;
import se.hupoker.cards.boardisomorphisms.PairManager;

import java.io.Serializable;
import java.util.Arrays;

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

    public double getHs(HoleCards hole) {
        return EquityMeasure.getHS(equityTable.getAhead()[hole.ordinal()]);
    }

    public double getPpot(HoleCards hole) {
        return EquityMeasure.getPPOT(equityTable.getAhead()[hole.ordinal()], equityTable.getEquities()[hole.ordinal()]);
    }

    public double getNpot(HoleCards hole) {
        return EquityMeasure.getPPOT(equityTable.getAhead()[hole.ordinal()], equityTable.getEquities()[hole.ordinal()]);
    }

    // For river
    public double getAverageEquity(HoleCards hole) {
        return EquityMeasure.getEquity(equityTable.getEquities()[hole.ordinal()]);
    }
}