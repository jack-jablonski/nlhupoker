package se.hupoker.inference.tree;

import se.hupoker.inference.handinformation.HandInfo;

import java.util.Collection;

/**
 * @author Alexander Nyberg
 */
public interface TreeOptimizer {
    /**
     * Update gradients over given list.
     *
     * @param list
     */
    void optimize(Collection<HandInfo> list);
}
