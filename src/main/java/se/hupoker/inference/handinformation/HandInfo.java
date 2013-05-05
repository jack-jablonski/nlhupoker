package se.hupoker.inference.handinformation;


import se.hupoker.cards.Card;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.common.Position;
import se.hupoker.common.PositionMap;
import se.hupoker.common.Street;
import se.hupoker.common.StreetMap;
import se.hupoker.handhistory.HeadsUp;
import se.hupoker.handhistory.IllegalHandException;
import se.hupoker.handhistory.Seated;
import se.hupoker.inference.states.PathBuilder;
import se.hupoker.inference.states.PathHistory;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkState;

/**
 * Normalize everything to 0.5/1 level.
 * Present players as IP or OOP.
 *
 * @author Alexander Nyberg
 */
public class HandInfo {
    private final HeadsUp hand;
    private PathHistory path;

    // Complete board for every street.
    private final StreetMap<CardSet> board = new StreetMap<>();
    // The objective possible hands a player could hold (after hand is played out)
    private final PositionMap<HolePossible> holePossible = new PositionMap<>(HolePossible.class);
//    private final PositionMap<HolePossible> holePossible = null;

    /**
     * Create & initialize from HeadsUp
     *
     * @param hs
     * @return
     * @throws IllegalHandException Erroneous hand.
     */
    public static HandInfo factory(HeadsUp hs) throws IllegalHandException {
        HandInfo info = new HandInfo(hs);
        info.extract();

        return info;
    }

    private HandInfo(HeadsUp hand) {
        this.hand = hand;
    }

    /**
     * @throws IllegalHandException Something erroneous with hand.
     */
    private void extract() throws IllegalHandException {
        initializeBoard();
        initializeHolePossible();

        path = PathBuilder.createPath(getHand());
    }

    /**
     * TODO: Remove known
     * Remove all known board cards from holepossible
     */
    private void initializeHolePossible() {
        CardSet completeBoard = getCompleteBoard();

        for (HolePossible hp : holePossible.values()) {
            hp.remove(completeBoard);

            if (completeBoard.size() == 0) {
                checkState(hp.numberOfPossible() == 1326);
            } else if (completeBoard.size() == 3) {
                checkState(hp.numberOfPossible() == 1176);
            } else if (completeBoard.size() == 4) {
                checkState(hp.numberOfPossible() == 1128);
            } else if (completeBoard.size() == 5) {
                checkState(hp.numberOfPossible() == 1081);
            } else {
                throw new IllegalStateException();
            }
        }

        for (Position pos : Position.values()) {
            Seated player = getHand().getSeated(pos);

            HoleCards shown = player.getHand();
            if (shown != null) {
                /*
				 * HolePossible should contain a single point now.
				 */
                HolePossible hp = getHolePossible(pos);
                hp.setKnownHole(shown);
            }
        }
    }

    private CardSet getCompleteBoard() {
        if (board.containsKey(Street.RIVER)) {
            return board.get(Street.RIVER);
        } else if (board.containsKey(Street.TURN)) {
            return board.get(Street.TURN);
        } else if (board.containsKey(Street.FLOP)) {
            return board.get(Street.FLOP);
        } else {
            return new CardSet();
        }
    }

    public HolePossible getHolePossible(Position pos) {
        return holePossible.get(pos);
    }

    /**
     * @param street
     * @param previous Board from previous street. Don't modify!
     * @return Complete board belonging to street.
     */
    private CardSet setBoard(Street street, Set<Card> previous, CardSet newCards) {
        CardSet fullBoardCards = new CardSet();

        fullBoardCards.addAll(previous);
        fullBoardCards.addAll(newCards);

        board.put(street, fullBoardCards);

        return fullBoardCards;
    }

    /**
     * For each street set the complete board.
     */
    private void initializeBoard() {
        Set<Street> streetsWithBoard = EnumSet.of(Street.FLOP, Street.TURN, Street.RIVER);
        CardSet cards = new CardSet();

        for (Street st : streetsWithBoard) {
            if (getHand().hasBoard(st)) {
                CardSet streetCards = getHand().getBoard(st);
                Set<Card> immutableCards = Collections.unmodifiableSet(cards);

                cards = setBoard(st, immutableCards, streetCards);
            }
        }
    }

    public HeadsUp getHand() {
        return hand;
    }

    public PathHistory getPath() {
        return path;
    }

    public CardSet getBoard(Street street) {
        return board.get(street);
    }
}