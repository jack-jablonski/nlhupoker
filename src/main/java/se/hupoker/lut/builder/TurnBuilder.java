package se.hupoker.lut.builder;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardenumerator.BoardRunner;
import se.hupoker.cards.boardenumerator.EnumerateBoard;
import se.hupoker.cards.handeval.EquityMatrix;
import se.hupoker.common.Street;
import se.hupoker.lut.LutKey;
import se.hupoker.lut.LutPath;
import se.hupoker.lut.TurnTable;
import se.hupoker.cards.CardSet;

/**
 *
 * @author Alexander Nyberg
 *
 */
final class TurnBuilder implements BoardRunner {
    private final TurnTable turn_hs = new TurnTable();
    private final TurnTable turn_ppot = new TurnTable();
    private final TurnTable turn_npot = new TurnTable();

    public void save() {
        turn_hs.save(LutPath.getTurnHs());
        turn_ppot.save(LutPath.getTurnPpot());
        turn_npot.save(LutPath.getTurnNpot());
    }

    private void dryRun(CardSet board) {
        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            final LutKey key = new LutKey(board, hole);
            turn_hs.setManually(key, (float) hole.ordinal());
            turn_ppot.setManually(key, (float) hole.ordinal());
            turn_npot.setManually(key, (float) hole.ordinal());
        }
    }

    private void realRun(CardSet board) {
        EquityMatrix me = EquityMatrix.from(board);

        for (HoleCards hole : HoleCards.allOf()) {
            if (board.containsAny(hole)) {
                continue;
            }

            final LutKey key = new LutKey(board, hole);
            turn_hs.setManually(key, (float) me.getHs(hole));
            turn_ppot.setManually(key, (float) me.getPpot(hole));
            turn_npot.setManually(key, (float) me.getNpot(hole));
        }
    }

    @Override
    public void evaluateBoard(CardSet board) {
        //dryRun(board);
        realRun(board);
        System.out.println("TurnBuilder set " + board);
    }

    public static void main(String[] args) {
        TurnBuilder turnBuilder = new TurnBuilder();
        EnumerateBoard turnEnumerator = new EnumerateBoard(turnBuilder, Street.TURN);

        turnEnumerator.iterateAllBoards();
        turnBuilder.save();
    }
}