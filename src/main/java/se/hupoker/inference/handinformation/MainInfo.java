package se.hupoker.inference.handinformation;


import se.hupoker.handhistory.HandParser;
import se.hupoker.handhistory.HeadsUp;
import se.hupoker.handhistory.IllegalHandException;
import se.hupoker.inference.states.PathBuilder;
import se.hupoker.inference.states.PathHistory;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Alexander Nyberg
 */
class MainInfo {
    private Collection<PathHistory> extractPaths(Collection<HeadsUp> list) {
        Collection<PathHistory> historyCollection = new LinkedList<>();

        for (HeadsUp hu : list) {
            try {
                PathHistory pb = PathBuilder.createPath(hu);
                historyCollection.add(pb);
            } catch (IllegalHandException e) {
                System.out.println("Broken hand.");
            }
        }

        return historyCollection;
    }

    public void loadAndRun(String[] args) {
        HandParser par = new HandParser();
        par.parse(args[0]);
        Collection<HeadsUp> hh = par.getHandList();

        Collection<PathHistory> pathList = extractPaths(hh);

        PathClassifier classifier = new PathClassifier();
        classifier.classify(pathList);
    }

    /**
     * @param args Path of the directory to parse
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Not enough input parameters");
            return;
        }

        new MainInfo().loadAndRun(args);
    }
}