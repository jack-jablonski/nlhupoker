package se.hupoker.inference.holebucket;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.CardSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 *
 * @author Alexander Nyberg
 */
class FlopBucketMap extends HoleBucketMap {
    protected FlopBucketMap(CardSet board) {
        FlopHoleReader reader = FlopHoleReader.getInstance();
        checkArgument(board.size() == 3, "Flop always 3 cards");

		/*
         * TODO: Can be speed up alot by doing batch reads.
		 */
        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                setNotValid(hole);
                continue;
            }

            int bucketIndex = reader.getBucketIndex(board, hole);
            setOnce(hole, bucketIndex);
        }

        verifyAllSet();
        checkState(numberSet() == 1176);

        //print(board, FlopHoleReader.size());
    }
}