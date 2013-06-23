package se.hupoker.inference.tree;

import se.hupoker.cards.CardSet;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.cards.HoleCards;
import se.hupoker.poker.Position;
import se.hupoker.poker.PositionMap;
import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.inference.handinformation.HolePossible;
import se.hupoker.inference.states.PathElement;

import java.util.Map;

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
     * @param state Where in the tree we are.
     * @param hand The current poker hand.
     * @param elem Describes the action.
     */
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
            grad.addActionProbability(hole.ordinal(), takenActionLikelihood);
        }
    }
}