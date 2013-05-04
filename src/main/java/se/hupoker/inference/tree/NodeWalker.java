package se.hupoker.inference.tree;

/**
 * @author Alexander Nyberg
 */
interface NodeWalker {
    /**
     * Walk the tree along some path and run given function over the node.
     *
     * @param node
     */
    void run(StateNode node);
}
