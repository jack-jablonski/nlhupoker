package se.hupoker.inference.holebucket;

import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.handeval.EquityMatrix;
import se.hupoker.cards.handeval.EquityRepository;
import se.hupoker.common.Street;
import se.hupoker.inference.actiondistribution.ActionDistOptions;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.inference.states.GenericState;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
* @author Alexander Nyberg
*/
public class RiverClusterMapper implements HoleClusterer {
	private final List<RiverCluster> buckets;

	public RiverClusterMapper(List<RiverCluster> buckets) {
        this.buckets = buckets;
	}

/*	private EnumSet<ActionDistOptions> getOptions(GenericState descriptor, RiverStructure rs) {
		if (descriptor.getActiontype()==Betting.FCR && rs.getStrength() == HandStrength.NUT) {
			return EnumSet.of(ActionDistOptions.NOFOLD);
		}

		return ActionDistOptions.empty();
	}*/

    @Override
	public HoleCluster getClusterUniverse(GenericState descriptor) {
		HoleCluster bm = new HoleCluster();

		for (RiverCluster river : buckets) {
//			EnumSet<ActionDistOptions> options = getOptions(descriptor, river);
			EnumSet<ActionDistOptions> options = ActionDistOptions.empty();

			ActionDistribution ad = ActionDistribution.from(descriptor.getBetting(), river.getName(), options);

			bm.add(ad);
		}

		return bm;
	}

    @Override
    public Map<HoleCards, Integer> getClustering(EquityRepository equityRepository, CardSet board) {
        checkArgument(board.size() == 5);
        Map<HoleCards, Integer> map = new HashMap<>();
        EquityMatrix matrix = equityRepository.get(Street.RIVER, board);

        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            double holeCardHandStrength = matrix.getAverageEquity(hole);
            map.put(hole, getClosest(holeCardHandStrength));
        }
        return map;
    }

    private int getClosest(double hs) {
        for (int i=0; i < buckets.size(); i++) {
            RiverCluster rp = buckets.get(i);

            if (hs <= rp.getHs()) {
                return i;
            }
        }

        throw new IllegalArgumentException("No equity for " +  hs);
    }
}