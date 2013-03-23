package se.hupoker.common;

import java.util.concurrent.ThreadFactory;

/**
 * Low priority thread.
 *
 * @author Alexander Nyberg
 */
public class LowPriorityFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
    }
}