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
        return new StandardHoleRelation(board);
    }

    /*protected static RelationManager factory(CardSet board) {
        switch (board.size()) {
            case 3:
                return new StandardHoleRelation(board);
            case 4:
                return new StandardHoleRelation(board);
            case 5:
                return new RiverHoleRelation(board);
//                return new StandardHoleRelation(board);
            default:
                throw new IllegalArgumentException("Board size unknown:" + board);
        }
    }*/

    abstract protected HoleRelation get(HoleCards hole);
}