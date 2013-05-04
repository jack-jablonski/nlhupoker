package se.hupoker.inference.tree;

import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.cards.HoleCards;
import se.hupoker.common.PositionMap;
import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.inference.handinformation.HolePossible;
import se.hupoker.inference.states.PathElement;
import se.hupoker.inference.holebucket.HoleBucketMap;

/**
 * Update the action distributions according to the gradient.
 *
 * @author Alexander Nyberg
 */
class CalculateGradient implements PathEvaluator {
    private final PositionMap<Gradient> gradients;

    public CalculateGradient(PositionMap<Gradient> g) {
        gradients = g;
    }

    @Override
    public void evaluateAtNode(StateNode node, HandInfo hand, PathElement elem) {
        final HoleBucketMap bucketMap = hand.getBucketMap(elem.getStreet());
        Gradient grad = gradients.get(elem.getPosition());

        HolePossible holePossible = hand.getHolePossible(elem.getPosition());
        for (HoleCards hole : holePossible) {
            ActionDistribution actionDist = node.getDistribution(bucketMap, hole);

			/**
			 * Core update
			 */
            double takenActionLikelihood = actionDist.getProbability(elem.getAction());
            if (takenActionLikelihood > 0) {
                double adder = grad.getActionProbability(hole.ordinal()) / takenActionLikelihood;
                adder /= grad.getSum();

                actionDist.addDerivative(elem.getAction(), adder);
            }
        }
    }
}