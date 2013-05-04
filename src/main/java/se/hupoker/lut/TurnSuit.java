package se.hupoker.lut;

import com.google.common.math.IntMath;

import java.util.ArrayList;

/**
 * @author Alexander Nyberg
 */
final class TurnSuit {
    private static final int BADENTRY = -1;

    private final int suitMap[][][][][][] = new int[4][4][4][4][4][4];

    /*
     * Suits 0..3, Ranks 0..5, 6 cards,
     * Max isomorphic card index 5*4+3 = 24
     * nchoosek(24,4) = 10626
     *
     * Reference http://en.wikipedia.org/wiki/Combinadic
     */
    private final static int NUMISOBOARDS = 10626;
    private final int sameHand[] = new int[2 * NUMISOBOARDS];

    /*
     *
     */
    private final ArrayList<int[]> patterns = new ArrayList<int[]>();

    public TurnSuit() {
        TableUtility.initArray(suitMap, BADENTRY);
        TableUtility.initArray(sameHand, BADENTRY);
    }

    public int getSize() {
        return patterns.size();
    }

    public int[] getPattern(int i) {
        return patterns.get(i);
    }

    public int getPatternIndex(int p[]) {
        return suitMap[p[0]][p[1]][p[2]][p[3]][p[4]][p[5]];
    }

    private int sameHandIndex(int ranks[], int suits[]) {
        int[] sortedBoard = TableUtility.getSortedBoard(ranks, suits);
        int suited = (suits[0] == suits[1]) ? 1 : 0;

        int hidx = 0;
        for (int i = 0; i < 4; i++) {
            hidx += IntMath.binomial(sortedBoard[i], i + 1);
            //hidx += Combinatorics.nchoosek(sortedBoard[i], i+1);
        }

        hidx += suited * NUMISOBOARDS;

        return hidx;
    }

    private int sameBoard(int ranks[], int suits[]) {
        int hidx = sameHandIndex(ranks, suits);

        return sameHand[hidx];
    }

    private void addSameBoard(int ranks[], int suits[], int index) {
        int hidx = sameHandIndex(ranks, suits);

        sameHand[hidx] = index;
    }

    private int getSuitMapIndex(int s[]) {
        return suitMap[s[0]][s[1]][s[2]][s[3]][s[4]][s[5]];
    }

    private void setSuitMapIndex(int s[], int index) {
        suitMap[s[0]][s[1]][s[2]][s[3]][s[4]][s[5]] = index;
    }

    /*
     * Keep track for addSuit
     */
    private int isoSuitIndex = 0;

    private void addSuit(int[] lowRanks, int suits[]) {
        int lowSuit[] = SuitUtility.lowestSuit(suits);

        if (!SuitUtility.isApplicable(lowRanks, lowSuit)) {
            return;
        }

		/*
		 * If paired, add hole cards suits in both orders.
		 */
        int reversedHoleSuits[] = null;
        int reversedLowest[] = null;
        boolean paired = (lowRanks[0] == lowRanks[1]);
        if (paired) {
            /// XXX: Clone or copy?
            reversedHoleSuits = suits.clone();
            TableUtility.swap(reversedHoleSuits, 0, 1);
            reversedLowest = SuitUtility.lowestSuit(reversedHoleSuits);
        }

        int seenHandIndex = sameBoard(lowRanks, lowSuit);
        if (seenHandIndex > BADENTRY) {
            setSuitMapIndex(suits, seenHandIndex);

            if (paired) {
                setSuitMapIndex(reversedHoleSuits, seenHandIndex);
            }

            return;
        }

        int lowSuitIndex = getSuitMapIndex(lowSuit);
        // we haven't come across this suit iso pattern yet
        if (lowSuitIndex == BADENTRY) {
            setSuitMapIndex(lowSuit, isoSuitIndex);
            setSuitMapIndex(suits, isoSuitIndex);
            addSameBoard(lowRanks, lowSuit, isoSuitIndex);

            if (paired) {
                setSuitMapIndex(reversedLowest, isoSuitIndex);
                setSuitMapIndex(reversedHoleSuits, isoSuitIndex);
                addSameBoard(lowRanks, reversedLowest, isoSuitIndex);
            }

            patterns.add(lowSuit);
            isoSuitIndex++;
        } else {
            setSuitMapIndex(suits, lowSuitIndex);
            if (paired) {
                setSuitMapIndex(reversedHoleSuits, lowSuitIndex);
            }
        }
    }

    /**
     * Enumerate the all suits
     *
     * @param lowRanks
     */
    public void enumSuits(int[] lowRanks) {
        int suits[] = new int[6];

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                for (int k = 0; k < 4; k++)
                    for (int l = 0; l < 4; l++)
                        for (int m = 0; m < 4; m++)
                            for (int n = 0; n < 4; n++) {
                                suits[0] = i;
                                suits[1] = j;
                                suits[2] = k;
                                suits[3] = l;
                                suits[4] = m;
                                suits[5] = n;

                                addSuit(lowRanks, suits);
                            }
    }
}