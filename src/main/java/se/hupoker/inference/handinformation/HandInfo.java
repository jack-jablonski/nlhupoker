package se.hupoker.inference.handinformation;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.poker.Position;
import se.hupoker.poker.PositionMap;
import se.hupoker.poker.Street;
import se.hupoker.poker.StreetMap;
import se.hupoker.handhistory.HeadsUp;
import se.hupoker.handhistory.IllegalHandException;
import se.hupoker.handhistory.Seated;
import se.hupoker.inference.states.PathBuilder;
import se.hupoker.inference.states.PathHistory;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * Normalize everything to 0.5/1 level.
 * Present players as IP or OOP.
 *
 * @author Alexander Nyberg
 */
public class HandInfo {
    private final PathHistory path;
    // Complete board for every street.
    private final StreetMap<CardSet> board;
    // The objective possible hands a player could hold (after hand is played out)
    private final PositionMap<HolePossible> holePossible;

    private HandInfo(PathHistory path, StreetMap<CardSet> board, PositionMap<HolePossible> holePossible) {
        this.path = path;
        this.board = board;
        this.holePossible = holePossible;
    }

    /**
     * Create & initialize from HeadsUp
     *
     * @param hand
     * @return Another view of 'hand'
     * @throws IllegalHandException if erroneous hand.
     */
    public static HandInfo factory(HeadsUp hand) throws IllegalHandException {
        PathHistory handPath = PathBuilder.factory(hand);
        StreetMap<CardSet> completeBoard = initializeBoard(hand.getBoard());

        CardSet lastCompleteBoard = getLastCompleteBoard(completeBoard);
        PositionMap<HolePossible> holePossible = initializeHolePossible(hand, lastCompleteBoard);

        return new HandInfo(handPath, completeBoard, holePossible);
    }

    /**
     * TODO: Remove possible through seeing opponent showdown, preferably in separate calculate.
     * Remove all known board cards from holepossible
     */
    private static PositionMap<HolePossible> initializeHolePossible(HeadsUp hand, CardSet lastCompleteBoard) {
        PositionMap<HolePossible> holePossible = new PositionMap<>(HolePossible.class);

        for (HolePossible hp : holePossible.values()) {
            hp.remove(lastCompleteBoard);
        }

        for (Position pos : Position.values()) {
            Seated player = hand.getSeated(pos);

            HoleCards shown = player.getHand();
            if (shown != null) {
				 // HolePossible should contain a single point.
                HolePossible hp = holePossible.get(pos);
                hp.setKnown(shown);
            }
        }

        return holePossible;
    }

    private static CardSet getLastCompleteBoard(StreetMap<CardSet> completeBoard) {
        List<Street> streetsWithBoard = Lists.newArrayList(Street.RIVER, Street.TURN, Street.FLOP);

        for (Street street : streetsWithBoard) {
            if (completeBoard.containsKey(street)) {
                return completeBoard.get(street);
            }
        }
        return new CardSet();
    }

    public HolePossible getHolePossible(Position pos) {
        return holePossible.get(pos);
    }

    /**
     * For each street set the complete board.
     */
    private static StreetMap<CardSet> initializeBoard(StreetMap<CardSet> perStreet) {
        List<Street> streetsWithBoard = Lists.newArrayList(Street.FLOP, Street.TURN, Street.RIVER);
        StreetMap<CardSet> completeBoard = new StreetMap<>();
        CardSet currentComplete = new CardSet();

        for (Street street : streetsWithBoard) {
            if (!perStreet.containsKey(street)) {
                break;
            }

            CardSet currentStreet = perStreet.get(street);
            currentComplete.addAll(currentStreet);

            // Copy & insert into Map
            CardSet insertionSet = new CardSet(ImmutableList.copyOf(currentComplete));
            completeBoard.put(street, insertionSet);
        }

        return completeBoard;
    }

    public PathHistory getPath() {
        return path;
    }

    public CardSet getBoard(Street street) {
        return board.get(street);
    }
}