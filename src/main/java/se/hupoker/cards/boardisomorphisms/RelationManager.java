package se.hupoker.cards.boardisomorphisms;

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
//        if (board.size() == 5) {
//            return new RiverHoleRelation(board);
//        } else {
//            return new StandardHoleRelation(board);
//        }

        return new StandardHoleRelation(board);
    }

    abstract protected HoleRelation get(HoleCards hole);
}