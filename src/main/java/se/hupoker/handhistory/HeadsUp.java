package se.hupoker.handhistory;

import com.google.common.collect.Iterables;
import se.hupoker.cards.CardSet;
import se.hupoker.cards.HoleCards;
import se.hupoker.common.*;
import se.hupoker.poker.*;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static se.hupoker.poker.ActionClassifier.CALL;
import static se.hupoker.poker.ActionClassifier.CHECK;
import static se.hupoker.poker.ActionClassifier.FOLD;
import static se.hupoker.poker.Street.PREFLOP;

/**
 * Describes one heads up hand history.
 *
 * @author Alexander Nyberg
 */
public class HeadsUp {
    /*
     * Keep track on which street we're on.
     * TODO should preferably not be aware of this...
     */
    private Street street = Street.first();

    /*
     * All seated players.
     */
    private final Map<Integer, Seated> seatMap = new HashMap<>();
    private final PositionMap<Seated> seated = new PositionMap<>();

    /*
     *  All actions for each street
     */
    private final StreetHistory<Action> actionHistory = new StreetHistory<>();

    /*
     * The cards that come on each street.
     */
    private final StreetMap<CardSet> board = new StreetMap<>();

    /*
     * Unique Hand ID
     */
    private String handId;

    public String getHandId() {
        return handId;
    }

    public void setHandId(String id) {
        handId = id;
    }

    private BigDecimal bigBlind, smallBlind;

    public BigDecimal getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(BigDecimal bigBlind) {
        this.bigBlind = bigBlind;
    }

    public BigDecimal getSmallBlind() {
        return smallBlind;
    }

    public void setSmallBlind(BigDecimal smallBlind) {
        this.smallBlind = smallBlind;
    }

    /*
     * Position of button
     */
    private Integer button = null;

    public Integer getButton() {
        return button;
    }

    public void setButton(Integer id) {
        button = id;
    }

    public void addPlayer(Integer seat, String name, String stackStr) throws IllegalHandException {
        BigDecimal st = parseDecimal(stackStr);
        seatMap.put(seat, new Seated(name, st));
    }

    public Seated getSeated(Position pos) {
        return seated.get(pos);
    }

    /**
     * Initializes 'seated' to correct state.
     *
     * @throws IllegalHandException
     */
    public void setSeated() throws IllegalHandException {
        if (button == null) {
            throw new IllegalHandException("Button not set!");
        }

        if (seatMap.size() != Position.numberOf()) {
            throw new IllegalHandException("Wrong number of players.");
        }

        for (Map.Entry<Integer, Seated> entry : seatMap.entrySet()) {
            Integer seat = entry.getKey();

            if (seat.equals(getButton())) {
                seated.put(Position.IP, entry.getValue());
            } else {
                seated.put(Position.OOP, entry.getValue());
            }
        }
    }

    /**
     * @param newCards
     * @throws IllegalHandException Card representation malformed.
     */
    public void updateForNewStreet(String newCards) throws IllegalHandException {
        String trimmedCards = Formatter.trimCards(newCards);
        street = street.next();

        /**
         * Catch any errors and wrap them into IllegalHandException.
         */
        final CardSet parsedCards;
        try {
            parsedCards = CardSet.from(trimmedCards);
        } catch (Exception e) {
            throw new IllegalHandException("Malformed board on " + street + ": " + newCards, e);
        }

        if (parsedCards.size() != street.numberOfNewBoardCards()) {
            throw new IllegalHandException("Not the expected number of cards");
        }

        board.put(street, parsedCards);
    }

    /**
     * @throws IllegalHandException
     */
    public void setHoleCards(String name, String cards) throws IllegalHandException {
        Position pos = findSeat(name);
        String trimmedCards = Formatter.trimCards(cards);

        try {
            HoleCards hole = HoleCards.from(trimmedCards);
            seated.get(pos).setHand(hole);
        } catch (Exception e) {
            throw new IllegalHandException("Malformed holecards:" + cards);
        }
    }

    private boolean hasBoardOnStreet(Street turn) {
        return board.get(turn) != null;
    }

    public StreetMap<CardSet> getBoard() {
        return board;
    }

    /**
     * @param name
     * @return
     * @throws IllegalHandException
     */
    private Position findSeat(String name) throws IllegalHandException {
        for (Position pos : Position.values()) {
            String playerName = seated.get(pos).getName();
            if (playerName.equalsIgnoreCase(name)) {
                return pos;
            }
        }

        throw new IllegalHandException("No seated player:\"" + name + "\"");
    }

    public List<Action> getActions(Street st) {
        return actionHistory.get(st);
    }

    private void addAction(Action act) {
        actionHistory.add(street, act);
    }

