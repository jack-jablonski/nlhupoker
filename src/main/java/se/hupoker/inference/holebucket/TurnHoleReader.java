package se.hupoker.inference.holebucket;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.inference.actiondistribution.ActionDistOptions;
import se.hupoker.cards.HoleCards;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutPath;
import se.hupoker.lut.TurnTable;
import se.hupoker.inference.states.GenericState;
import se.hupoker.cards.CardSet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
* 
* Default <Street>Bucket
* 
* @author Alexander Nyberg
*
*/
public final class TurnHoleReader implements HoleReader {
	private static final TurnHoleReader single = new TurnHoleReader();

	private final List<TurnStructure> tupleList = new ArrayList<>();

	private final TurnTable hsTable = new TurnTable();
	private final TurnTable ppotTable = new TurnTable();
	private final TurnTable npotTable = new TurnTable();

	private TurnHoleReader() {
		System.out.println("TurnHoleReader loading");

		hsTable.load(LutPath.getTurnHs());
		ppotTable.load(LutPath.getTurnPpot());
		npotTable.load(LutPath.getTurnNpot());
		
		initialize();
	}

    /**
     *
     * @return Singleton
     */
	public static TurnHoleReader getInstance() {
		return single;
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
	
/*	private EnumSet<ActionDistOptions> getOptions(GenericState descriptor, TurnStructure ts) {
		if (descriptor.getActiontype()==Betting.FCR && ts.strength == HandStrength.NUT) {
			return EnumSet.of(ActionDistOptions.NOFOLD);
		}

		return ActionDistOptions.empty();
	}*/

    /**
     *
     * @param descriptor
     * @return Set of action distributions corresponding to each node state.
     */
    @Override
	public HoleCluster getBucketMap(GenericState descriptor) {
		HoleCluster bm = new HoleCluster();

		for (TurnStructure tuple : tupleList) {
//			EnumSet<ActionDistOptions> options = getOptions(descriptor, tuple);
			EnumSet<ActionDistOptions> options = ActionDistOptions.empty();

			ActionDistribution ad = ActionDistribution.from(descriptor.getBetting(), tuple.toString(), options);

			bm.add(ad);			
		}
		
		return bm;
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

		return getClosest(tp);
	}

	public static class TurnStructure extends HoleTuple implements StreetStructure {
		public HandStrength strength = HandStrength.NONE;
	}	

	/**
	 * Load from file.
	 */
	private void initialize() {
		try {
//			YamlReader reader = new YamlReader(HolePath.getTurnPath());
			YamlReader reader = new YamlReader(new FileReader(HolePath.getTurnPath()));

			while (true) {
				TurnStructure tuple = reader.read(TurnStructure.class);
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