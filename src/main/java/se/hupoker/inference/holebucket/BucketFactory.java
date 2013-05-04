package se.hupoker.inference.holebucket;

import se.hupoker.common.Street;
import se.hupoker.inference.states.GenericState;

import static se.hupoker.common.Street.FLOP;
import static se.hupoker.common.Street.PREFLOP;
import static se.hupoker.common.Street.RIVER;

/**
 * @author Alexander Nyberg
 */
public class BucketFactory {
    private static HoleCluster preflop(GenericState descriptor) {
        PreflopHoleReader preflop = PreflopHoleReader.getInstance();

        return preflop.getBucketMap(descriptor);
    }

    private static HoleCluster flop(GenericState descriptor) {
        FlopHoleReader flop = FlopHoleReader.getInstance();

        return flop.getBucketMap(descriptor);
    }

    private static HoleCluster turn(GenericState descriptor) {
        TurnHoleReader turn = TurnHoleReader.getInstance();

        return turn.getBucketMap(descriptor);
    }

    private static HoleCluster river(GenericState descriptor) {
        RiverHoleReader river = RiverHoleReader.getInstance();

        return river.getBucketMap(descriptor);
    }

    public static HoleCluster factory(GenericState descriptor) {
        final Street street = descriptor.getStreet();

        if (street.equals(PREFLOP)) {
            return preflop(descriptor);
        } else if (street.equals(RIVER)) {
            return river(descriptor);
        } else if (street.equals(FLOP)) {
            return flop(descriptor);
        } else {
            return turn(descriptor);
        }
    }
}