    public void addFold(String name) throws IllegalHandException {
        Position pos = findSeat(name);

        Action a = new Action(pos, FOLD);
        addAction(a);
    }

    public void addCheck(String name) throws IllegalHandException {
        Position pos = findSeat(name);

        Action a = new Action(pos, CHECK);
        addAction(a);
    }

    private BigDecimal parseDecimal(String sum) throws IllegalHandException {
        BigDecimal parsedSum;

        try {
            parsedSum = Formatter.toDecimal(sum);
        } catch (NumberFormatException e) {
            throw new IllegalHandException("Action sum bad format:" + sum);
        }

        if (DoubleMath.isNegative(parsedSum)) {
            throw new IllegalHandException("Action sum non-positive:" + sum);
        }

        return parsedSum;
    }

    public void addCall(String name, String sum) throws IllegalHandException {
        Position pos = findSeat(name);

        Action a = new Action(pos, CALL, parseDecimal(sum));
        addAction(a);
    }

    public void addBet(String name, String sum) throws IllegalHandException {
        Position pos = findSeat(name);

        Action a = new Action(pos, ActionClassifier.BET, parseDecimal(sum));
        addAction(a);
    }

    /**
     * Add a raise action.
     * <p/>
     * Quirk: If IP calls to OOP, if OOP is aggressive it's called 'raise'
     * when it should be 'bet' since he can clearly 'check or bet'.
     *
     * @param name
     * @param sum
     * @throws IllegalHandException
     */
    public void addRaise(String name, String sum) throws IllegalHandException {
        Position pos = findSeat(name);
        ActionClassifier type = ActionClassifier.RAISE;

		/*
         * Quirk-fix: In (OOP, CB), if he puts in money it should be 'Bet' but
		 * some hand histories says 'Raise'. Treat amount-to-call==0 as Check/Bet.
		 */
        if (street.equals(PREFLOP)) {
            List<Action> ah = actionHistory.get(street);
            if (ah.size() == 1) {
                Action first = ah.get(0);
                if (first.ofClassifier(CALL)) {
                    type = ActionClassifier.BET;
                }
            }
        }

        Action a = new Action(pos, type, parseDecimal(sum));
        addAction(a);
    }

    /**
     * @return Does the hand look action-sequence-wise Ok?
     */
    public boolean verifyHand() {
        boolean previousStreetAction = true;

        /*
        * Make sure we don't find intermittent actions.
        */
        for (Street street : Street.values()) {
            List<Action> actions = actionHistory.get(street);

            if (!actions.isEmpty() && !previousStreetAction) {
                return false;
            }

            if (actions.isEmpty()) {
                previousStreetAction = false;
            }
        }

		/*
         * Check existing actions for all streets.
		 */
        for (Street street : Street.values()) {
            List<Action> actions = actionHistory.get(street);
            if (actions.isEmpty()) {
                break;
            }

            /*
             * All sequences must end in fold/call
             */
            ActionClassifier lastClassifier = Iterables.getLast(actions).getClassifier();
            EnumSet<ActionClassifier> sequenceEndsWith = EnumSet.of(FOLD, CHECK, CALL);
            if (!sequenceEndsWith.contains(lastClassifier)) {
                return false;
            }

            /**
             * If more streets see we have at least two more actions
             */
            EnumSet<ActionClassifier> expectMoreActions = EnumSet.of(CHECK, CALL);
            if (expectMoreActions.contains(lastClassifier) && street.hasNext()) {
                Street next = street.next();
                List<Action> nextActions = actionHistory.get(next);
                if (nextActions.size() < 2) {
                    return false;
                }
            }

			/*
			 * If someone is folding in CB state skip this hand.
			 */
            EnumSet<Street> postFlop = EnumSet.of(Street.FLOP, Street.TURN, Street.RIVER);
            if (postFlop.contains(street)) {
                if (actions.size() == 1) {
                    System.out.println(street + "|" + getHandId() + "| Only one action taken on street");
                    return false;
                } else if (actions.size() == 2) {
                    if (actions.get(0).ofClassifier(CHECK) && actions.get(1).ofClassifier(FOLD)) {
                        System.out.println("Assh**** folding in CB");
                        return false;
                    }
                }
            }
        }

		/*
		 * Make sure having turn means having flop
		 */
        if (hasBoardOnStreet(Street.TURN) && !hasBoardOnStreet(Street.FLOP)) {
            System.out.println("Missing streets from TURN");
            return false;
        }

		/*
		 * Make sure having river means having turn
		 */
        if (hasBoardOnStreet(Street.RIVER) && !hasBoardOnStreet(Street.TURN)) {
            System.out.println("Missing streets from RIVER");
            return false;
        }

        return true;
    }
}