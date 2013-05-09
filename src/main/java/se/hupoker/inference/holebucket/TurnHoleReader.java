package se.hupoker.inference.holebucket;

import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.handeval.EquityMatrix;
import se.hupoker.cards.handeval.EquityRepository;
import se.hupoker.inference.actiondistribution.ActionDistOptions;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.inference.states.GenericState;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
* @author Alexander Nyberg
*/
public final class TurnHoleReader implements HoleClusterer {
	private final List<TurnCluster> tupleList;

	public TurnHoleReader(List<TurnCluster> turnClusters) {
        this.tupleList = turnClusters;
	}

    /**
     *
     * @param descriptor
     * @return Set of action distributions corresponding to each node state.
     */
    @Override
	public HoleCluster getHoleClusters(GenericState descriptor) {
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
    public Map<HoleCards, Integer> getHoleCluster(EquityRepository equityRepository, CardSet board) {
        checkArgument(board.size() == 4);

        throw new UnsupportedOperationException();
    }

    /**
     * @param otherTuple
     * @return Closest bucket in HoleTuple terms.
     */
//    private int getClosest(HoleTuple otherTuple) {
//        int minIndex = 0;
//        double minDistance = Double.MAX_VALUE;
//
//        for (int i=0; i < tupleList.size(); i++) {
//            HoleTuple tuple = tupleList.get(i);
//
//            double dist = otherTuple.getDistance(tuple);
//
//            if (dist < minDistance) {
//                minDistance = dist;
//                minIndex = i;
//            }
//        }
//
//        return minIndex;
//    }
}