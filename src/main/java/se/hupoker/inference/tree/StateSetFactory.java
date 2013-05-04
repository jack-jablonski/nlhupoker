package se.hupoker.inference.tree;

import se.hupoker.common.Street;
import se.hupoker.inference.holebucket.BucketFactory;
import se.hupoker.inference.holebucket.HoleCluster;
import se.hupoker.inference.states.GenericState;
import se.hupoker.inference.states.PostflopStateReader;
import se.hupoker.inference.states.PreflopStateReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * @author Alexander Nyberg
 */
public class StateSetFactory {
    private static StreetStateSet preflopFactory() {
        StreetStateSet preflop = new StreetStateSet(Street.PREFLOP);
        Collection<GenericState> prefFopStates = PreflopStateReader.getStates();

        for (GenericState state : prefFopStates) {
            HoleCluster bucket = BucketFactory.factory(state);
            PrintWriter writer = createLogWriter(state);
            StateNode value = new StateNode(state, bucket, writer);
            preflop.addNode(value);
        }

        return preflop;
    }

    private static StreetStateSet postFactory(Street street) {
        StreetStateSet post = new StreetStateSet(street);
        Collection<GenericState> postFlopStates = PostflopStateReader.getStates(street);

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
    public static void createChildLinks(Street street, StreetStateSet stateSet) {
        for (StateNode node : stateSet) {
            if (node.getCenter().hasChild()) {
                final Street nextStreet = street.next();
                StreetStateSet nextStateSet = postFactory(nextStreet);

                node.setChildLink(nextStateSet);
                createChildLinks(nextStreet, nextStateSet);
            }
        }
    }

    public static StreetStateSet factory() {
        StreetStateSet stateSet = preflopFactory();

        createChildLinks(Street.PREFLOP, stateSet);

        return stateSet;
    }
}