package se.hupoker.inference.actiondistribution;

import com.google.common.collect.ImmutableMap;
import se.hupoker.poker.Action;
import se.hupoker.poker.ActionClassifier;

import java.util.EnumSet;

/**
 * @author Alexander Nyberg
 */
public class FCRDistribution extends ActionDistribution {
    private final static ImmutableMap<ActionClassifier, Integer> actionMap = ImmutableMap.<ActionClassifier, Integer>builder()
            .put(ActionClassifier.FOLD, 0)
            .put(ActionClassifier.CALL, 1)
            .put(ActionClassifier.RAISE, 2)
            .build();

    public FCRDistribution(String desc, EnumSet<ActionDistOptions> options) {
        super(desc, options);
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