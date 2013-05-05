package se.hupoker.inference.holebucket;

import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.inference.actiondistribution.ActionDistOptions;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.inference.states.GenericState;
import se.hupoker.lut.FlopTable;
import se.hupoker.lut.LutKey;

import java.util.EnumSet;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 
 * @author Alexander Nyberg
  */
public class FlopHoleReader implements HoleClusterer {
	private final List<FlopCluster> flopClusters;
	private final FlopTable hsTable;
	private final FlopTable ppotTable;
	private final FlopTable npotTable;

	public FlopHoleReader(List<FlopCluster> flopClusters, FlopTable hsTable, FlopTable ppotTable, FlopTable npotTable) {
		this.flopClusters = flopClusters;
        this.hsTable = hsTable;
        this.ppotTable = ppotTable;
        this.npotTable = npotTable;
	}

    @Override
	public HoleCluster getHoleClusters(GenericState descriptor) {
		HoleCluster bm = new HoleCluster();

		for (FlopCluster tuple : flopClusters) {
			EnumSet<ActionDistOptions> options = ActionDistOptions.empty();

			ActionDistribution ad = ActionDistribution.from(descriptor.getBetting(), tuple.toString(), options);

			bm.add(ad);
		}

		return bm;
	}

    @Override
    public int getHoleClusterIndex(CardSet board, HoleCards hole) {
        checkArgument(board.size() == 3);
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

		for (int i=0; i < flopClusters.size(); i++) {
			HoleTuple tuple = flopClusters.get(i);

			double dist = otherTuple.getDistance(tuple);
			if (dist < minDistance) {
				minDistance = dist;
				minIndex = i;
			}
		}

		return minIndex;
	}
}