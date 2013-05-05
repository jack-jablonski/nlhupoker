package se.hupoker.inference.tree;

import se.hupoker.common.Street;
import se.hupoker.inference.holebucket.*;
import se.hupoker.inference.states.*;
import se.hupoker.lut.FlopTable;
import se.hupoker.lut.LutPath;
import se.hupoker.lut.RiverTable;
import se.hupoker.lut.TurnTable;

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
    private static StreetStateSet preflopFactory(StateRepository stateRepository, ClusterRepository clusterRepository) {
        final Street street = Street.PREFLOP;
        StreetStateSet preflop = new StreetStateSet(street);
        Collection<GenericState> prefFopStates = stateRepository.getPreflop();

        Collection<PreflopCluster> preflopClusters = clusterRepository.preflop();
        PreflopHoleReader preflopHoleReader = new PreflopHoleReader(preflopClusters);

        for (GenericState state : prefFopStates) {
            PrintWriter writer = createLogWriter(state);
            StateNode value = new StateNode(state, preflopHoleReader, writer);
            preflop.addNode(value);
        }

        return preflop;
    }

    private static StreetStateSet postFactory(Street street, StateRepository stateRepository, ClusterRepository clusterRepository) {
        StreetStateSet post = new StreetStateSet(street);
        Collection<GenericState> postFlopStates = stateRepository.getPostFlop(street);

        for (GenericState state : postFlopStates) {
            StateNode value = createNode(street, state, clusterRepository);
            post.addNode(value);
        }

        return post;
    }

    private static StateNode createNode(Street street, GenericState state, ClusterRepository clusterRepository) {
        PrintWriter writer = createLogWriter(state);

        if (street == Street.FLOP) {
            List<FlopCluster> flopClusters = clusterRepository.flop();

            FlopHoleReader flopHoleReader = new FlopHoleReader(flopClusters, Tables.flopHsTable, Tables.flopPpotTable, Tables.flopNpotTable);
            return new StateNode(state, flopHoleReader, writer);
        } else if (street == Street.TURN) {
            List<TurnCluster> turnClusters = clusterRepository.turn();

            TurnHoleReader flopHoleReader = new TurnHoleReader(turnClusters, Tables.turnHsTable, Tables.turnPpotTable, Tables.turnNpotTable);
            return new StateNode(state, flopHoleReader, writer);
        } else if (street == Street.RIVER) {
            List<RiverCluster> riverClusters = clusterRepository.river();

            RiverHoleReader riverHoleReader = new RiverHoleReader(riverClusters, Tables.riverHsTable);
            return new StateNode(state, riverHoleReader, writer);
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
    public static void createChildLinks(Street street, StreetStateSet stateSet, StateRepository stateRepository, ClusterRepository clusterRepository) {
        for (StateNode node : stateSet) {
            if (node.getCenter().hasChild()) {
                final Street nextStreet = street.next();
                StreetStateSet nextStateSet = postFactory(nextStreet, stateRepository, clusterRepository);

                node.setChildLink(nextStateSet);
                createChildLinks(nextStreet, nextStateSet, stateRepository, clusterRepository);
            }
        }
    }

    // Nasty but contained
    private static class Tables {
        private final static FlopTable flopHsTable = FlopTable.create(LutPath.getFlopHs());
        private final static FlopTable flopPpotTable = FlopTable.create(LutPath.getFlopPpot());
        private final static FlopTable flopNpotTable = FlopTable.create(LutPath.getFlopNpot());

        private final static TurnTable turnHsTable = TurnTable.create(LutPath.getTurnHs());
        private final static TurnTable turnPpotTable = TurnTable.create(LutPath.getTurnPpot());
        private final static TurnTable turnNpotTable = TurnTable.create(LutPath.getTurnNpot());

        private final static RiverTable riverHsTable = RiverTable.create(LutPath.getRiverHs());
    }

    public static StreetStateSet factory(StateRepository stateRepository, ClusterRepository clusterRepository) {
        StreetStateSet stateSet = preflopFactory(stateRepository, clusterRepository);

        createChildLinks(Street.PREFLOP, stateSet, stateRepository, clusterRepository);

        return stateSet;
    }
}