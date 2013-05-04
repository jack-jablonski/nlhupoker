package se.hupoker.inference.output;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.boardenumerator.BoardRunner;
import se.hupoker.cards.boardenumerator.EnumerateBoard;
import se.hupoker.cards.handeval.EquityMatrix;
import se.hupoker.common.Street;
import se.hupoker.cards.CardSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 * @author Alexander Nyberg
 */
public class OutputMeasures implements BoardRunner {
    private final String path;

    public OutputMeasures(String path) {
        this.path = path;
    }

    private String matlabDouble(double v) {
        DecimalFormat df = new DecimalFormat("#.####");

        if (Double.isNaN(v)) {
            return "NaN";
        } else {
            return df.format(v);
        }
    }

    private void printFlopMatrix(CardSet board) {
        final String filename = path + "\\" + board.toString();
        File matOut = new File(filename);
        if (matOut.exists()) {
            System.out.println("Exists: " + filename);
            return;
        }

        EquityMatrix me = EquityMatrix.from(board);

        try {
            PrintWriter pw = new PrintWriter(matOut);

            for (HoleCards hole : HoleCards.allOf()) {
                if (board.containsAny(hole)) {
                    continue;
                }
                double hs = me.getHs(hole);
                double ppot = me.getPpot(hole);
                double npot = me.getNpot(hole);

                String desc = matlabDouble(hs) + "," + matlabDouble(ppot) + "," + matlabDouble(npot);
                pw.println(desc);
            }

            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Wrote " + filename);
    }

    @Override
    public void evaluateBoard(CardSet board) {
        printFlopMatrix(board);
    }

    public static void main(String[] args) {
        OutputMeasures om = new OutputMeasures("TurnMeasures");
        EnumerateBoard turn = new EnumerateBoard(om, Street.TURN);
        turn.iterateAllBoards();
    }
}