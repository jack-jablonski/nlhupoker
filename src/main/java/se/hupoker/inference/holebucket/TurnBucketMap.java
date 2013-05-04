package se.hupoker.inference.holebucket;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.CardSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author Alexander Nyberg
 */
class TurnBucketMap extends HoleBucketMap {
    protected TurnBucketMap(CardSet board) {
        TurnHoleReader turn = TurnHoleReader.getInstance();
        checkArgument(board.size() == 4, "Turn cards");

		/*
         * TODO: Can be speed up alot by doing batch reads.
		 */
        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                setNotValid(hole);
                continue;
            }

            int bucketIndex = turn.getBucketIndex(board, hole);
            setOnce(hole, bucketIndex);
        }

        verifyAllSet();
        checkState(numberSet() == 1128);

        //print(board, TurnHoleReader.size());
    }
}