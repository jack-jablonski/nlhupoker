package se.hupoker.cards.cache;

import se.hupoker.cards.RankIndex;

/**
 * Immutable class for Holecard relation to board.
 *
 * @author Alexander Nyberg
 */
@Deprecated
class HoleRelation {
    private final int rank;
    private final FlushConfiguration flush;

    protected HoleRelation(int ranking, FlushConfiguration flush) {
        this.rank = ranking;
        this.flush = flush;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof HoleRelation)) {
            return false;
        }

        final HoleRelation otherRelation = (HoleRelation) other;
        return this.rank == otherRelation.rank && this.flush == otherRelation.flush;
    }

    /**
     * @return Perfect hash.
     */
    @Override
    public int hashCode() {
        return rank + RankIndex.NumberOfRankIndices * flush.ordinal();
    }

    @Override
    public String toString() {
        return rank + "|" + flush;
    }
}