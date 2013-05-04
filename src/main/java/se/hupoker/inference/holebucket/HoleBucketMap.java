package se.hupoker.inference.holebucket;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardisomorphisms.SingleManager;
import se.hupoker.common.Street;
import se.hupoker.cards.CardSet;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Maps holecards on a given board into a number of buckets where the holecards
 * in each bucket share similar properties.
 *
 * f: HoleCards -> Bucket
 *
 * @author Alexander Nyberg
 */
public abstract class HoleBucketMap {
    private final Map<HoleCards, Integer> map = new HashMap<>();

    public static HoleBucketMap factory(Street street, CardSet set) {
        switch (street) {
            case PREFLOP:
                return new PreflopBucketMap();
            case FLOP:
                return new FlopBucketMap(set);
            case TURN:
                return new TurnBucketMap(set);
            case RIVER:
                return new RiverBucketMap(set);
        }

        throw new IllegalArgumentException("Unknown street: " + street);
    }

    protected final void setOnce(HoleCards hole, int bucketIndex) {
        checkArgument(!map.containsKey(hole));
        map.put(hole, bucketIndex);
    }

    // TODO: Mark entry as erroneous
    protected void setNotValid(HoleCards hole) {
//        map.put(hole, -1);
    }

    // TODO: Should verify all legitimate are set
    protected void verifyAllSet() {
//        throw new UnsupportedOperationException();
    }

    protected final int numberSet() {
        return map.size();
    }

    /**
     * @param hole
     * @return Index into HoleCluster
     */
    public final Integer get(HoleCards hole) {
        checkArgument(map.containsKey(hole));
        return map.get(hole);
    }

    /**
     * For debugging, print the bucketing.
     *
     * @param board
     * @param numBuckets
     */
    protected void print(CardSet board, int numBuckets) {
        System.out.println("BOARD:" + board);

        for (int bucketIndex = 0; bucketIndex < numBuckets; bucketIndex++) {
            System.out.println("======== BUCKET #" + bucketIndex);
            SingleManager manager = new SingleManager(board);

            for (HoleCards hole : HoleCards.allOf()) {
                if (get(hole) == bucketIndex) {
                    if (!manager.addIfNotExists(hole)) {
                        System.out.println(hole);
                    }
                }
            }
        }
    }
}