package se.hupoker.lut;

import com.google.common.math.IntMath;

import java.util.ArrayList;

/**
 * @author Alexander Nyberg
 */
final class RiverSuit {
    private static final int BADENTRY = -1;
    private final int suitMap[][][][][][][] = new int[4][4][4][4][4][4][4];

    private final ArrayList<int[]> patterns = new ArrayList<>();

    /*
     * Suits 0..3, Ranks 0..6, 7 cards, max card index 6*4+3
     * Only needed during creation so can be shared among all RiverSuit's
     */
    private int NUMRANKISOBOARDS = 98280;
    private int sameHand[] = new int[2 * NUMRANKISOBOARDS];

    protected RiverSuit() {
        TableUtility.initArray(suitMap, BADENTRY);
        TableUtility.initArray(sameHand, BADENTRY);
    }

    protected int getSize() {
        return patterns.size();
    }

    protected int getPatternIndex(int p[]) {
        return suitMap[p[0]][p[1]][p[2]][p[3]][p[4]][p[5]][p[6]];
    }

    private void setSuitMapIndex(int s[], int index) {
        suitMap[s[0]][s[1]][s[2]][s[3]][s[4]][s[5]][s[6]] = index;
    }

    /**
     * See http://en.wikipedia.org/wiki/Combinadic
     *
     * @param ranks
     * @param suits
     * @return Unique index of isomorphic board wrt. hole cards suited or not.
     */
    private int sameHandIndex(int ranks[], int suits[]) {
        int[] sortedBoard = TableUtility.getSortedBoard(ranks, suits);
        int isHoleSuited = (suits[0] == suits[1]) ? 1 : 0;

        int handRankIndex = 0;
        for (int i = 0; i < 5; i++) {
            handRankIndex += IntMath.binomial(sortedBoard[i], i + 1);
        }

        handRankIndex += isHoleSuited * NUMRANKISOBOARDS;

        return handRankIndex;
    }

    private int getBoardIsomorphicIndex(int ranks[], int suits[]) {
        int handRankIndex = sameHandIndex(ranks, suits);

        return sameHand[handRankIndex];
    }

    private void setBoardIsomorphicIndex(int ranks[], int suits[], int index) {
        int handRankIndex = sameHandIndex(ranks, suits);

        sameHand[handRankIndex] = index;
    }

    // first index to a suit pattern with less than three of same suit
    private int lessThanThreeIndex = BADENTRY;

    /**
     * @param suits
     * @return Less than three of one suit, then all map to same suit.
     */
    private boolean lessThanThree(int suits[]) {
        int board[] = new int[]{suits[2], suits[3], suits[4], suits[5], suits[6]};
        int count[] = SuitUtility.suitCount(board);

        return TableUtility.maximumOccurrence(count) < 3;
    }

    /*
     * Lowest un-used suit isomorphic index
     */
    private int isoSuitIndex = 0;

    /**
     * Save all interesting suit patterns wrt. this lowRank
     *
     * @param lowRank Compressed rank pattern.
     * @param suits
     */
    private void addSuit(int lowRank[], int suits[]) {
        if (!SuitUtility.isApplicable(lowRank, suits)) {
            return;
        }

        int lowSuit[] = SuitUtility.lowestSuit(suits);

        /*
		 * If paired, add hole cards suits in both orders.
		 */
        int reversedHoleSuits[] = null;
        int reversedLowest[] = null;
        boolean paired = (lowRank[0] == lowRank[1]);
        if (paired) {
            /// XXX: Clone or copy?
            reversedHoleSuits = suits.clone();
            TableUtility.swap(reversedHoleSuits, 0, 1);
            reversedLowest = SuitUtility.lowestSuit(reversedHoleSuits);
        }

        int seenHandIndex = getBoardIsomorphicIndex(lowRank, lowSuit);
        if (seenHandIndex != BADENTRY) {
            setSuitMapIndex(suits, seenHandIndex);

            if (paired) {
                setSuitMapIndex(reversedHoleSuits, seenHandIndex);
            }

            return;
        }

        if (lessThanThreeIndex != BADENTRY && lessThanThree(lowSuit)) {
            setSuitMapIndex(suits, lessThanThreeIndex);
            setBoardIsomorphicIndex(lowRank, lowSuit, lessThanThreeIndex);

            if (paired) {
                setSuitMapIndex(reversedHoleSuits, lessThanThreeIndex);
                setBoardIsomorphicIndex(lowRank, reversedHoleSuits, lessThanThreeIndex);
            }

            return;
        }

        int lowSuitIndex = getPatternIndex(lowSuit);
        // we haven't come across this suit pattern yet
        if (lowSuitIndex == BADENTRY) {
            setSuitMapIndex(lowSuit, isoSuitIndex);
            setSuitMapIndex(suits, isoSuitIndex);
            setBoardIsomorphicIndex(lowRank, lowSuit, isoSuitIndex);

            if (paired) {
                setSuitMapIndex(reversedLowest, isoSuitIndex);
                setSuitMapIndex(reversedHoleSuits, isoSuitIndex);
                setBoardIsomorphicIndex(lowRank, reversedLowest, isoSuitIndex);
            }

            if (lessThanThreeIndex == BADENTRY && lessThanThree(lowSuit)) {
                lessThanThreeIndex = isoSuitIndex;
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
     * Enumerate the smallest set of board suit patterns for this board rank pattern.
     *
     * @param lowRanks
     */
    protected void enumerateSuits(int[] lowRanks) {
        int suits[] = new int[7];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    for (int l = 0; l < 4; l++) {
                        for (int m = 0; m < 4; m++) {
                            for (int n = 0; n < 4; n++) {
                                for (int o = 0; o < 4; o++) {
                                    suits[0] = i;
                                    suits[1] = j;
                                    suits[2] = k;
                                    suits[3] = l;
                                    suits[4] = m;
                                    suits[5] = n;
                                    suits[6] = o;

                                    addSuit(lowRanks, suits);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
