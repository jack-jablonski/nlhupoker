package se.hupoker.inference.actiondistribution;

import se.hupoker.common.Action;
import se.hupoker.common.ActionClassifier;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * @author Alexander Nyberg
 */
public class FCRDistribution extends ActionDistribution {
    public final static EnumMap<ActionClassifier, Integer> actionMap = new EnumMap<>(ActionClassifier.class);
    static {
        actionMap.put(ActionClassifier.FOLD, 0);
        actionMap.put(ActionClassifier.CALL, 1);
        actionMap.put(ActionClassifier.RAISE, 2);
    }

    public FCRDistribution(String desc, EnumSet<ActionDistOptions> options) {
        super(desc, options);

        if (options.contains(ActionDistOptions.NOFOLD)) {
//            setProbability(ActionType.Fold, 0.0);
//            probability.set(0, 0.0);
//            probability.normalize();
        }
    }

    @Override
    protected int getActionIndex(Action act) {
        return actionMap.get(act.getClassifier());
    }

    @Override
    protected int getNumberOfActions() {
        return actionMap.size();
    }
}