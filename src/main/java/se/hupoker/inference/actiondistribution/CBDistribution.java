package se.hupoker.inference.actiondistribution;

import se.hupoker.common.Action;
import se.hupoker.common.ActionClassifier;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * @author Alexander Nyberg
 */
public class CBDistribution extends ActionDistribution {
    public static final EnumMap<ActionClassifier, Integer> actionMap = new EnumMap<>(ActionClassifier.class);
    static {
        actionMap.put(ActionClassifier.CHECK, 0);
        actionMap.put(ActionClassifier.BET, 1);
    }

    public CBDistribution(String desc, EnumSet<ActionDistOptions> options) {
        super(desc, options);
    }

    @Override
    protected int getNumberOfActions() {
        return actionMap.size();
    }

    @Override
    protected int getActionIndex(Action act) {
        return actionMap.get(act.getClassifier());
    }
}