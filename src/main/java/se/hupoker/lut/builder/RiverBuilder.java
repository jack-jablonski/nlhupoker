package se.hupoker.lut.builder;

import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardenumerator.BoardRunner;
import se.hupoker.cards.boardenumerator.IsomorphicBoardEnumerator;
import se.hupoker.cards.handeval.EquityMatrix;
import se.hupoker.common.Street;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutPath;
import se.hupoker.lut.RiverTable;

/**
 *
 * @author Alexander Nyberg
 *
 */
final class RiverBuilder implements BoardRunner {
    private final RiverTable river_hs = new RiverTable();

    public void save() {
        river_hs.save(LutPath.getRiverHs());
    }

    @Override
    public void evaluateBoard(CardSet board) {
        EquityMatrix me = EquityMatrix.factory(board);

        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            LutKey key = new LutKey(board, hole);
            river_hs.setManually(key, (float) me.getAverageEquity(hole));
        }

        System.out.println("RiverBuilder set " + board);
    }

    public static void main(String[] args) {
        RiverBuilder build = new RiverBuilder();
        IsomorphicBoardEnumerator river = new IsomorphicBoardEnumerator(build, Street.RIVER);

        river.enumerate();
        river.printCompletedStats();
        build.save();
    }
}