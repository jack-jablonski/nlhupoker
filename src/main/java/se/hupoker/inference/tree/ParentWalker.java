package se.hupoker.inference.tree;

/**
 * @author Alexander Nyberg
 */
public interface ParentWalker {
    /**
     * Initial parent (root) is BaseLevel.
     *
     * Walk the tree along some path and run given function over the node.
     */
    void run(Object parent, StateNode child);

    void addRoot(Object object);
}
