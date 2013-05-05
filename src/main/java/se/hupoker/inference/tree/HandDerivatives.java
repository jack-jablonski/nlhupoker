package se.hupoker.inference.tree;

import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.inference.states.PathHistory;

/**
 * Calculate & Update the gradients of each node in the tree.
 *
 * @author Alexander Nyberg
 */
class HandDerivatives implements Runnable {
    private final HandInfo handInfo;
    private final GameTree tree;

    public HandDerivatives(GameTree tree, HandInfo handInfo) {
        this.tree = tree;
        this.handInfo = handInfo;
    }

    @Override
    public void run() {
        PathHistory path = handInfo.getPath();

        BuildGradient buildGradient = new BuildGradient(handInfo);
        tree.walkNodeTreePath(handInfo, path, buildGradient);

        CalculateGradient calculateGradient = new CalculateGradient(buildGradient.getGradients());
        tree.walkNodeTreePath(handInfo, path, calculateGradient);
    }
}
