package se.hupoker.inference.tree;

import se.hupoker.inference.Configuration;
import se.hupoker.inference.handinformation.HandInfo;

import java.util.Collection;

/**
 *
 * @author Alexander Nyberg
 */
public class SerialOptimization implements TreeOptimizer {
    private final GameTree tree;

    public SerialOptimization(GameTree tree) {
        this.tree = tree;
    }

    public void optimize(Collection<HandInfo> list) {
        for (int i = 0; i < Configuration.OptimizationIterations; i++) {
            for (HandInfo hand : list) {
                HandDerivatives opt = new HandDerivatives(tree, hand);
                opt.run();
            }

            System.out.println("Iteration #" + i + " done w/:" + tree.logLikelihood(list));
            tree.updateDerivatives();
        }
    }
}