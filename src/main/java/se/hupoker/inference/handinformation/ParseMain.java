package se.hupoker.inference.handinformation;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import se.hupoker.common.Street;
import se.hupoker.handhistory.HandParser;
import se.hupoker.handhistory.HeadsUp;
import se.hupoker.handhistory.IllegalHandException;
import se.hupoker.inference.states.PathBuilder;
import se.hupoker.inference.states.PathHistory;

import java.util.Collection;

/**
 * @author Alexander Nyberg
 */
public class ParseMain {

    /**
     * @param args Path of the directory to parse
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Not enough input parameters");
            return;
        }

        HandParser par = new HandParser();
        par.parse(args[0]);
        Collection<HeadsUp> headsUpList = par.getHandList();

        Function<HeadsUp, PathHistory> pathExtraction = new Function<HeadsUp, PathHistory>() {
            @Override
            public PathHistory apply(HeadsUp input) {
                try {
                    return PathBuilder.factory(input);
                } catch (IllegalHandException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        Collection<PathHistory> historyCollection = Collections2.transform(headsUpList, pathExtraction);
        for (PathHistory history : historyCollection) {
            System.out.println(history.get(Street.PREFLOP));
        }

    }
}