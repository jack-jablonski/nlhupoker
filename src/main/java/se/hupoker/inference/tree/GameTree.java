package se.hupoker.inference.tree;

import se.hupoker.common.Street;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.inference.states.PathElement;
import se.hupoker.inference.states.PathHistory;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Root of the tree!
 *
 * @author Alexander Nyberg
 */
public class GameTree {
    private final StreetStateSet root;

    public GameTree(StreetStateSet preFlopStateSet) {
        root = preFlopStateSet;
    }

    public void walkParentsFromRoot(ParentWalker parentWalker) {
        parentWalker.addRoot(root);
        walkParentTree(root, root, parentWalker);
    }

    private void walkParentTree(Object parent, StreetStateSet sb, ParentWalker walker) {
        for (StateNode node : sb) {
            walker.run(parent, node);

            StreetStateSet child = node.getChild();
            if (child != null) {
                walkParentTree(node, child, walker);
            }
        }
    }

    /**
     * Entry point for Markov-style tree walk.
     *
     * @param walker
     */
    public void walkNodesFromRoot(NodeWalker walker) {
        walkNodeTree(root, walker);
    }

    /**
     * Walks the entire tree.
     *
     * @param sb
     * @param walker
     */
    private void walkNodeTree(StreetStateSet sb, NodeWalker walker) {
        for (StateNode node : sb) {
            walker.run(node);

            StreetStateSet child = node.getChild();
            if (child != null) {
                walkNodeTree(child, walker);
            }
        }
    }

    /**
     * Walk the nodes according to the path of the hand.
     *
     * @param hand
     * @param pathHistory
     * @param pw
     */
    protected void walkNodeTreePath(HandInfo hand, PathHistory pathHistory, PathEvaluator pw) {
        StreetStateSet base = root;

        for (Street street : Street.values()) {
            List<PathElement> path = pathHistory.get(street);
            if (path.isEmpty()) {
                break;
            }

            checkNotNull(base);

            StateNode last = null;
            for (PathElement elem : path) {
                last = base.findNode(elem);

                checkArgument(elem.getStreet() == street);
                checkArgument(last.getCenter().getPosition() == elem.getPosition());
                checkArgument(last.getCenter().getStreet() == elem.getStreet());
                pw.evaluateAtNode(last, hand, elem);
            }
            base = last.getChild();
        }
    }

    /**
     * Update the derivatives of each node!
     */
    protected void updateDerivatives() {
        final NodeWalker updater = new NodeWalker() {
            @Override
            public void run(StateNode node) {
                node.logDerivatives();
                for (ActionDistribution ad : node.getHoleClusterer()) {
                    ad.adjustAndClearDerivatives();
                }
                node.logProbablities();
            }
        };

        walkNodesFromRoot(updater);
    }

    /**
     * @return Log likelihood of seeing this action sequence.
     */
    private double handLikelihood(HandInfo hand) {
        HandLikelihood hd = new HandLikelihood(hand);

        walkNodeTreePath(hand, hand.getPath(), hd);

        return hd.getHandProbability();
    }

    /**
     * @param list
     * @return
     */
    public double logLikelihood(Collection<HandInfo> list) {
        double sum = 0;

        for (HandInfo he : list) {
            double value = handLikelihood(he);

            sum += value;
        }

        return sum;
    }

    public void addForStatistics(Collection<HandInfo> hands) {
        final PathEvaluator statistics = new PathEvaluator() {
            @Override
            public void evaluateAtNode(StateNode node, HandInfo hand, PathElement elem) {
                node.addVisit(hand, elem);
            }
        };

        for (HandInfo hand : hands) {
            walkNodeTreePath(hand, hand.getPath(), statistics);
        }
    }

    public void print() {
//        public void walkNodesFromRoot(NodeWalker fu) {
        System.out.println("==============================================================");
        root.print("");
    }
}