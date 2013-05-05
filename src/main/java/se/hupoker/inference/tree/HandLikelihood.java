package se.hupoker.inference.tree;

import se.hupoker.cards.CardSet;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.cards.HoleCards;
import se.hupoker.common.Position;
import se.hupoker.common.PositionMap;
import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.inference.handinformation.HolePossible;
import se.hupoker.inference.states.PathElement;
import se.hupoker.inference.vectors.HoleDistribution;

/**
 *
 * Find the log likelihood of seeing the given hand.
 *
 * @author Alexander Nyberg
 *
 */
class HandLikelihood implements PathEvaluator {
	private final HandInfo hand;
	private final PositionMap<HoleDistribution> playerDistribution;
	private double handProbability = 0;

    public double getHandProbability() {
        return handProbability;
    }

	public HandLikelihood(HandInfo hand)  {
		this.hand = hand;
		playerDistribution = initialDistribution();
	}

	/**
	 * 
	 * @return
	 */
	private PositionMap<HoleDistribution> initialDistribution() {
		PositionMap<HoleDistribution> dist = new PositionMap<>();

		for (Position pos : Position.values()) {
			HolePossible hp = hand.getHolePossible(pos);
			dist.put(pos, new HoleDistribution(hp));
		}

		return dist;
	}

    /**
     * @param hand
     * @param hd  Hole Card distribution.
     * @return Probability of act in this node.
     */
    private double findActionProbability(StateNode state, HandInfo hand, HoleDistribution hd, PathElement elem) {
        CardSet board = hand.getBoard(elem.getStreet());
        double sum = 0;

		/*
		 * Iterate over all hole cards.
		 */
        HolePossible holePossible = hand.getHolePossible(elem.getPosition());
        for (HoleCards hole : holePossible) {
            ActionDistribution dist = state.getDistribution(board, hole);

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

        HolePossible holePossible = hand.getHolePossible(elem.getPosition());
        for (HoleCards hole : holePossible) {
            ActionDistribution dist = state.getDistribution(board, hole);

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