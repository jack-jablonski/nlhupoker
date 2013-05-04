package se.hupoker.lut;

import se.hupoker.cards.HoleCards;
import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;

import java.util.Arrays;

/**
 *
 * This class is very ugly but necessary evil.
 *
 * @author Alexander Nyberg
 *
 */
public class LutKey {
    private final int[] ranks;
    private final int[] suits;

    /**
     *
     * @return Two first elements are sorted ranks of hole cards. Next elements are sorted
     * ranks of the board.
     */
    public int[] getRanks() {
        return ranks;
    }

    /**
     *
     * @return Two first elements are suits of hole cards. Next elements are
     * suits of the board. Ofcourse needs to correspond to getranks().
     */
    public int[] getSuits() {
        return suits;
    }

    /**
     * Transform (Board, Hole) representation to internal LUT presentation.
     *
     * @see {@link #getRanks()}
     * @see {@link #getSuits()}
     *
     * @param board Community cards.
     * @param hole Private hole cards.
     */
    public LutKey(CardSet board, HoleCards hole) {
        final int numberOfCards = board.size() + hole.size();

        ranks = new int[numberOfCards];
        suits = new int[numberOfCards];

        Card holeArray[] = hole.toArray(new Card[hole.size()]);
        Card boardArray[] = board.toArray(new Card[board.size()]);

        Arrays.sort(holeArray);
        Arrays.sort(boardArray);

        // Hole
        for (int i = 0; i < 2; i++) {
            getRanks()[i] = holeArray[i].rankOf().ordinal();
            getSuits()[i] = holeArray[i].suitOf().ordinal();
        }

        // Board
        for (int i = 0; i < board.size(); i++) {
            getRanks()[i + 2] = boardArray[i].rankOf().ordinal();
            getSuits()[i + 2] = boardArray[i].suitOf().ordinal();
        }
    }
}