package se.hupoker.inference.tree;

import se.hupoker.cards.CardSet;
import se.hupoker.cards.handeval.EquityRepository;
import se.hupoker.common.Predicates;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.cards.HoleCards;
import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.inference.holebucket.HoleCluster;
import se.hupoker.inference.holebucket.HoleClusterer;
import se.hupoker.inference.states.PathElement;
import se.hupoker.inference.states.GenericState;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
    private final HoleClusterer holeClusterer;
    private final HoleCluster holeClusters;
    private final EquityRepository equityRepository;
    private final PrintWriter writer;

    protected StateNode(GenericState state, HoleClusterer holeClusterer, EquityRepository equityRepository, PrintWriter writer) {
        this.center = state;
        this.holeClusterer = holeClusterer;
        this.equityRepository = equityRepository;
        this.writer = writer;

        // TODO: nasty
        this.holeClusters = holeClusterer.getClusterUniverse(state);
    }

    protected void setChildLink(StreetStateSet childLevel) {
        checkState(getCenter().hasChild());
        child = childLevel;
    }

    /**
     * TODO: Could use caching of the clustering per board
     *
     * @param board The board to cluster the holecards on.
     */
    private void getBucketMapping(CardSet board) {
        Map<HoleCards, Integer> map = holeClusterer.getClustering(equityRepository, board);
        throw new UnsupportedOperationException();
    }

    /**
     * @param board The board we want to cluster the holecards of
     * @return The distribution of actions.
     */
    protected Map<HoleCards, ActionDistribution> getDistribution(CardSet board) {
        Map<HoleCards, Integer> clusterMap = holeClusterer.getClustering(equityRepository, board);

        Map<HoleCards, ActionDistribution> actionMap = new HashMap<>();
        for (HoleCards cards : HoleCards.allOf()) {
            if (clusterMap.containsKey(cards)) {
                int clusterIndex = clusterMap.get(cards);

                actionMap.put(cards, holeClusters.get(clusterIndex));
            }
        }

        return actionMap;
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
//        final HoleBucketMap holeClusterer = hand.getHoleClusterer(getCenter().getStreet());
//        HolePossible hp = hand.getHolePossible(element.getPosition());

		/*
		 * Seen hands.
		 */
/*
        if (hp.numberOfPossible() == 1) {
            HoleCards unique = hp.getUnique();
            int bucketIndex = holeClusterer.get(unique);
            int actionIndex = BetFactory.getIndex(center.getActiontype(), he.getAction());
            holeVisit[bucketIndex][actionIndex]++;
        }
*/
        numberOfVisits++;

//        getVisits().add(element);
    }

    protected void logDerivatives() {
        writer.println("=== Derivatives ===");

        for (ActionDistribution ad : getHoleClusterer()) {
            writer.println(ad.derivativeString() + "{" + ad.toString() + "}");
        }

        writer.flush();
    }

    protected void logProbablities() {
        writer.println("=== Probabilities ===");

        for (ActionDistribution ad : getHoleClusterer()) {
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
        for (int i = 0; i < getHoleClusterer().size(); i++) {
            ActionDistribution ad = getHoleClusterer().get(i);

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

    public HoleCluster getHoleClusterer() {
        return holeClusters;
    }

    public StreetStateSet getChild() {
        return child;
    }
}