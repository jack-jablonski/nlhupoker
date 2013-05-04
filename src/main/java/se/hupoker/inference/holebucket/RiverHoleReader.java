package se.hupoker.inference.holebucket;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import se.hupoker.inference.actiondistribution.ActionDistOptions;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutPath;
import se.hupoker.lut.RiverTable;
import se.hupoker.inference.states.GenericState;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
*
* @author Alexander Nyberg
*
*/
class RiverHoleReader implements HoleReader {
	private static final RiverHoleReader single = new RiverHoleReader();
	private final List<RiverStructure> buckets = new ArrayList<>();
    private final RiverTable hsTable = new RiverTable();

	private RiverHoleReader() {
        hsTable.load(LutPath.getRiverHs());

		initialize();
		System.out.println("Sucessfully loaded river buckets");
	}

	/**
	 *
	 * @return The singleton.
	 */
	public static RiverHoleReader getInstance() {
		return single;
	}

/*	private EnumSet<ActionDistOptions> getOptions(GenericState descriptor, RiverStructure rs) {
		if (descriptor.getActiontype()==Betting.FCR && rs.getStrength() == HandStrength.NUT) {
			return EnumSet.of(ActionDistOptions.NOFOLD);
		}

		return ActionDistOptions.empty();
	}*/

    @Override
	public HoleCluster getBucketMap(GenericState descriptor) {
		HoleCluster bm = new HoleCluster();

		for (RiverStructure river : buckets) {
//			EnumSet<ActionDistOptions> options = getOptions(descriptor, river);
			EnumSet<ActionDistOptions> options = ActionDistOptions.empty();

			ActionDistribution ad = ActionDistribution.from(descriptor.getBetting(), river.getName(), options);

			bm.add(ad);
		}

		return bm;
	}

	/**
	 *
	 * @return
	 */
	public int getBucketIndex(CardSet board, HoleCards hole) {
        float hs = hsTable.lookupOne(new LutKey(board, hole));

		for (int i=0; i < buckets.size(); i++) {
			RiverStructure rp = buckets.get(i);

			if (hs <= rp.getHs()) {
				return i;
			}
		}

		throw new IllegalArgumentException("No equity for " + board + " " + hole + "=" + hs);
	}

	private void addBucket(RiverStructure r) {
		buckets.add(r);
		Collections.sort(buckets);
	}

	public static class RiverStructure implements Comparable<RiverStructure>, StreetStructure {
		private String name;
		private HandStrength strength = HandStrength.NONE;
		private double hs;

		@Override
		public int compareTo(RiverStructure arg) {
			return Double.compare(getHs(), arg.getHs());
		}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public HandStrength getStrength() {
            return strength;
        }

        public void setStrength(HandStrength strength) {
            this.strength = strength;
        }

        public double getHs() {
            return hs;
        }

        public void setHs(double hs) {
            this.hs = hs;
        }
    }

	private void initialize() {
		try {
//			YamlReader reader = new YamlReader(HolePath.getRiverPath());
			YamlReader reader = new YamlReader(new FileReader(HolePath.getRiverPath()));

			while (true) {
				RiverStructure contact = reader.read(RiverStructure.class);
				if (contact == null) {
					break;
				}

				addBucket(contact);
			}
		} catch (YamlException|FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Could not load.");
		}
	}
}