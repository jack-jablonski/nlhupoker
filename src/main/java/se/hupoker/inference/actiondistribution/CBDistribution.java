package se.hupoker.inference.actiondistribution;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import se.hupoker.common.Action;
import se.hupoker.common.ActionClassifier;

import java.util.EnumSet;

/**
 * @author Alexander Nyberg
 */
public class CBDistribution extends ActionDistribution {
    private final ImmutableMap<ActionClassifier, Integer> actionMap = ImmutableMap.<ActionClassifier, Integer>builder()
            .put(ActionClassifier.CHECK, 0)
            .put(ActionClassifier.BET, 1)
            .build();

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