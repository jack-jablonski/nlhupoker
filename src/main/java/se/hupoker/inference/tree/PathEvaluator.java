package se.hupoker.inference.tree;

import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.inference.states.PathElement;

/**
 *
 * @author Alexander Nyberg
 */
public interface PathEvaluator {
    /**
     * @param hand
     * @param elem
     */
    void evaluateAtNode(StateNode node, HandInfo hand, PathElement elem);
}
