package se.hupoker.inference.holebucket;

/**
 *
 * @author Alexander Nyberg
 *
 */
class BucketKey {
    private final int key;

    public BucketKey(int key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof BucketKey)) {
            return false;
        }

        final BucketKey otherKey = (BucketKey) other;
        return key == otherKey.key;
    }

    @Override
    public int hashCode() {
        return key;
    }
}