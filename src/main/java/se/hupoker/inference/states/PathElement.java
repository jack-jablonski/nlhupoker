package se.hupoker.inference.states;

import com.google.common.base.Objects;
import se.hupoker.common.Action;
import se.hupoker.common.Betting;
import se.hupoker.common.Position;
import se.hupoker.common.Street;
import se.hupoker.inference.BetPathDescription;

import static com.google.common.base.Preconditions.checkState;

/**
 * Structure describing a certain state in a hand along with an action taken at that state.
 *
 * @author Alexander Nyberg
 */
public class PathElement {
    private final Street street;
    private final float effective;
    private final BetPathDescription pathDescription;

    private final float pot;
    private final float toCall;
    // The result of this
    private final Action action;

    protected PathElement(Street street, float effective,
                          float ipagg, float oopagg, float pot, float toCall, Action action) {
        this.street = street;
        this.effective = effective;
        pathDescription = new BetPathDescription(ipagg, oopagg);
        this.pot = pot;
        this.toCall = toCall;
        this.action = action;

        checkState(getToCall() > 0 ? Betting.FCR.available().contains(action.getClassifier()) : Betting.CB.available().contains(action.getClassifier()));
    }

    public BetPathDescription getPathDescription() {
        return pathDescription;
    }

    public Street getStreet() {
        return street;
    }

    public float getEffective() {
        return effective;
    }

    public float getPot() {
        return pot;
    }

    public float getToCall() {
        return toCall;
    }

    public Action getAction() {
        return action;
    }

    public Position getPosition() {
        return action.getPosition();
    }

    public Betting getBettingType() {
        return Betting.get(action.getClassifier());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Position", getPosition())
                .add("Street", getStreet())
                .add("Effective", getEffective())
                .add("IpTocall", pathDescription.getIpTocall())
                .add("OopTocall", pathDescription.getOopTocall())
                .add("Pot", getPot())
                .add("Tocall", getToCall())
                .toString();
    }
}