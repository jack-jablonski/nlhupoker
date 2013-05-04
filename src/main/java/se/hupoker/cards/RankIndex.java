package se.hupoker.cards;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

/**
 * Could be made general to any enum with very slight changes.
 *
 * @author Alexander Nyberg
 */
public class RankIndex {
    /**
     * Multichoose(13,2)
     */
    public static final int NumberOfRankIndices = 91;
    private final Table<Rank, Rank, Integer> table = ArrayTable.create(Rank.allOf(), Rank.allOf());

    public RankIndex() {
        int currentIndex = 0;

        ICombinatoricsVector<Rank> initialVector = Factory.createVector(Rank.allOf());
        Generator<Rank> generator = Factory.createMultiCombinationGenerator(initialVector, 2);
        for (ICombinatoricsVector<Rank> comb : generator) {
            setCombination(comb.getValue(0), comb.getValue(1), currentIndex);
            currentIndex++;
        }
    }

    private void setCombination(Rank first, Rank second, int index) {
        table.put(first, second, index);
        table.put(second, first, index);
    }

    public int get(Rank first, Rank sec) {
        return table.get(first, sec);
    }

    public int get(int first, int second) {
        return get(Rank.fromOrdinal(first), Rank.fromOrdinal(second));
    }

    public int get(HoleCards hole) {
        return get(hole.first().rankOf(), hole.last().rankOf());
    }
}