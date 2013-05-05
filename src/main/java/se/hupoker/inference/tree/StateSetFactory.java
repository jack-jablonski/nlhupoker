package se.hupoker.inference.tree;

import se.hupoker.common.Street;
import se.hupoker.inference.holebucket.BucketFactory;
import se.hupoker.inference.holebucket.HoleCluster;
import se.hupoker.inference.states.GenericState;
import se.hupoker.inference.states.PostflopStateReader;
import se.hupoker.inference.states.PreflopStateReader;
import se.hupoker.inference.states.StateConfigurationPath;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * @author Alexander Nyberg
 */
public class StateSetFactory {
    private static StreetStateSet preflopFactory(StateConfigurationPath configuration) {
        StreetStateSet preflop = new StreetStateSet(Street.PREFLOP);
        Collection<GenericState> prefFopStates = PreflopStateReader.getStates(configuration);

        for (GenericState state : prefFopStates) {
            HoleCluster bucket = BucketFactory.factory(state);
            PrintWriter writer = createLogWriter(state);
            StateNode value = new StateNode(state, bucket, writer);
            preflop.addNode(value);
        }

        return preflop;
    }

    private static StreetStateSet postFactory(Street street, StateConfigurationPath configuration) {
        StreetStateSet post = new StreetStateSet(street);
        Collection<GenericState> postFlopStates = PostflopStateReader.getStates(street, configuration);

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
    public static void createChildLinks(Street street, StreetStateSet stateSet, StateConfigurationPath configuration) {
        for (StateNode node : stateSet) {
            if (node.getCenter().hasChild()) {
                final Street nextStreet = street.next();
                StreetStateSet nextStateSet = postFactory(nextStreet, configuration);

                node.setChildLink(nextStateSet);
                createChildLinks(nextStreet, nextStateSet, configuration);
            }
        }
    }

    public static StreetStateSet factory(StateConfigurationPath configuration) {
        StreetStateSet stateSet = preflopFactory(configuration);

        createChildLinks(Street.PREFLOP, stateSet, configuration);

        return stateSet;
    }
}