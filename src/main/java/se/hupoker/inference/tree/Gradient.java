package se.hupoker.inference.tree;

import se.hupoker.cards.HoleCards;
import se.hupoker.inference.handinformation.HolePossible;
import se.hupoker.inference.vectors.DoubleVector;

/**
 * The gradient of the likelihood belonging to a single hand.
 *
 * @author Alexander Nyberg
 */
class Gradient
{
    private final DoubleVector vector = new DoubleVector(HoleCards.TexasCombinations);

    /**
     * Set initial state to a uniform distribution over possible hole cards.
     *
     * @param hp
     */
    protected Gradient(HolePossible hp) {
        vector.setAll(0.0);

        double assign = 1.0 / hp.numberOfPossible();
        for (HoleCards hole : hp) {
            vector.set(hole.ordinal(), assign);
        }
    }

    protected double getSum() {
        return vector.getSum();
    }

    protected void addActionProbability(int index, double actionProbability) {
        vector.multiplyOne(index, actionProbability);
    }

    protected double getActionProbability(int index) {
        return vector.get(index);
    }
}