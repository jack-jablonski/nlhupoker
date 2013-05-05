package se.hupoker.lut;

import java.util.Arrays;

/**
 *
 * @author Alexander Nyberg
 */
final public class FlopTable extends LutTable {
    private static final int FLOPCARDS = 5;
    private static final int TABLESIZE = 1361802;

    /*
     *  The number of (hole,board) rank isomorphisms.
     */
    private final int NUMRANKISOMORHPISMS = 36;
    private final FlopSuit rankIsomorphicSuits[] = new FlopSuit[NUMRANKISOMORHPISMS];

    /*
     * How many unique ranks that compress into a certain isomorphic rank pattern
     */
    private final int[] countRankIsomorphic = new int[NUMRANKISOMORHPISMS];

    /*
     * The index of this 'isomorphic rank'
     */
    private final int[][][][][] rankIsomorphicIndex = new int[5][5][5][5][5];

    /*
     * Gives each rank a unique position within the rank pattern it belongs to.
     *
     * Number of hole rank indices Multichoose(13, 2)
     * Number of board rank indices Multichoose(13, 3)
     *
     * Also includes "5 of a kind".
     *
     * NUMRANKCOMBINATIONS = (# hole rank indices) * (# board rank indices)
     */
    private final int NUMRANKCOMBINATIONS = 41405;

    /*
     * Keep track of where in this rank isomorphism to find index.
     */
    private final int[] rankFoldMap = new int[NUMRANKCOMBINATIONS];

    /*
     * Map: Unique rank index => Rank isomorphism index
     * [number of ranks]
     */
    private int[] rankIsomorphismMap = new int[NUMRANKCOMBINATIONS];

    public FlopTable() {
        super(TABLESIZE);

        TableUtility.initArray(rankIsomorphicIndex, BADENTRY);

        enumerateHole();
    }

    public static FlopTable create(String source) {
        FlopTable table = new FlopTable();
        table.load(source);
        return table;
    }

    /**
     * @param lutKey
     * @return The final index into LUT.
     */
    @Override
    protected int getIndex(LutKey lutKey) {
        //debug(board.toString() + "|" + hole.toString());
        //debug(Arrays.toString(ranks));
        //debug(Arrays.toString(suits));
        //debug(Arrays.toString(SuitUtility.lowestSuit(suits)));

        return tableIndex(lutKey);
    }

    /**
     * @param lutKey
     * @return The big table uniquely identifying the isomorphic (hole, board) pair
     */
    private int tableIndex(LutKey lutKey) {
        int rankIndex = FlopRank.uniqueRankIndex(lutKey.getRanks());
        int rankIsomorphicIndex = rankIsomorphismMap[rankIndex];

		/*
         * Offset to get inside this rank isomorphism range
		 */
        int offset = 0;
        for (int i = 0; i < rankIsomorphicIndex; i++) {
            offset += countRankIsomorphic[i] * rankIsomorphicSuits[i].getSize();
        }

		/*
		 * Inside this rank isomorphism range
		 */
        int inside = rankFoldMap[rankIndex] * rankIsomorphicSuits[rankIsomorphicIndex].getSize();

		/*
		 * This suit isomorphism
		 */
        int suitIsoIndex = rankIsomorphicSuits[rankIsomorphicIndex].getIsomorphicSuitIndex(lutKey.getSuits());

		/*
		 * int index = offset + inside + suitIsoIndex;
		 */
        int index = offset + inside + suitIsoIndex;

        debug("" + index);

        return index;
    }

    /*
     * The current smallest un-used 'lowest rank isomorphism' index
     */
    private int currentRankIsomorphism = 0;

    /**
     * Count and generate indexing tables.
     *
     * @param Rank Unique rank combination of [h1, h2, b1, b2, b3].
     */
    private void enumerateSuits(int[] Rank) {
        int lo[] = RankUtility.lowestRank(Rank);
        int rankIsoIndex = rankIsomorphicIndex[lo[0]][lo[1]][lo[2]][lo[3]][lo[4]];

		/*
		 *  If we haven't seen this rank pattern yet, add it.
		 */
        if (rankIsoIndex == BADENTRY) {
            FlopSuit flopSuit = new FlopSuit();
            flopSuit.enumSuits(lo);

            // Set newly found rank isomorphism to fresh index.
            rankIsomorphicIndex[lo[0]][lo[1]][lo[2]][lo[3]][lo[4]] = currentRankIsomorphism;

            debug(Arrays.toString(lo));
            debug("Num suits:" + flopSuit.getSize() + " rankindex:" + currentRankIsomorphism);

			/*
			 * Set back variables
			 */
            rankIsomorphicSuits[currentRankIsomorphism] = flopSuit;
            rankIsoIndex = currentRankIsomorphism;
            currentRankIsomorphism++;
        }

        int rankIndex = FlopRank.uniqueRankIndex(Rank);

        // Keep track where in this rank isomorphism range this was found
        rankFoldMap[rankIndex] = countRankIsomorphic[rankIsoIndex];
        countRankIsomorphic[rankIsoIndex]++;
        rankIsomorphismMap[rankIndex] = rankIsoIndex;
    }

    private void enumerateBoard(int[] Rank) {
        for (int k = 0; k < 13; k++) {
            for (int l = k; l < 13; l++) {
                for (int m = l; m < 13; m++) {
                    Rank[2] = k;
                    Rank[3] = l;
                    Rank[4] = m;

                    // no 5 of a kind please
                    if (RankUtility.countMax(Rank) > 4) {
                        continue;
                    }

                    enumerateSuits(Rank);
                }
            }
        }
    }

    private void enumerateHole() {
        int[] Rank = new int[FLOPCARDS];

        for (int i = 0; i < 13; i++) {
            for (int j = i; j < 13; j++) {
                Rank[0] = i;
                Rank[1] = j;

                enumerateBoard(Rank);
            }
        }
    }
}