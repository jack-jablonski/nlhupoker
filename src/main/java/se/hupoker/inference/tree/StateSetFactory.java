package se.hupoker.inference.tree;

import se.hupoker.cards.handeval.EquityRepository;
import se.hupoker.common.Street;
import se.hupoker.inference.holebucket.*;
import se.hupoker.inference.states.GenericState;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

/**
 * @author Alexander Nyberg
 */
public class StateSetFactory {
    private static StreetStateSet preflopFactory(StateRepository stateRepository, ClusterRepository clusterRepository, EquityRepository equityRepository) {
        final Street street = Street.PREFLOP;
        StreetStateSet preflop = new StreetStateSet(street);
        Collection<GenericState> prefFopStates = stateRepository.getPreflop();

        Collection<PreflopCluster> preflopClusters = clusterRepository.preflop();
        PreflopHoleReader preflopHoleReader = new PreflopHoleReader(preflopClusters);

        for (GenericState state : prefFopStates) {
            PrintWriter writer = createLogWriter(state);
            StateNode value = new StateNode(state, preflopHoleReader, equityRepository, writer);
            preflop.addNode(value);
        }

        return preflop;
    }

    private static StreetStateSet postFactory(Street street, StateRepository stateRepository, ClusterRepository clusterRepository, EquityRepository equityRepository) {
        StreetStateSet post = new StreetStateSet(street);
        Collection<GenericState> postFlopStates = stateRepository.getPostFlop(street);

        for (GenericState state : postFlopStates) {
            StateNode value = createNode(street, state, clusterRepository, equityRepository);
            post.addNode(value);
        }

        return post;
    }

    private static StateNode createNode(Street street, GenericState state, ClusterRepository clusterRepository, EquityRepository equityRepository) {
        PrintWriter writer = createLogWriter(state);

        if (street == Street.FLOP) {
            List<FlopCluster> flopClusters = clusterRepository.flop();

            FlopHoleReader flopHoleReader = new FlopHoleReader(flopClusters);
            return new StateNode(state, flopHoleReader, equityRepository, writer);
        } else if (street == Street.TURN) {
            List<TurnCluster> turnClusters = clusterRepository.turn();

            TurnHoleReader flopHoleReader = new TurnHoleReader(turnClusters);
            return new StateNode(state, flopHoleReader, equityRepository, writer);
        } else if (street == Street.RIVER) {
            List<RiverCluster> riverClusters = clusterRepository.river();

            RiverHoleClusterer riverHoleClusterer = new RiverHoleClusterer(riverClusters);
            return new StateNode(state, riverHoleClusterer, equityRepository, writer);
        } else {
            throw new IllegalArgumentException("No bucketmapping for " + street);
        }
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
    public static void createChildLinks(Street street, StreetStateSet stateSet, StateRepository stateRepository, ClusterRepository clusterRepository, EquityRepository equityRepository) {
        for (StateNode node : stateSet) {
            if (node.getCenter().hasChild()) {
                final Street nextStreet = street.next();
                StreetStateSet nextStateSet = postFactory(nextStreet, stateRepository, clusterRepository, equityRepository);

                node.setChildLink(nextStateSet);
                createChildLinks(nextStreet, nextStateSet, stateRepository, clusterRepository, equityRepository);
            }
        }
    }

    public static StreetStateSet factory(StateRepository stateRepository, ClusterRepository clusterRepository, EquityRepository equityRepository) {
        StreetStateSet stateSet = preflopFactory(stateRepository, clusterRepository, equityRepository);

        createChildLinks(Street.PREFLOP, stateSet, stateRepository, clusterRepository, equityRepository);

        return stateSet;
    }
}