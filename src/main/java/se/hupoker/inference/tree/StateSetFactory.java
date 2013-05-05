package se.hupoker.inference.tree;

import se.hupoker.common.Street;
import se.hupoker.inference.holebucket.BucketFactory;
import se.hupoker.inference.holebucket.HoleCluster;
import se.hupoker.inference.states.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * @author Alexander Nyberg
 */
public class StateSetFactory {
    private static StreetStateSet preflopFactory(StateGetter stateGetter) {
        StreetStateSet preflop = new StreetStateSet(Street.PREFLOP);
        Collection<GenericState> prefFopStates = stateGetter.getPreflop();

        for (GenericState state : prefFopStates) {
            HoleCluster bucket = BucketFactory.factory(state);
            PrintWriter writer = createLogWriter(state);
            StateNode value = new StateNode(state, bucket, writer);
            preflop.addNode(value);
        }

        return preflop;
    }

    private static StreetStateSet postFactory(Street street, StateGetter stateGetter) {
        StreetStateSet post = new StreetStateSet(street);
        Collection<GenericState> postFlopStates = stateGetter.getPostFlop(street);

        for (GenericState state : postFlopStates) {
            HoleCluster bucket = BucketFactory.factory(state);
            PrintWriter writer = createLogWriter(state);
            StateNode value = new StateNode(state, bucket, writer);
            post.addNode(value);
        }

        return post;
    }

    private static PrintWriter createLogWriter(GenericState state) {
        final String logDirectory = "logs/";
        String filename = logDirectory + state.toString() + ".txt";
        try {
            new File(logDirectory).mkdir();
            return new PrintWriter(new FileWriter(filename));
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError();
        }
    }

    /**
     *
     * @param street
     * @param stateSet
     */
    public static void createChildLinks(Street street, StreetStateSet stateSet, StateGetter stateGetter) {
        for (StateNode node : stateSet) {
            if (node.getCenter().hasChild()) {
                final Street nextStreet = street.next();
                StreetStateSet nextStateSet = postFactory(nextStreet, stateGetter);

                node.setChildLink(nextStateSet);
                createChildLinks(nextStreet, nextStateSet, stateGetter);
            }
        }
    }

    public static StreetStateSet factory(StateGetter stateGetter) {
        StreetStateSet stateSet = preflopFactory(stateGetter);

        createChildLinks(Street.PREFLOP, stateSet, stateGetter);

        return stateSet;
    }
}