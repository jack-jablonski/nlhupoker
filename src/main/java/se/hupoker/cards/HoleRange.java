package se.hupoker.cards;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 22+,
 * A2s+,K2s+,Q2s+,J2s+,T2s+,92s+,82s+,72s+,62s+,52s+,42s+,32s,
 * A2o+,K2o+,Q2o+,J2o+,T2o+,92o+,82o+,72o+,62o+,52o+,42o+,32o
 * A2e+,K2e+,Q2e+,J2e+,T2e+,92e+,82e+,72e+,62e+,52e+,42e+,32e
 *
 * @author Alexander Nyberg
 */
public class HoleRange {
    /**
     * @param range "77+,ATs+,KTs+,QTs+,JTs,AQo+,ATo,KTo+,QTo+,JTo"
     * @return
     */
    public List<HoleCards> parse(String range) {
        List<HoleCards> completeRange = new ArrayList<>();

        checkArgument(!range.contains(" "));

        String split[] = range.split(",");
        for (String minimumRange : split) {
            List<HoleCards> splitRange = parseSeparateRange(minimumRange);
            completeRange.addAll(splitRange);
        }

        return completeRange;
    }

    /**
     * @param range "ATo", "77+"
     * @return
     */
    private List<HoleCards> parseSeparateRange(String range) {
        if (range.contains("+")) {
            return parseHoleAndBetter(range);
        } else if (range.contains("-")) {
            return parseInterval(range);
        } else {
            return parseSingleHole(range);
        }
    }

    /**
     * @param range "A9s-A2s", "99-22"
     * @return
     */
    private List<HoleCards> parseInterval(String range) {
        Rank upperBigRank = Rank.fromChar(range.charAt(0));
        Rank upperSmallRank = Rank.fromChar(range.charAt(1));

        if (upperBigRank == upperSmallRank) {
            Rank smallestPair = Rank.fromChar(range.charAt(3));

            EnumSet<Rank> pairRange = fromAndToInclusive(smallestPair, upperBigRank);
            return createPairs(pairRange);
        } else {
            checkArgument(range.charAt(3) == '-');
            checkArgument(Rank.fromChar(range.charAt(4)) == upperBigRank);
            Rank lowerSmallRank = Rank.fromChar(range.charAt(5));

            EnumSet<Rank> rankRange = fromAndToInclusive(lowerSmallRank, upperSmallRank);

            SuitConfiguration configuration = SuitConfiguration.of(range.charAt(2));
            return configuration.create(upperBigRank, rankRange);
        }
    }

    /**
     * @param range "ATo", "ATs", "ATe", "66"
     * @return
     */
    private List<HoleCards> parseSingleHole(String range) {
        Rank bigRank = Rank.fromChar(range.charAt(0));
        Rank smallRank = Rank.fromChar(range.charAt(1));

        if (bigRank == smallRank) {
            return createPairs(EnumSet.of(bigRank));
        } else {
            SuitConfiguration configuration = SuitConfiguration.of(range.charAt(2));
            return configuration.create(bigRank, EnumSet.of(smallRank));
        }
    }

    private enum SuitConfiguration {
        Unsuited('o') {
            @Override
            public List<HoleCards> create(Rank big, EnumSet<Rank> rankRange) {
                checkArgument(!rankRange.contains(big));

                List<HoleCards> parsed = new ArrayList<>();

                for (Rank small : rankRange) {
                    for (Suit suitOne : Suit.values()) {
                        for (Suit suitTwo : Suit.values()) {
                            if (suitOne != suitTwo) {
                                Card one = Card.from(big, suitOne);
                                Card two = Card.from(small, suitTwo);

                                HoleCards hc = HoleCards.of(one, two);
                                //System.out.println(hc);
                                parsed.add(hc);
                            }
                        }
                    }
                }
                return parsed;
            }

        },
        Suited('s') {
            public List<HoleCards> create(Rank big, EnumSet<Rank> rankRange) {
                checkArgument(!rankRange.contains(big));
                List<HoleCards> parsed = new ArrayList<>();

                for (Rank small : rankRange) {
                    for (Suit suit : Suit.values()) {
                        Card one = Card.from(big, suit);
                        Card two = Card.from(small, suit);

                        HoleCards hc = HoleCards.of(one, two);
                        //System.out.println(hc);
                        parsed.add(hc);
                    }
                }
                return parsed;
            }
        },
        Egal('e') {
            public List<HoleCards> create(Rank big, EnumSet<Rank> rankRange) {
                List<HoleCards> parsed = new ArrayList<>();

                parsed.addAll(Suited.create(big, rankRange));
                parsed.addAll(Unsuited.create(big, rankRange));

                return parsed;
            }
        };

        private final char representation;

        private SuitConfiguration(char config) {
            this.representation = config;
        }

        public abstract List<HoleCards> create(Rank big, EnumSet<Rank> rankRange);

        public static SuitConfiguration of(char instance) {
            for (SuitConfiguration value : SuitConfiguration.values()) {
                if (value.representation == instance) {
                    return value;
                }
            }
            throw new IllegalArgumentException("Errenous Suit representation:" + instance);
        }
    }


    /**
     * @param range "77+", "AJs+"
     * @return
     */
    private List<HoleCards> parseHoleAndBetter(String range) {
        Rank bigRank = Rank.fromChar(range.charAt(0));
        Rank smallRank = Rank.fromChar(range.charAt(1));

        if (bigRank == smallRank) {
            checkArgument(range.charAt(2) == '+');
            EnumSet<Rank> rankRange = fromAndBetter(bigRank);

            return createPairs(rankRange);
        } else {
            checkArgument(range.charAt(3) == '+');
            EnumSet<Rank> rankRange = fromAndToExcludingTop(smallRank, bigRank);

            SuitConfiguration configuration = SuitConfiguration.of(range.charAt(2));
            return configuration.create(bigRank, rankRange);
        }
    }

    private List<HoleCards> createPairs(EnumSet<Rank> rankRange) {
        List<HoleCards> parsed = new ArrayList<>();

        for (Rank rank : rankRange) {
            for (Suit suitOne : Suit.values()) {
                for (Suit suitTwo : Suit.values()) {
                    if (suitOne.ordinal() < suitTwo.ordinal()) {
                        Card one = Card.from(rank, suitOne);
                        Card two = Card.from(rank, suitTwo);

                        HoleCards hc = HoleCards.of(one, two);
                        parsed.add(hc);
                    }
                }
            }
        }

        return parsed;
    }

    private EnumSet<Rank> fromAndToInclusive(Rank bottom, Rank top) {
        return EnumSet.range(bottom, top);
    }

    private EnumSet<Rank> fromAndToExcludingTop(Rank bottom, Rank top) {
        EnumSet<Rank> range = EnumSet.range(bottom, top);
        range.remove(top);
        return range;
    }

    private EnumSet<Rank> fromAndBetter(Rank bottom) {
        return fromAndToInclusive(bottom, Rank.ACE);
    }
}