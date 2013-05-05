package se.hupoker.inference.tree;

import se.hupoker.common.Computation;
import se.hupoker.common.LowPriorityFactory;
import se.hupoker.inference.handinformation.HandInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Alexander Nyberg
 */
public class ThreadedOptimization implements TreeOptimizer {
    private ExecutorService executor = Executors.newFixedThreadPool(Computation.units(), new LowPriorityFactory());
    private final GameTree tree;

    public ThreadedOptimization(GameTree tree) {
        this.tree = tree;
    }

    /**
     * Optimize the tree concurrently.
     *
     * @param list
     */
    @Override
    public void optimize(Collection<HandInfo> list, final int numberOfIterations) {
        List<Callable<Object>> callables = new ArrayList<>();

        for (HandInfo hand : list) {
            HandDerivatives opt = new HandDerivatives(tree, hand);
            callables.add(Executors.callable(opt));
        }

        for (int i = 0; i < numberOfIterations; i++) {
            try {
                executor.invokeAll(callables);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Iteration #" + i + " done w/:" + tree.logLikelihood(list));
            tree.updateDerivatives();
        }

        executor.shutdown();
    }
}