package se.hupoker.inference.actiondistribution;

import se.hupoker.common.Betting;
import se.hupoker.common.Action;

/**
 * 
 * @author Alexander Nyberg
 *
 */
public class BetFactory {
    /**
	 * TODO: Please remove.
	 * 
	 * @param act
	 * @return
	 */
	public static int getIndex(Betting type, Action act) {
		if (type == Betting.CB) {
			return CBDistribution.actionMap.get(act.getClassifier());
		} else {
			return FCRDistribution.actionMap.get(act.getClassifier());
		}
	}

}