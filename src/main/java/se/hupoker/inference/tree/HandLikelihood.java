package se.hupoker.inference.tree;

import se.hupoker.cards.CardSet;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.cards.HoleCards;
import se.hupoker.poker.Position;
import se.hupoker.poker.PositionMap;
import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.inference.handinformation.HolePossible;
import se.hupoker.inference.states.PathElement;
import se.hupoker.inference.vectors.HoleDistribution;

import java.util.Map;

/**
 *
 * Find the log likelihood of seeing the given hand.
 *
 * @author Alexander Nyberg
 *
 */
class HandLikelihood implements PathEvaluator {
	private final PositionMap<HoleDistribution> playerDistribution;
	private double handProbability = 0;

	private HandLikelihood(HandInfo hand, PositionMap<HoleDistribution> playerDistribution)  {
		this.playerDistribution = playerDistribution;
	}

    public static HandLikelihood create(HandInfo hand) {
        PositionMap<HoleDistribution> distribution = initialDistribution(hand);
        return new HandLikelihood(hand, distribution);
    }

    /**
	 *
	 * @return
	 */
	private static PositionMap<HoleDistribution> initialDistribution(HandInfo hand) {
		PositionMap<HoleDistribution> dist = new PositionMap<>();

		for (Position pos : Position.values()) {
			HolePossible hp = hand.getHolePossible(pos);
			dist.put(pos, new HoleDistribution(hp));
		}

		return dist;
	}

    public double getHandProbability() {
        return handProbability;
    }

    /**
     * @param hand
     * @param hd  Hole Card distribution.
     * @return Probability of act in this node.
     */
    private double findActionProbability(StateNode state, HandInfo hand, HoleDistribution hd, PathElement elem) {
        CardSet board = hand.getBoard(elem.getStreet());
        Map<HoleCards, ActionDistribution> actionDistributionMap = state.getDistribution(board);
        double sum = 0;

		/*
		 * Iterate over all hole cards.
		 */
        HolePossible holePossible = hand.getHolePossible(elem.getPosition());
        for (HoleCards hole : holePossible) {
            ActionDistribution dist = actionDistributionMap.get(hole);

            double cardprob = hd.get(hole.ordinal()) * dist.getProbability(elem.getAction());
            sum += cardprob;
        }

        return sum;
    }

    /**
     *
     * @param hand
     * @param hd
     * @param elem
     */
    private void updateHoleDistribution(StateNode state, HandInfo hand, HoleDistribution hd, PathElement elem) {
        CardSet board = hand.getBoard(elem.getStreet());
        Map<HoleCards, ActionDistribution> actionDistributionMap = state.getDistribution(board);

        HolePossible holePossible = hand.getHolePossible(elem.getPosition());
        for (HoleCards hole : holePossible) {
            ActionDistribution dist = actionDistributionMap.get(hole);

            /**
             *
             */
            double takenActionLikelihood = dist.getProbability(elem.getAction());
            hd.multiplyOne(hole.ordinal(), takenActionLikelihood);
        }

        hd.normalize();
    }

	@Override
	public void evaluateAtNode(StateNode state, HandInfo hand, PathElement elem) {
        HoleDistribution dist = playerDistribution.get(elem.getPosition());

		double actionProbability = findActionProbability(state, hand, dist, elem);
		updateHoleDistribution(state, hand, dist, elem);

		handProbability += Math.log(actionProbability);
	}
}