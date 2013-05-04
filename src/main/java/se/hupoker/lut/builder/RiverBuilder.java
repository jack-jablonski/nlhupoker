package se.hupoker.lut.builder;

import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardenumerator.BoardRunner;
import se.hupoker.cards.boardenumerator.EnumerateBoard;
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

    private void dryRun(CardSet board) {
        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            river_hs.setManually(new LutKey(board, hole), (float) hole.ordinal());
        }
    }

    private void realRun(CardSet board) {
        EquityMatrix me = EquityMatrix.from(board);

        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            river_hs.setManually(new LutKey(board, hole), (float) me.getAverageEquity(hole));
        }
    }

    @Override
    public void evaluateBoard(CardSet board) {
//        dryRun(board);
        realRun(board);
        System.out.println("RiverBuilder set " + board);
    }

    public static void main(String[] args) {
        RiverBuilder build = new RiverBuilder();
        EnumerateBoard river = new EnumerateBoard(build, Street.RIVER);

        river.iterateAllBoards();
        river.printCompletedStats();
        build.save();
    }
}