package se.hupoker.inference.holebucket;

import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.inference.actiondistribution.ActionDistOptions;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.inference.states.GenericState;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.TurnTable;

import java.util.EnumSet;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
* @author Alexander Nyberg
*/
public final class TurnHoleReader implements HoleClusterer {
	private final List<TurnCluster> tupleList;

	private final TurnTable hsTable;
	private final TurnTable ppotTable;
	private final TurnTable npotTable;

	public TurnHoleReader(List<TurnCluster> turnClusters, TurnTable hsTable, TurnTable ppotTable, TurnTable npotTable) {
        this.tupleList = turnClusters;
		this.hsTable = hsTable;
        this.ppotTable = ppotTable;
        this.npotTable = npotTable;
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
    public int getHoleClusterIndex(CardSet board, HoleCards hole) {
        checkArgument(board.size() == 4);

        final LutKey key = new LutKey(board, hole);
        float hs = hsTable.lookupOne(key);
        float ppot = ppotTable.lookupOne(key);
        float npot = npotTable.lookupOne(key);

        HoleTuple tp = new HoleTuple(hs, ppot, npot);

        return getClosest(tp);
    }

    /**
     * @param otherTuple
     * @return Closest bucket in HoleTuple terms.
     */
    private int getClosest(HoleTuple otherTuple) {
        int minIndex = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i=0; i < tupleList.size(); i++) {
            HoleTuple tuple = tupleList.get(i);

            double dist = otherTuple.getDistance(tuple);

            if (dist < minDistance) {
                minDistance = dist;
                minIndex = i;
            }
        }

        return minIndex;
    }
}