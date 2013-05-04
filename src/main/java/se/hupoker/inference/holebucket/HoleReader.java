package se.hupoker.inference.holebucket;

import se.hupoker.inference.states.GenericState;

/**
 * @author Alexander Nyberg
 */
public interface HoleReader {
    /**
     * @param descriptor
     * @return The distribution for this node.
     */
    public HoleCluster getBucketMap(GenericState descriptor);
}