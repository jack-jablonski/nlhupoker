package se.hupoker.cards.boardisomorphisms;

import se.hupoker.cards.HoleCards;
import se.hupoker.common.EnumCounter;
import se.hupoker.cards.RankIndex;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Suit;

/**
 * Describes Texas hole card relation to a board.
 * 
 * @author Alexander Nyberg
 *
 */
class StandardHoleRelation extends RelationManager {
    private final CardSet board;
    private final EnumCounter<Suit> suitCounter = new EnumCounter<>(Suit.class);
    private final static RankIndex rankMap = new RankIndex();

    @Override
    public HoleRelation get(HoleCards hole) {
        int ranking = rankMap.get(hole);
        FlushConfiguration flush = getFlushing(hole);

        return new HoleRelation(ranking, flush);
    }

    protected StandardHoleRelation(CardSet board) {
        this.board = board;

        for (Card card : board) {
            suitCounter.increment(card.suitOf());
        }
    }

    /**
     *
     * @param hole
     * @return The suit configuration the hole cards have on this board.
     */
    private FlushConfiguration getFlushing(HoleCards hole) {
        Suit one = hole.first().suitOf();
        Suit two = hole.last().suitOf();

        int holeAdd = (one == two ? 2 : 1);
        int common = holeAdd + Math.max(suitCounter.get(one), suitCounter.get(two));

        if (common >= 5) {
            return FlushConfiguration.Flush;
        } else if (common == 4 && board.size() < 5) {
            return FlushConfiguration.Draw;
        } else if (common == 3 && board.size() == 3) {
            return FlushConfiguration.BackDraw;
        }

        return FlushConfiguration.None;
    }
}