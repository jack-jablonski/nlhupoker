package se.hupoker.lut;

import com.google.common.math.IntMath;

import java.util.ArrayList;


/**
 * -> If blocking only 1 suit it matters very little which one (try 0.25707385 vs. 0.25704312) for general rank patterns.
 *
 * @author Alexander Nyberg
 */
final class FlopSuit {
    private static final int BADENTRY = -1;

    /*
     * Maps each unique suit to its isomorphic suit index.
     */
    private final int suitMap[][][][][] = new int[4][4][4][4][4];

    /*
     * Suits 0..3, Ranks 0..4, 3 cards, max card index 4*4+3 => 20 "cards"
     * so C(20,3) = 1140. Only used during creation.
     *
     */
    private static final int NUMISOBOARDS = 1140;

    /*
     * How each board maps to some isomorphic suit index
     */
    private final int boardToIsomorphic[] = new int[2 * NUMISOBOARDS];

    /*
     * The list of suit patterns we see are the only suit patterns mattering
     * with respect to this rank pattern.  Only used during enumeration, not lookup.
     */
    private final ArrayList<int[]> suitIsomorphisms = new ArrayList<int[]>();

    protected FlopSuit() {
        TableUtility.initArray(suitMap, BADENTRY);
        TableUtility.initArray(boardToIsomorphic, BADENTRY);
    }

    public int getSize() {
        return suitIsomorphisms.size();
    }

    protected int getIsomorphicSuitIndex(int p[]) {
        return suitMap[p[0]][p[1]][p[2]][p[3]][p[4]];
    }

    /**
     * @param ranks   Standard ranks
     * @param lowSuit Lowest isomorphic suit.
     * @return Unique index of the board wrt. hole suited or not
     */
    private int getBoardIndex(int ranks[], int lowSuit[]) {
        int[] board = TableUtility.getSortedBoard(ranks, lowSuit);
        int suited = (lowSuit[0] == lowSuit[1]) ? 1 : 0;

        int hidx = 0;
        for (int i = 0; i < 3; i++) {
            hidx += IntMath.binomial(board[i], i + 1);
            //hidx += Combinatorics.nchoosek(board[i], i+1);
        }

        // different if suited hole or not
        hidx += suited * NUMISOBOARDS;

        return hidx;
    }

    private int getBoardIsomorphicIndex(int ranks[], int suits[]) {
        int hidx = getBoardIndex(ranks, suits);

        return boardToIsomorphic[hidx];
    }

    private void setBoardIsomorphicIndex(int ranks[], int suits[], int index) {
        int hidx = getBoardIndex(ranks, suits);

        boardToIsomorphic[hidx] = index;
    }

    private int getSuitIsomorphicIndex(int s[]) {
        return suitMap[s[0]][s[1]][s[2]][s[3]][s[4]];
    }

    /**
     * @param s Suits
     * @param index
     */
    private void setSuitIsomorphicIndex(int s[], int index) {
        suitMap[s[0]][s[1]][s[2]][s[3]][s[4]] = index;
    }

    /*
     * Keep track for addSuit
     */
    private int isoSuitIndex = 0;

    /**
     * Pick out the suit patterns which work with the rank pattern.
     *
     * @param lowRanks Lowest rank of Hole and Board.
     * @param suits    Hole and Board.
     */
    private void addSuit(int lowRanks[], int suits[]) {
        int lowSuits[] = SuitUtility.lowestSuit(suits);

		/*
         *  See if suit pattern works on this board.
		 */
        if (!SuitUtility.isApplicable(lowRanks, lowSuits)) {
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

		/* 
		 * If this "isomorphic board" has been seen.
		 */
        int suitIsomorphicIndex = getBoardIsomorphicIndex(lowRanks, lowSuits);
        if (suitIsomorphicIndex != BADENTRY) {
            setSuitIsomorphicIndex(suits, suitIsomorphicIndex);

            if (paired) {
                setSuitIsomorphicIndex(reversedHoleSuits, suitIsomorphicIndex);
            }

            return;
        }

        int lowSuitIndex = getSuitIsomorphicIndex(lowSuits);
        // we haven't come across this suit pattern yet
        if (lowSuitIndex == BADENTRY) {
            setSuitIsomorphicIndex(lowSuits, isoSuitIndex);
            setSuitIsomorphicIndex(suits, isoSuitIndex);
            setBoardIsomorphicIndex(lowRanks, lowSuits, isoSuitIndex);

            if (paired) {
                setSuitIsomorphicIndex(reversedLowest, isoSuitIndex);
                setSuitIsomorphicIndex(reversedHoleSuits, isoSuitIndex);
                setBoardIsomorphicIndex(lowRanks, reversedLowest, isoSuitIndex);
            }

            suitIsomorphisms.add(lowSuits);
            isoSuitIndex++;
        } else {
            setSuitIsomorphicIndex(suits, lowSuitIndex);
            if (paired) {
                setSuitIsomorphicIndex(reversedHoleSuits, lowSuitIndex);
            }
        }
    }

    /**
     * Enumerate all potential suits to match to 'ranks'
     *
     * @param lowRanks
     */
    protected void enumSuits(int lowRanks[]) {
        int suits[] = new int[5];

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                for (int k = 0; k < 4; k++)
                    for (int l = 0; l < 4; l++)
                        for (int m = 0; m < 4; m++) {
                            suits[0] = i;
                            suits[1] = j;
                            suits[2] = k;
                            suits[3] = l;
                            suits[4] = m;

                            addSuit(lowRanks, suits);
                        }
    }
}