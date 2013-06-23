package se.hupoker.inference.holebucket;

import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.handeval.EquityMatrix;
import se.hupoker.cards.handeval.EquityRepository;
import se.hupoker.poker.Street;
import se.hupoker.inference.actiondistribution.ActionDistOptions;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.inference.states.GenericState;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 
 * @author Alexander Nyberg
  */
public class FlopHoleClusterer implements HoleClusterer {
	private final List<FlopCluster> flopClusters;

	public FlopHoleClusterer(List<FlopCluster> flopClusters) {
		this.flopClusters = flopClusters;
	}

    @Override
	public HoleCluster getClusterUniverse(GenericState descriptor) {
		HoleCluster bm = new HoleCluster();

		for (FlopCluster tuple : flopClusters) {
			EnumSet<ActionDistOptions> options = ActionDistOptions.empty();

			ActionDistribution ad = ActionDistribution.from(descriptor.getBetting(), tuple.toString(), options);

			bm.add(ad);
		}

		return bm;
	}

    /**
     * This needs a lot of love.
     *
     * @param equityRepository
     * @param board
     * @return Mapping of HoleCards -> Cluster key
     */
    @Override
    public Map<HoleCards, Integer> getClustering(EquityRepository equityRepository, CardSet board) {
        checkArgument(board.size() == 3);
        Map<HoleCards, Integer> map = new HashMap<>();
        EquityMatrix matrix = equityRepository.get(Street.FLOP, board);

        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            HoleTuple holeTuple = new HoleTuple(matrix.getApproximateHs(hole), matrix.getApproximatePpot(hole), matrix.getApproximateNpot(hole));
            map.put(hole, getClosest(holeTuple));
        }

        return map;
    }

    /**
	 * @param otherTuple
	 * @return Closest bucket in HoleTuple terms.
	 */
	private int getClosest(HoleTuple otherTuple) {
		int minIndex = 0;
		double minDistance = Double.MAX_VALUE;

		for (int i=0; i < flopClusters.size(); i++) {
			HoleTuple tuple = flopClusters.get(i).getHoleTuple();

			double dist = otherTuple.getDistance(tuple);
			if (dist < minDistance) {
				minDistance = dist;
				minIndex = i;
			}
		}

		return minIndex;
	}
}