package se.hupoker.lut.builder;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardenumerator.BoardRunner;
import se.hupoker.cards.boardenumerator.IsomorphicBoardEnumerator;
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
class FlopBuilder implements BoardRunner {
    private final FlopTable flop_hs = new FlopTable();
    private final FlopTable flop_ppot = new FlopTable();
    private final FlopTable flop_npot = new FlopTable();

    public void save() {
        flop_hs.save(LutPath.getFlopHs());
        flop_ppot.save(LutPath.getFlopPpot());
        flop_npot.save(LutPath.getFlopNpot());
    }

    @Override
    public void evaluateBoard(CardSet board) {
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

        System.out.println("FlopBuilder set " + board);
    }

    public static void main(String[] args) {
        FlopBuilder build = new FlopBuilder();
        IsomorphicBoardEnumerator flop = new IsomorphicBoardEnumerator(build, Street.FLOP);

        flop.enumerate();
        flop.printCompletedStats();

        build.save();
    }
}