package se.hupoker.lut;


/**
 * 
 * @author Alexander Nyberg
 *
 */
final class SuitUtility {
	/**
	 *
	 * @param suits
	 * @return The number of ranks of the most common rank.
	 */
	public static int countMax(int suits[]) {
		int count[] = suitCount(suits);
        return TableUtility.maximumOccurrence(count);
	}

    /**
     *
     * @param suits
     * @return The number of suits of each type.
     */
    public static int[] suitCount(int suits[]) {
        int count[] = new int [4];

        for (int suit : suits) {
            count[suit]++;
        }

        return count;
    }

	/**
	 *
	 * @param suits
	 * @return Isomorphic lowest suit pattern.
	 */
	public static int [] lowestSuit(int suits[]) {
		int map[] = new int[] {-1,-1,-1,-1};
		int isoSuit[] = new int[suits.length];

		int currentSuit=0;

		for (int i=0; i < suits.length; i++) {
			if (map[suits[i]] == -1) {
				map[suits[i]] = currentSuit;
				currentSuit++;
			}
			isoSuit[i] = map[suits[i]];
		}
		return isoSuit;
	}

	/**
	 *
	 * In the iso model ranks 0..ncards-1, suits 0..3
	 * Check if there are any duplicate cards.
	 *
	 * @param ranks
	 * @param suits
	 * @return Can this suit be applied to ranks?
	 */
	public static boolean isApplicable(int ranks[], int suits[]) {
		int ncards = ranks.length;

		/*
		 * Compare C(ncards, 2) iterations for identical hands.
		 */
		for (int i=0; i < ncards; i++) {
			for (int j=i+1; j < ncards; j++) {
				if (ranks[i]==ranks[j] && suits[i]==suits[j]) {
					return false;
				}
			}
		}

		return true;
	}
}