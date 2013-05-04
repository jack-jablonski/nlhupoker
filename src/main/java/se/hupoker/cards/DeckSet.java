package se.hupoker.cards;

import java.util.*;

/**
 * Container of full or near full deck of cards. Class is specialised for:
 * -> iteration
 * -> contains
 * -> removal
 *
 * @author Alexander Nyberg
 */
public class DeckSet extends HashSet<Card> {
    public DeckSet(DeckSet deck) {
        super(deck);
    }

    private DeckSet(Collection<? extends Card> cards) {
        super(cards);
    }

    /**
     * Return an ordered 52-card deck.
     *
     * @return a 52-card deck in order from clubs to spades and within each suit from deuce to Ace.
     */
    public static DeckSet freshDeck() {
        return new DeckSet(Card.allOf());
    }

    /**
     * @return An ordered shuffled 52-card deck.
     */
    public static List<Card> shuffledDeck() {
        List<Card> shuffled = new ArrayList<>(freshDeck());
        Collections.shuffle(shuffled);
        return shuffled;
    }
}

