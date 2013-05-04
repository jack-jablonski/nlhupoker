package se.hupoker.inference.holebucket;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.inference.actiondistribution.ActionDistOptions;
import se.hupoker.cards.HoleCards;
import se.hupoker.lut.FlopTable;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutPath;
import se.hupoker.inference.states.GenericState;
import se.hupoker.cards.CardSet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * 
 * @author Alexander Nyberg
  */
class FlopHoleReader implements HoleReader {
	private static final FlopHoleReader single = new FlopHoleReader();

	private final List<FlopStructure> tupleList = new ArrayList<>();
	private final FlopTable hsTable = new FlopTable();
	private final FlopTable ppotTable = new FlopTable();
	private final FlopTable npotTable = new FlopTable();

	private FlopHoleReader() {
		System.out.println("FlopHoleReader loading");

		hsTable.load(LutPath.getFlopHs());
		ppotTable.load(LutPath.getFlopPpot());
		npotTable.load(LutPath.getFlopNpot());

		initialize();
	}

	public static FlopHoleReader getInstance() {
		return single;
	}

    @Override
	public HoleCluster getBucketMap(GenericState descriptor) {
		HoleCluster bm = new HoleCluster();

		for (FlopStructure tuple : tupleList) {
			EnumSet<ActionDistOptions> options = ActionDistOptions.empty();

			ActionDistribution ad = ActionDistribution.from(descriptor.getBetting(), tuple.toString(), options);

			bm.add(ad);
		}

		return bm;
	}

	/**
	 * Closest bucket in HoleTuple terms.
	 * 
	 * @param otherTuple
	 * @return
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

	/**
	 * 
	 * @param board
	 * @param hole
	 * @return Index into 
	 */
	public int getBucketIndex(CardSet board, HoleCards hole) {
        final LutKey key = new LutKey(board, hole);
        float hs = hsTable.lookupOne(key);
        float ppot = ppotTable.lookupOne(key);
        float npot = npotTable.lookupOne(key);

		HoleTuple tp = new HoleTuple(hs, ppot, npot);
//        System.out.println("Tuple: (" + tp + ")");

		return getClosest(tp);
	}

	// TODO: use 
	public static class FlopStructure extends HoleTuple implements StreetStructure {
		public HandStrength strength = HandStrength.NONE;
	}

	/**
	 * Load from file.
	 */
	private void initialize() {
		try {
			YamlReader reader = new YamlReader(new FileReader(HolePath.getFlopPath()));

			while (true) {
				FlopStructure tuple = reader.read(FlopStructure.class);
				if (tuple == null) {
					break;
				}

				tupleList.add(tuple);
			}
		} catch (YamlException|FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not load.");
		}
	}
}