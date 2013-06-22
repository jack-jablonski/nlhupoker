package se.hupoker.cards.cache;

import com.google.common.base.Optional;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.CardSet;

/**
 * Isomorphic (or near) Pair of hole cards on certain board.
 * 
 * @author Alexander Nyberg
 *
 */ 
public class ApproximationCache {
	private final StandardHoleRelation relationManager;
    private final SymmetricRelationTable cache = new SymmetricRelationTable();
    private int cacheMiss, cacheHit;

	private ApproximationCache(StandardHoleRelation relationManager) {
        this.relationManager = relationManager;
	}

    public static ApproximationCache create(CardSet board) {
        StandardHoleRelation relationManager = new StandardHoleRelation(board);
        return new ApproximationCache(relationManager);
    }

    /**
	 * 
	 * @param holeOne
	 * @param holeTwo
	 * @return Entry in cache (previous or just created)
	 */
	public Pair<HoleCards> getAndAddCached(HoleCards holeOne, HoleCards holeTwo) {
        HoleRelation first = relationManager.get(holeOne);
        HoleRelation sec = relationManager.get(holeTwo);

		Optional<Pair<HoleCards>> pair = cache.get(first, sec);
		if (pair.isPresent()) {
            cacheHit++;
			return pair.get();
		} else {
            cacheMiss++;
			cache.add(holeOne, holeTwo, first, sec);
			return null;
		}
	}

    public int getCacheMiss() {
        return cacheMiss;
    }

    public int getCacheHit() {
        return cacheHit;
    }
}