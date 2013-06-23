package se.hupoker.inference.tree;

import se.hupoker.cards.CardSet;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.cards.HoleCards;
import se.hupoker.poker.PositionMap;
import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.inference.handinformation.HolePossible;
import se.hupoker.inference.states.PathElement;

import java.util.Map;

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
    public void evaluateAtNode(StateNode state, HandInfo hand, PathElement elem) {
        CardSet board = hand.getBoard(elem.getStreet());
        Map<HoleCards, ActionDistribution> actionDistributionMap = state.getDistribution(board);
        Gradient grad = gradients.get(elem.getPosition());

        HolePossible holePossible = hand.getHolePossible(elem.getPosition());
        for (HoleCards hole : holePossible) {
            ActionDistribution actionDist = actionDistributionMap.get(hole);

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