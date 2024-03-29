package se.hupoker.cards.cache;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.CardSet;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Alexander Nyberg
 *
 */
public class SingleManager {
    private final StandardHoleRelation relationManager;
	private final Set<HoleRelation> seen = new HashSet<>();

	public SingleManager(CardSet board) {
        relationManager = new StandardHoleRelation(board);
	}

    /**
     *
     * @param set
     * @return Have we seen this an isomorphic holecards before?
     */
	public boolean addIfNotExists(HoleCards set) {
		HoleRelation relation = relationManager.get(set);

		if (seen.contains(relation)) {
			return true;
		} else {
			seen.add(relation);
			return false;
		}
	}
}