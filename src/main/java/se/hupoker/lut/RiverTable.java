package se.hupoker.lut;

/**
 * @author Alexander Nyberg
 */
public final class RiverTable extends LutTable {
    private static final int RIVERCARDS = 7;
    private static final int TABLESIZE = 52402675;

    // The different suit patterns matching to each rank pattern. [number of rank patterns]
    private static final int NUMRANKISOMORPHISMS = 214;
    private RiverSuit rankIsomorphicSuits[] = new RiverSuit[NUMRANKISOMORPHISMS];
    // Says to which rank pattern this rank belongs.
    private int[][][][][][][] rankIsomorphicIndex = new int[7][7][7][7][7][7][7];
    // how many ranks that compress into a certain rank pattern [number of rank patterns]
    private int[] countRankIsomorphic = new int[NUMRANKISOMORPHISMS];

    /*
     * nchoosek(13+5-1, 5) * nchoosek(13+2-1, 2)
     * CR(13,5) * CR(13,2)
     *
     * Includes 5 of a kind...
     */
    private final int NUMRANKCOMBINATIONS = 563199;
    // Gives each rank a unique index with each rank pattern [number of ranks]
    private int[] rankFoldMap = new int[NUMRANKCOMBINATIONS];
    // Says to which rank pattern index this rank belongs to [number of ranks]
    private int[] rankIsomorphismMap = new int[NUMRANKCOMBINATIONS];

    public RiverTable() {
        super(TABLESIZE);

        TableUtility.initArray(rankIsomorphicIndex, BADENTRY);

        enumerateHole();
    }

    public static RiverTable create(String source) {
        RiverTable table = new RiverTable();
        table.load(source);
        return table;
    }

    @Override
    protected int getIndex(LutKey in) {
        //debug(board.toString() + "|" + hole.toString());
        //debug(Arrays.toString(ranks));
        //debug(Arrays.toString(suits));
        //debug(Arrays.toString(SuitIso.lowestSuit(suits)));

        return tableIndex(in);
    }

    private int tableIndex(LutKey in) {
        int rankIndex = RiverRank.handRankIndex(in.getRanks());
        int rankIsomorphicIndex = rankIsomorphismMap[rankIndex];

        int offset = 0;
        for (int i = 0; i < rankIsomorphicIndex; i++) {
            offset += countRankIsomorphic[i] * rankIsomorphicSuits[i].getSize();
        }

        int inside = rankFoldMap[rankIndex] * rankIsomorphicSuits[rankIsomorphicIndex].getSize();

        int suitIsoIndex = rankIsomorphicSuits[rankIsomorphicIndex].getPatternIndex(in.getSuits());

        int index = offset + inside + suitIsoIndex;

        debug("" + index);

        return index;
    }

    /*
     * The current smallest un-used rank isomorphism index
     */
    private int currentRankIsomorphism = 0;

    private void enumerateSuits(int[] Rank) {
        int lo[] = RankUtility.lowestRank(Rank);
        int rankIsoIndex = rankIsomorphicIndex[lo[0]][lo[1]][lo[2]][lo[3]][lo[4]][lo[5]][lo[6]];

		/*
         * Haven't come upon this 'lowest rank pattern' yet, add the suits to it.
		 */
        if (rankIsoIndex == BADENTRY) {
            RiverSuit riversuit = new RiverSuit();
            riversuit.enumerateSuits(lo);

            rankIsomorphicIndex[lo[0]][lo[1]][lo[2]][lo[3]][lo[4]][lo[5]][lo[6]] = currentRankIsomorphism;

			/*
			 * Set back variables
			 */
            rankIsomorphicSuits[currentRankIsomorphism] = riversuit;
            rankIsoIndex = currentRankIsomorphism;
            currentRankIsomorphism++;
        }

        int rankIndex = RiverRank.handRankIndex(Rank);

        rankFoldMap[rankIndex] = countRankIsomorphic[rankIsoIndex];
        countRankIsomorphic[rankIsoIndex]++;
        rankIsomorphismMap[rankIndex] = rankIsoIndex;
    }

    private void enumerateBoard(int ranks[]) {
        for (int i = 0; i < 13; i++) {
            for (int j = i; j < 13; j++) {
                for (int k = j; k < 13; k++) {
                    for (int l = k; l < 13; l++) {
                        for (int m = l; m < 13; m++) {
                            ranks[2] = i;
                            ranks[3] = j;
                            ranks[4] = k;
                            ranks[5] = l;
                            ranks[6] = m;

                            //	skip 5 of a kind
                            if (RankUtility.countMax(ranks) > 4) {
                                continue;
                            }

                            enumerateSuits(ranks);
                        }
                    }
                }
            }
        }
    }

    private void enumerateHole() {
        int ranks[] = new int[RIVERCARDS];

        for (int i = 0; i < 13; i++) {
            for (int j = i; j < 13; j++) {
                ranks[0] = i;
                ranks[1] = j;

                enumerateBoard(ranks);
            }
        }
    }
}