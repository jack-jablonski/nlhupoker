package se.hupoker.inference.states;

import com.google.common.base.Objects;
import se.hupoker.poker.Betting;
import se.hupoker.common.DoubleMath;
import se.hupoker.poker.Position;
import se.hupoker.poker.Street;

/**
 * Immutable description of state.
 *
 * @author Alexander Nyberg
 */
public class GenericState {
	private Street street;
	private Position position;
    private double iptocall, ooptocall;

    public GenericState(Street street, Position position, double iptocall, double ooptocall) {
        this.street = street;
        this.position = position;
        this.iptocall = iptocall;
        this.ooptocall = ooptocall;
    }

    public Betting getBetting() {
        if (position == Position.IP) {
            return getBettingFromAmount(getIptocall());
        } else {
            return getBettingFromAmount(getOoptocall());
        }
    }

    private Betting getBettingFromAmount(double toCall) {
        return toCall > 0 ? Betting.FCR : Betting.CB;
    }

    /**
     * Distance between state and PathElement
     *
     * @param pe
     * @return Distance measure >= 0.
     */
    public double getDistance(PathElement pe) {
        if (isBettingPathZero(pe)) {
            return Double.POSITIVE_INFINITY;
        }

        return Math.abs(getIptocall() - pe.getPathDescription().getIpTocall()) + Math.abs(getOoptocall() - pe.getPathDescription().getOopTocall());
    }

    /**
     * Never match
     */
    private boolean isBettingPathZero(PathElement pe) {
        if (DoubleMath.isZero(getIptocall()) && pe.getPathDescription().getIpTocall() > 0) {
            return true;
        }

        if (DoubleMath.isZero(getOoptocall()) && pe.getPathDescription().getOopTocall() > 0) {
            return true;
        }

        return false;
    }

    public double getIptocall() {
        return iptocall;
    }

    public double getOoptocall() {
        return ooptocall;
    }

	@Override
	public int hashCode() {
		return Objects.hashCode(getStreet(), getPosition());
	}

	@Override
	public boolean equals(Object obj) {
//		GenericState other = (GenericState) obj;

        throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
        return Objects.toStringHelper(this)
            .add("Position", getPosition())
            .add("Street", getStreet())
            .add("IpTocall", getIptocall())
            .add("OopTocall", getOoptocall())
            .toString();
	}

    public Position getPosition() {
        return position;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    /**
     *
     * @return Does this state have child states?
     */
    public boolean hasChild() {
        if (street == Street.RIVER) {
            return false;
        }

        // TODO: first out preflop

        // first out post flop does not have children!
        if (position == Position.OOP && street != Street.PREFLOP) {
            if (getIptocall() == 0 && getOoptocall() == 0) {
                return false;
            }
        }

        return true;
    }

    public static class GenericStateBuilder {
        public Street street;
        public Position position;
        public double iptocall, ooptocall;

        public GenericState build() {
            return new GenericState(street, position, iptocall, ooptocall);
        }
    }
}