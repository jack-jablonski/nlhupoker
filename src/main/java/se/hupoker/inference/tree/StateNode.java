package se.hupoker.inference.tree;

import se.hupoker.common.Predicates;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.cards.HoleCards;
import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.inference.holebucket.HoleCluster;
import se.hupoker.inference.states.PathElement;
import se.hupoker.inference.holebucket.HoleBucketMap;
import se.hupoker.inference.states.GenericState;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * Describing the visible state of a node.
 *
 * @author Alexander Nyberg
 */
public class StateNode {
    /*
     * Defines this node!
     */
    private final GenericState center;

    /*
     * Link to next street set
     */
    private StreetStateSet child;

    /*
     * Holds action distributions for all hole card buckets.
     */
    private final HoleCluster holeCluster;
    private final PrintWriter writer;


    protected StateNode(GenericState state, HoleCluster holeCluster, PrintWriter writer) {
        // TODO: Should clone, since GenericState mutable b/c yamlbeans
        this.center = state;
        this.holeCluster = holeCluster;
        this.writer = writer;

    }

    protected void setChildLink(StreetStateSet childLevel) {
        checkState(getCenter().hasChild());
        child = childLevel;
    }

    /**
     *
     * @param holeBucketMap
     * @param hole
     * @return The distribution of actions in the holeBucketMap.
     */
    protected ActionDistribution getDistribution(HoleBucketMap holeBucketMap, HoleCards hole) {
        int bucketIndex = holeBucketMap.get(hole);
        return getHoleCluster().get(bucketIndex);
    }

    protected boolean isCompareable(PathElement elem) {
        return Predicates.allTrue(
                getCenter().getPosition() == elem.getPosition(),
                getCenter().getBetting() == elem.getBettingType(),
                getCenter().getStreet() == elem.getStreet()
        );
    }

    /**
     * Do sanity checks, make sure PathElement matches GenericState.
     *
     * @param elem
     * @return Distance between this node and the element.
     */
    protected double getDistance(PathElement elem) {
        checkArgument(isCompareable(elem));

        return getCenter().getDistance(elem);
    }

    public int numberOfVisits = 0;

    /**
     * Build statistics of how often this node was visited.
     *
     * @param hand
     * @param element The visit.
     */
    protected void addVisit(HandInfo hand, PathElement element) {
//        final HoleBucketMap holeCluster = hand.getHoleCluster(getCenter().getStreet());
//        HolePossible hp = hand.getHolePossible(element.getPosition());

		/*
		 * Seen hands.
		 */
/*
        if (hp.numberOfPossible() == 1) {
            HoleCards unique = hp.getUnique();
            int bucketIndex = holeCluster.get(unique);
            int actionIndex = BetFactory.getIndex(center.getActiontype(), he.getAction());
            holeVisit[bucketIndex][actionIndex]++;
        }
*/
        numberOfVisits++;

//        getVisits().add(element);
    }

    protected void logDerivatives() {
        writer.println("=== Derivatives ===");

        for (ActionDistribution ad : getHoleCluster()) {
            writer.println(ad.derivativeString() + "{" + ad.toString() + "}");
        }

        writer.flush();
    }

    protected void logProbablities() {
        writer.println("=== Probabilities ===");

        for (ActionDistribution ad : getHoleCluster()) {
            writer.println(ad.probString() + "{" + ad.toString() + "}");
        }

        writer.flush();
    }

    public void printStatistics() {
//        final Betting type = getCenter().getBetting();
//        Distribution dist = new Distribution(type.numberOfOptions(), 0.0);
//
//        for (PathElement h : getVisits()) {
//            Action action = h.getAction();
//
//            dist.add(BetFactory.getIndex(getCenter().getBetting(), action), 1.0);
//        }
//
//        dist.normalize();
//
//        String stat = "(#" + getVisits().size() + ")" + "(" + type + ") = " + dist.getAsPercentages();
//        System.out.println(stat);

        System.out.println("Visits #" + numberOfVisits);

//        printBucketStatistics();
    }

    private void printBucketStatistics() {
        for (int i = 0; i < getHoleCluster().size(); i++) {
            ActionDistribution ad = getHoleCluster().get(i);

//            System.out.print(Arrays.toString(getHoleVisit()[i]) + " ");
            System.out.println(ad.probString() + "{" + ad.toString() + "}");
        }
    }

    @Override
    public String toString() {
        return getCenter().toString();
    }

    public GenericState getCenter() {
        return center;
    }

    public HoleCluster getHoleCluster() {
        return holeCluster;
    }

    public StreetStateSet getChild() {
        return child;
    }
}