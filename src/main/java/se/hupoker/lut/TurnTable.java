package se.hupoker.lut;

/**
 * 
 * @author Alexander Nyberg
 *
 */
final public class TurnTable extends LutTable {
	private static final int TURNCARDS = 6;		
	private static final int TABLESIZE = 15111642;

	 // The different suit patterns matching to each rank pattern. [number of rank patterns]
	private static final int NUMRANKISOMORPHISMS = 89;
	private TurnSuit rankPatternSuits[] = new TurnSuit[NUMRANKISOMORPHISMS];
	 // Says to which rank pattern this rank belongs.
	private int[][][][][][] rankPatternIndex = new int[6][6][6][6][6][6];

	// how many ranks that compress into a certain rank pattern [number of rank patterns]
	private int[] numRankPattern = new int[NUMRANKISOMORPHISMS];
	
	/*
	 * nchoosek(13+4-1,4) * nchoosek(13+2-1,2)
	 */
	private static final int NUMRANKCOMBINATIONS = 165620;

	// Gives each rank a unique index with each rank pattern [number of rank indices]
	private int[] rankPositionMap = new int[NUMRANKCOMBINATIONS];
	// Says to which rank pattern index this rank belongs to [number of ranks]
	private int[] rankIndexMap = new int[NUMRANKCOMBINATIONS];

	public TurnTable() {
		super(TABLESIZE);

		TableUtility.initArray(rankPatternIndex, BADENTRY);

		enumerateHole();
	}

    public static TurnTable create(String source) {
        TurnTable table = new TurnTable();
        table.load(source);
        return table;
    }

	@Override
	protected int getIndex(LutKey in) {
		return tableIndex(in.getRanks(), in.getSuits());
	}

	/**
	 * 
	 * @param ranks
	 * @param suits
	 * @return Index into table.
	 */
	private int tableIndex(int ranks[], int suits[]) {
		int rankIndex = TurnRank.handRankIndex(ranks);
		int rankIsoIndex = rankIndexMap[rankIndex];

		int offset = 0;
		for (int i=0; i < rankIsoIndex; i++) {
			offset += numRankPattern[i] * rankPatternSuits[i].getSize();
		}

		int inside = rankPositionMap[rankIndex] * rankPatternSuits[rankIsoIndex].getSize();
		int suitIndex = rankPatternSuits[rankIsoIndex].getPatternIndex(suits);
		int index = offset + inside + suitIndex;

		return index;
	}

	/*
	 * The current smallest un-used rank isomorphism index
	 */
	private int currentRankIsomorphism=0;

	/**
	 * 
	 * @param ranks
	 */
	private void enumerateSuits(int[] ranks) {
		int lo[] = RankUtility.lowestRank(ranks);
		int rankIsoIndex = rankPatternIndex[lo[0]][lo[1]][lo[2]][lo[3]][lo[4]][lo[5]];

		// Haven't come upon this rank pattern yet, add it.
		if (rankIsoIndex == BADENTRY) {
			TurnSuit suit = new TurnSuit();

			rankPatternIndex[lo[0]][lo[1]][lo[2]][lo[3]][lo[4]][lo[5]] = currentRankIsomorphism;

			suit.enumSuits(lo);

			//TableUtility.printArray("", r);
			//System.out.println("Num suits:" + t.getSize() + " rankindex:" + rankPatternCount);

			rankPatternSuits[currentRankIsomorphism] = suit;
			rankIsoIndex = currentRankIsomorphism;
			currentRankIsomorphism++;
		}

		int rankidx = TurnRank.handRankIndex(ranks);

		rankPositionMap[rankidx] = numRankPattern[rankIsoIndex];
		rankIndexMap[rankidx] = rankIsoIndex;

		numRankPattern[rankIsoIndex]++;
	}

	private void enumerateBoard(int[] ranks) {
    	for(int i=0; i < 13; i++)
        	for(int j=i; j < 13; j++)
        		for (int k=j; k < 13; k++)
        			for (int l=k; l < 13; l++) {
        				ranks[2] = i;
        				ranks[3] = j;
        				ranks[4] = k;
        				ranks[5] = l;

       					//	skip 5 of a kind
    					if (RankUtility.countMax(ranks) > 4) {
    						continue;
    					}

       					enumerateSuits(ranks);
        			}
	}

	private void enumerateHole() {
		int[] ranks = new int[TURNCARDS];

		for (int i = 0; i < 13; i++) {
			for (int j = i; j < 13; j++) {
				ranks[0] = i;
				ranks[1] = j;

				enumerateBoard(ranks);
			}
		}
	}
}