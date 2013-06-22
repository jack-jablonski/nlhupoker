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
public class TurnHoleClusterer implements HoleClusterer {
	private final List<TurnCluster> tupleList;

	public TurnHoleClusterer(List<TurnCluster> turnClusters) {
        this.tupleList = turnClusters;
	}

    /**
     *
     * @param descriptor
     * @return Set of action distributions corresponding to each node state.
     */
    @Override
	public HoleCluster getClusterUniverse(GenericState descriptor) {
		HoleCluster bm = new HoleCluster();

		for (TurnCluster tuple : tupleList) {
//			EnumSet<ActionDistOptions> options = getOptions(descriptor, tuple);
			EnumSet<ActionDistOptions> options = ActionDistOptions.empty();

			ActionDistribution ad = ActionDistribution.from(descriptor.getBetting(), tuple.toString(), options);

			bm.add(ad);
		}

		return bm;
	}

    @Override
    public Map<HoleCards, Integer> getClustering(EquityRepository equityRepository, CardSet board) {
        checkArgument(board.size() == 4);
        Map<HoleCards, Integer> map = new HashMap<>();
        EquityMatrix matrix = equityRepository.get(Street.TURN, board);

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

        for (int i=0; i < tupleList.size(); i++) {
            HoleTuple tuple = tupleList.get(i).getHoleTuple();

            double dist = otherTuple.getDistance(tuple);

            if (dist < minDistance) {
                minDistance = dist;
                minIndex = i;
            }
        }

        return minIndex;
    }
}