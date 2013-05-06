package se.hupoker.lut.builder;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardenumerator.BoardRunner;
import se.hupoker.cards.boardenumerator.EnumerateBoard;
import se.hupoker.cards.handeval.EquityMatrix;
import se.hupoker.common.Street;
import se.hupoker.lut.FlopTable;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutPath;
import se.hupoker.cards.CardSet;

/**
 *
 * @author Alexander Nyberg
 */
final class FlopBuilder implements BoardRunner {
    private final FlopTable flop_hs = new FlopTable();
    private final FlopTable flop_ppot = new FlopTable();
    private final FlopTable flop_npot = new FlopTable();

    public void save() {
        flop_hs.save(LutPath.getFlopHs());
        flop_ppot.save(LutPath.getFlopPpot());
        flop_npot.save(LutPath.getFlopNpot());
    }

    private void dryRun(CardSet board) {
        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            final LutKey key = new LutKey(board, hole);
            flop_hs.setManually(key, (float) hole.ordinal());
            flop_ppot.setManually(key, (float) hole.ordinal());
            flop_npot.setManually(key, (float) hole.ordinal());
        }
    }

    private void realRun(CardSet board) {
        EquityMatrix me = EquityMatrix.factory(board);

        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            final LutKey key = new LutKey(board, hole);
            flop_hs.setManually(key, (float) me.getHs(hole));
            flop_ppot.setManually(key, (float) me.getPpot(hole));
            flop_npot.setManually(key, (float) me.getNpot(hole));
        }
    }

    @Override
    public void evaluateBoard(CardSet board) {
        dryRun(board);
//        realRun(board);
        System.out.println("FlopBuilder set " + board);
    }

    public static void main(String[] args) {
        FlopBuilder build = new FlopBuilder();
        EnumerateBoard flop = new EnumerateBoard(build, Street.FLOP);

        flop.iterateAllBoards();
        flop.printCompletedStats();

//        build.save();
    }
}