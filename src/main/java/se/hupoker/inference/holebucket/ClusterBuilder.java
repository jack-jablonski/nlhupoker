package se.hupoker.inference.holebucket;

/**
 * @author Alexander Nyberg
 */
public interface ClusterBuilder<T> {
    /**
     *
     * @return Immutable built cluster.
     */
    T build();
}