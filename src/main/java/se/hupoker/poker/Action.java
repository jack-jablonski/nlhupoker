package se.hupoker.poker;

import se.hupoker.common.DoubleMath;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * 
 * @author Alexander Nyberg
 *
 */
public class Action {
    private final Position position;
	private final ActionClassifier classifier;
    private BigDecimal sum;

	public Action(Position pos, ActionClassifier which) {
		this.position = pos;
        this.classifier = which;
	}

	public Action(Position pos, ActionClassifier which, BigDecimal sum) {
		this(pos, which);
        checkArgument(!DoubleMath.isNegative(sum), "Don't use if there is no sum!");
        this.sum = sum;
	}

    public BigDecimal getSum() {
        checkNotNull(sum);
        return sum;
    }

    public boolean ofClassifier(ActionClassifier classifier) {
        return this.classifier == classifier;
    }

    public boolean isAggressive() {
        return ofClassifier(ActionClassifier.BET) || ofClassifier(ActionClassifier.RAISE);
    }

	public boolean isAddingMoney() {
		return ofClassifier(ActionClassifier.CALL) || isAggressive();
	}

    public ActionClassifier getClassifier() { return classifier; }
    public Position getPosition() { return position; }
}