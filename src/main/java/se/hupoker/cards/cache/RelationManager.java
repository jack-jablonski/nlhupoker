package se.hupoker.cards.cache;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.CardSet;

/**
 * Given some board:
 * f: HoleCards -> Abstract representation
 *
 * @author Alexander Nyberg
 */
abstract class RelationManager {

    protected static RelationManager factory(CardSet board) {
        // More clever than previously thought!
        return new StandardHoleRelation(board);
    }

    abstract protected HoleRelation get(HoleCards hole);
}