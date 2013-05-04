package se.hupoker.inference.holebucket;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import se.hupoker.inference.actiondistribution.ActionDistribution;
import se.hupoker.inference.actiondistribution.ActionDistOptions;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.HoleRange;
import se.hupoker.inference.states.GenericState;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Alexander Nyberg
 */
class PreflopHoleReader implements HoleReader {
    private static final PreflopHoleReader pfp = new PreflopHoleReader();

    private final List<PreflopStructure> bucketList = new ArrayList<>();
    private final Map<HoleCards, Integer> map = new HashMap<>();

    private PreflopHoleReader() {
        System.out.println("PreflopHoleReader loading");
        initialize();
        buildMap();
        verify();
    }

    private void verify() {
        checkState(map.size() == HoleCards.TexasCombinations);

        List<HoleCards> allHoles = new ArrayList<>();
        for (PreflopStructure preflop : bucketList) {
            allHoles.addAll(preflop.holes);
        }
        assert allHoles.size() == HoleCards.TexasCombinations;

        Set<HoleCards> uniqueHoles = new HashSet<>(allHoles);
        assert uniqueHoles.size() == HoleCards.TexasCombinations;
    }

    /**
     * @return The singleton.
     */
    public static PreflopHoleReader getInstance() {
        return pfp;
    }

    /**
     * Construct the map: hole index -> bucket index
     */
    private void buildMap() {
        for (int index = 0; index < bucketList.size(); index++) {
            PreflopStructure bucket = bucketList.get(index);

            for (HoleCards hole : bucket.holes) {
                map.put(hole, index);
            }
        }
    }

    public int getBucket(HoleCards hole) {
        return map.get(hole);
    }

    /**
     * TODO: FIX
     *
     * @param descriptor
     * @param pf
     * @return
     *
    private EnumSet<ActionDistOptions> getOptions(GenericState descriptor, PreflopStructure pf) {
    if (pf.strength == HandStrength.NONE || descriptor.getNofold() == HandStrength.NONE) {
    return false;
    }

    if (pf.strength.strongerOrEqual(descriptor.getNofold())) {
    System.out.println( descriptor.getActiontype() + "|Setting nofold:" + pf.name);
    System.out.println(pf.strength + "|" + descriptor.getNofold());
    return true;
    }

    if (descriptor.getActiontype() == Betting.FCR && pf.strength == HandStrength.NUT) {
    return EnumSet.of(ActionDistOptions.NOFOLD);
    }

    return ActionDistOptions.empty();
    }*/

    /**
     * Factory
     *
     * @param descriptor
     * @return
     */
    public HoleCluster getBucketMap(GenericState descriptor) {
        HoleCluster bm = new HoleCluster();

        for (PreflopStructure pf : bucketList) {
//            EnumSet<ActionDistOptions> options = getOptions(descriptor, pf);
            EnumSet<ActionDistOptions> options = ActionDistOptions.empty();

            ActionDistribution ad = ActionDistribution.from(descriptor.getBetting(), pf.range, options);

            bm.add(ad);
        }

        return bm;
    }

    private void extractAndAddHoles(PreflopStructure bucket) {
        HoleRange holeRange = new HoleRange();
        List<HoleCards> localBucket = bucket.holes;

        List<HoleCards> bucketCards = holeRange.parse(bucket.range);
        System.out.println(bucketCards);
        localBucket.addAll(bucketCards);

        bucketList.add(bucket);
    }

    /**
     * Contains at least the implementation from file.
     */
    public static class PreflopStructure implements StreetStructure {
        public String range;
        public HandStrength strength = HandStrength.NONE;
        public List<HoleCards> holes = new ArrayList<>();
    }

    private void initialize() {
        try {
            YamlReader reader = new YamlReader(new FileReader(HolePath.getPreflopPath()));
//            YamlReader reader = new YamlReader(getPreflopPath());

            while (true) {
                PreflopStructure contact = reader.read(PreflopStructure.class);
                if (contact == null) {
                    break;
                }

                extractAndAddHoles(contact);
            }
        } catch (YamlException | FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not load.");
        }
    }
}