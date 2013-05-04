package se.hupoker.lut;

/**
 * @author Alexander Nyberg
 */
final class FlopRank {
    private static final int[] bRank1 = {0, 12, 23, 33, 42, 50, 57, 63, 68, 72, 75, 77, 78};
    private static final int[] bRank2 = {0, 78, 144, 199, 244, 280, 308, 329, 344, 354, 360, 363, 364};

    /**
     * @param bRank Board rank (sorted ascending).
     * @return Rank index of the board [0, 454]
     */
    private static int boardRankIndex(int bRank[]) {
        return bRank2[bRank[0]] + bRank1[bRank[1]] + bRank[2];
    }

    /**
     * @param Rank
     * @return Unique index for every rank (hole rank, board rank) combination.
     */
    protected static int uniqueRankIndex(int Rank[]) {
        int holeRank[] = new int[]{Rank[0], Rank[1]};
        int boardRank[] = new int[]{Rank[2], Rank[3], Rank[4]};

        int holeRankIndex = RankUtility.holeRankIndex(holeRank);
        int boardRankIndex = boardRankIndex(boardRank);

        return boardRankIndex * 91 + holeRankIndex;
    }
}
