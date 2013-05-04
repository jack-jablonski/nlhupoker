package se.hupoker.cards.handeval;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.Rank;
import se.hupoker.cards.Suit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

public class CardEvaluationMain {
    public static void printHoleCards() {
        try {
            File matOut = new File("Holes.txt");
            PrintWriter pw = new PrintWriter(matOut);

            for (HoleCards hole : HoleCards.allOf()) {
                pw.println(hole.ordinal() + "=" + hole);
            }

            pw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String matlabDouble(double v) {
        DecimalFormat df = new DecimalFormat("#.####");

        if (Double.isNaN(v)) {
            return "NaN";
        } else {
            return df.format(v);
        }
    }

    public static void printFlopMatrix(CardSet board) {
        String filename = "Flop\\" + board.toString();

        File matOut = new File(filename);
        if (matOut.exists()) {
            System.out.println("Exists: " + filename);
            return;
        }
/*
        EquityMatrix me = new EquityMatrix(board);
		try {
			PrintWriter pw = new PrintWriter(matOut);
			
			for (double [] v : me.equities) {
				pw.print(matlabDouble(v[0]));
				
				for (int i=1; i < v.length; i++) {
					double d = v[i];
					
					final String f = "," + matlabDouble(d);
					pw.print(f);
				}
				pw.println();
			}
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
        System.out.println("Wrote: " + filename);
    }

    public static void printFlops() {
        for (Rank i : Rank.values()) {
            for (Rank j : Rank.values()) {
                for (Rank k : Rank.values()) {
                    if (i.ordinal() < j.ordinal() && j.ordinal() < k.ordinal()) {
                        CardSet board = new CardSet();

                        board.add(Card.from(i, Suit.CLUB));
                        board.add(Card.from(j, Suit.DIAMOND));
                        board.add(Card.from(k, Suit.HEART));

                        printFlopMatrix(board);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
/*
		printHoleCards();
		
		CardSet board = new CardSet();
		board.add(new Card("9c"));
		board.add(new Card("8h"));
		board.add(new Card("4s"));
		
		//printFlopMatrix(board);
		
		printFlops();*/

        //System.out.println(Arrays.toString(hse.equities));
    }
}
