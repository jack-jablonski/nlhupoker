package se.hupoker.inference.tree;

import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.cards.HoleCards;
import se.hupoker.common.Position;
import se.hupoker.common.PositionMap;
import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.inference.handinformation.HolePossible;
import se.hupoker.inference.states.PathElement;
import se.hupoker.inference.holebucket.HoleBucketMap;

/**
 * @author Alexander Nyberg
 */
class BuildGradient implements PathEvaluator {
    private final PositionMap<Gradient> gradients = new PositionMap<>();

    public PositionMap<Gradient> getGradients() {
        return gradients;
    }

    public BuildGradient(HandInfo hand) {
        for (Position pos : Position.values()) {
            HolePossible hp = hand.getHolePossible(pos);

            gradients.put(pos, new Gradient(hp));
        }
    }

    /**
     * Update the gradient according to the actions taken
     *
     * @param node Where in the tree we are.
     * @param hand The current poker hand.
     * @param elem Describes the action.
     */
    @Override
    public void evaluateAtNode(StateNode node, HandInfo hand, PathElement elem) {
        HoleBucketMap bucketMap = hand.getBucketMap(elem.getStreet());
        Gradient grad = gradients.get(elem.getPosition());

        HolePossible holePossible = hand.getHolePossible(elem.getPosition());
        for (HoleCards hole : holePossible) {
            ActionDistribution actionDist = node.getDistribution(bucketMap, hole);

            /**
             * Core update
             */
            double takenActionLikelihood = actionDist.getProbability(elem.getAction());
            grad.addActionProbability(hole.ordinal(), takenActionLikelihood);
        }
    }
}