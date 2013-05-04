package se.hupoker.cards.boardisomorphisms;

import se.hupoker.cards.*;
import se.hupoker.common.EnumCounter;

/**
 *
 * @author Alexander Nyberg
 *
 */
public class RiverHoleRelation extends RelationManager {
    private final EnumCounter<Suit> suitCounter = new EnumCounter<>(Suit.class);
    private final static RankIndex rankMap = new RankIndex();

    public RiverHoleRelation(CardSet board) {
        for (Card card : board) {
            suitCounter.increment(card.suitOf());
        }
    }

    @Override
    public HoleRelation get(HoleCards hole) {
        int ranking = rankMap.get(hole);
        FlushConfiguration flush = getFlush(hole);

        return new HoleRelation(ranking, flush);
    }

    private FlushConfiguration getFlush(HoleCards hole) {
        Suit one = hole.first().suitOf();
        Suit two = hole.last().suitOf();

        int holeAdd = (one == two ? 2 : 1);
        int common = holeAdd + Math.max(suitCounter.get(one), suitCounter.get(two));

        if (common >= 5) {
            return FlushConfiguration.Flush;
        } else {
            return FlushConfiguration.None;
        }
    }
}