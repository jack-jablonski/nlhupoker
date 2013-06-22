package se.hupoker.cards.handeval;

import se.hupoker.cards.Card;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.cache.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Alexander Nyberg
 */
public class HoleCardCombinations {
    final int[][] inner = {{0, 1, 2, 3}, {0, 2, 1, 3}, {0, 3, 1, 2}};

    public Set<Pair<HoleCards>> get(List<Card> cards) {
        checkArgument(cards.size() == 4);

        Set<Pair<HoleCards>> pairSet = new HashSet<>();

        for (int combination[] : inner) {
            Card myOne = cards.get(combination[0]);
            Card myTwo = cards.get(combination[1]);
            HoleCards myHole = HoleCards.of(myOne, myTwo);

            Card opOne = cards.get(combination[2]);
            Card opTwo = cards.get(combination[3]);
            HoleCards opHole = HoleCards.of(opOne, opTwo);

            pairSet.add(new Pair<>(myHole, opHole));
        }

        return pairSet;
    }
}