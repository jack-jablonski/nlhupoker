package se.hupoker.inference;

import se.hupoker.common.Computation;
import se.hupoker.common.LowPriorityFactory;
import se.hupoker.inference.handinformation.HandInfo;
import se.hupoker.handhistory.HeadsUp;
import se.hupoker.inference.tree.Extraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 * @author Alexander Nyberg
 */
public class ThreadedExtraction implements Extraction {
    private ExecutorService executor = Executors.newFixedThreadPool(Computation.units(), new LowPriorityFactory());

    private static class HandExtract implements Callable<HandInfo> {
        private final HeadsUp headsup;

        public HandExtract(HeadsUp hu) {
            headsup = hu;
        }

        @Override
        public HandInfo call() throws Exception {
            return HandInfo.factory(headsup);
        }
    }

    /**
     * @param list
     */
    public Collection<HandInfo> extract(Collection<HeadsUp> list) {
        final List<HandInfo> extracted = new ArrayList<>();
        List<HandExtract> callables = new ArrayList<>();

        for (HeadsUp hs : list) {
            callables.add(new HandExtract(hs));
        }

        List<Future<HandInfo>> futures;
        try {
            futures = executor.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return extracted;
        }

        for (Future<HandInfo> future : futures) {
            try {
                extracted.add(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        return extracted;
    }
}