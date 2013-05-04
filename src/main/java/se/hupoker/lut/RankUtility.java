package se.hupoker.lut;

import se.hupoker.cards.RankIndex;

/**
 *
 * @author Alexander Nyberg
 *
 */
final class RankUtility {
    private static final RankIndex rankIndex = new RankIndex();

    /**
     * @param ranks
     * @return The number of ranks of the most common rank.
     */
    public static int countMax(int ranks[]) {
        int count[] = new int[13];
        int max = 0;

        for (int rank : ranks) {
            count[rank]++;

            if (count[rank] > count[max]) {
                max = rank;
            }
        }
        return count[max];
    }

    /**
     * @param ranks Not sorted!
     * @return The lowest rank pattern.
     */
    public static int[] lowestRank(int ranks[]) {
        int map[] = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        int lowestRank[] = new int[ranks.length];

        int currentRank = 0;

        for (int i = 0; i < ranks.length; i++) {
            if (map[ranks[i]] == -1) {
                map[ranks[i]] = currentRank;
                currentRank++;
            }
            lowestRank[i] = map[ranks[i]];
        }
        return lowestRank;
    }

    /**
     * @param hRank Hole rank. Need not be sorted.
     * @return Unique index of hole card ranks.
     */
    public static int holeRankIndex(int[] hRank) {
        return rankIndex.get(hRank[0], hRank[1]);
    }
}