package se.hupoker.inference.states;

import se.hupoker.common.*;
import se.hupoker.handhistory.HeadsUp;
import se.hupoker.handhistory.IllegalHandException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static se.hupoker.common.Street.RIVER;

/**
 * 
 * @author Alexander Nyberg
 *
 */
public class PathBuilder {
    private final PathHistory path = new PathHistory();

	private BigDecimal effectiveStartStack;
	private PositionMap<BigDecimal> currentStack = new PositionMap<>();
    private PositionMap<BigDecimal> investedInPot = new PositionMap<>(BigDecimal.ZERO);
    private PositionMap<BigDecimal> betInStreet = new PositionMap<>();

    private PositionMap<BigDecimal> lastCallOddsInStreet = new PositionMap<>();

	private PathBuilder() {
    }

    /**
     *
     * @param hand
     * @return Betting history of hand.
     * @throws IllegalHandException Betting is illegal.
     */
    public static PathHistory factory(HeadsUp hand) throws IllegalHandException {
        PathBuilder pathBuilder = new PathBuilder();

        pathBuilder.initialize(hand);
        pathBuilder.iterateActions(hand);

        return pathBuilder.path;
    }

    /**
	 * Starting states before any action is taken
	 */
	private void initialize(HeadsUp hand) {
		final BigDecimal bigBlind = hand.getBigBlind();

        BigDecimal minimumStack = new BigDecimal(Double.MAX_VALUE);
		for (Position pos : Position.values()) {
			minimumStack = minimumStack.min(hand.getSeated(pos).getStack());
		}

		effectiveStartStack = minimumStack.divide(bigBlind);

		// Texas Holdem rules.
		betInStreet.put(Position.IP, new BigDecimal(0.5));
        betInStreet.put(Position.OOP, new BigDecimal(1));

        // Beginning call odds
        lastCallOddsInStreet.put(Position.IP, new BigDecimal(0.25));
        lastCallOddsInStreet.put(Position.OOP, BigDecimal.ZERO);

        // Initial effective stacks
        for (Position pos : Position.values()) {
            BigDecimal initialEffectiveStack = effectiveStartStack.subtract(betInStreet.get(pos));
            currentStack.put(pos, initialEffectiveStack);
        }

		//System.out.println("Starting stacks: " + inpot[0] + " and " + inpot[1]);
	}

	/**
	 * Iterate each street & its actions.
	 * 
	 * @param hand
	 * @throws IllegalHandException
	 */
	private void iterateActions(HeadsUp hand) throws IllegalHandException {
		for (Street street : Street.values()) {
			List<Action> list = hand.getActions(street);
			if (list.isEmpty()) {
				break;
			}

			for (Action action : list) {
				addPathElement(street, action);
				updateForNewAction(hand, action);
			}

			if (!street.equals(RIVER)) {
				updateForNextStreet();
			}
		}
	}

	/**
	 * Add how the state looked before Action.
	 * 
	 * @param street
	 * @param act
	 */
	private void addPathElement(Street street, Action act) {
		PathElement element = new PathElement(
				street,
                effectiveStartStack.floatValue(),
				lastCallOddsInStreet.get(Position.IP).floatValue(),
                lastCallOddsInStreet.get(Position.OOP).floatValue(),
				potSize().floatValue(),
				toCall().floatValue(),
				act);

		path.add(street, element);
	}

    private BigDecimal getEffectiveActionSize(Position pos, BigDecimal actionSum) {
        BigDecimal possibleSize = currentStack.get(pos).add(betInStreet.get(pos));
        return actionSum.min(possibleSize);
    }

	/**
	 *
	 *
     * @param hand
     * @param act
     * @throws IllegalHandException
	 */
	private void updateForNewAction(HeadsUp hand, Action act) throws IllegalHandException {
		Position pos = act.getPosition();

		if (act.isAddingMoney()) {
			BigDecimal actionSum = act.getSum().divide(hand.getBigBlind());
			BigDecimal bbsum = getEffectiveActionSize(pos, actionSum);

			if (DoubleMath.isNegative(bbsum)) {
				throw new IllegalHandException("updateForNewAction: Negative bbsum");
			}

			if (act.isAggressive()) {
				BigDecimal additionalCash = bbsum.subtract(betInStreet.get(pos));
				if (DoubleMath.isNegative(additionalCash)) {
					throw new IllegalHandException("Negative (" + additionalCash + ") in " + hand.getHandId());
				}

                betInStreet.put(pos, bbsum);
                currentStack.put(pos, currentStack.get(pos).subtract(additionalCash));
			} else {
                betInStreet.put(pos, betInStreet.get(pos).add(actionSum));
                currentStack.put(pos, currentStack.get(pos).subtract(actionSum));
			}

            lastCallOddsInStreet.put(pos, getPotOdds());
		}
	}

    private void updateForNextStreet() {
		for (Position pos : Position.values()) {
			investedInPot.put(pos, investedInPot.get(pos).add(betInStreet.get(pos)));
			betInStreet.put(pos, BigDecimal.ZERO);
            lastCallOddsInStreet.put(pos, BigDecimal.ZERO);
		}
	}

    private BigDecimal getPotOdds() {
        return DoubleMath.divide(toCall(), potSize());
    }

	private BigDecimal toCall() {
        Collection<BigDecimal> invested = betInStreet.values();
        BigDecimal leastInvested = Collections.min(invested);

        return Collections.max(invested).subtract(leastInvested);
	}

	private BigDecimal potSize() {
		BigDecimal pot = BigDecimal.ZERO;

		for (Position pos : Position.values()) {
            pot = pot.add(investedInPot.get(pos));
            pot = pot.add(betInStreet.get(pos));
		}

		return pot;
	}
}