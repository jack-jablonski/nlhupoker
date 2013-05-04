package se.hupoker.inference.holebucket;

import se.hupoker.cards.HoleCards;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The "static" preflop bucket (it never changes).
 *
 * @author Alexander Nyberg
 */
public class PreflopBucketMap extends HoleBucketMap {
    public PreflopBucketMap() {
        PreflopHoleReader preflop = PreflopHoleReader.getInstance();

        for (HoleCards hole : HoleCards.allOf()) {
            int bucketIndex = preflop.getBucket(hole);
            checkArgument(bucketIndex >= 0, "All preflop buckets should be valid.");

            setOnce(hole, bucketIndex);
        }

        verifyAllSet();
    }
}