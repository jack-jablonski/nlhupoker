package se.hupoker.inference.holebucket;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.CardSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 *
 * @author Alexander Nyberg
 */
class RiverBucketMap extends HoleBucketMap {
    protected RiverBucketMap(CardSet board) {
        RiverHoleReader river = RiverHoleReader.getInstance();
        checkArgument(board.size() == 5, "River cards");

		/*
         * TODO: Can be speed up alot by doing batch reads.
		 */
        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                setNotValid(hole);
                continue;
            }

            int bucketIndex = river.getBucketIndex(board, hole);
            setOnce(hole, bucketIndex);
        }

        //print(board, river.size());

        verifyAllSet();
        checkState(numberSet() == 1081);
    }
}