package se.hupoker.cards.cache;

import se.hupoker.cards.RankIndex;

/**
 * Immutable class for Holecard relation to board.
 *
 * @author Alexander Nyberg
 */
class HoleRelation {
    private final int rank;
    private final FlushConfiguration flush;

    protected HoleRelation(int ranking, FlushConfiguration flush) {
        this.rank = ranking;
        this.flush = flush;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof HoleRelation)) {
            return false;
        }

        final HoleRelation other = (HoleRelation) otherObject;
        return this.rank == other.rank && this.flush == other.flush;
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