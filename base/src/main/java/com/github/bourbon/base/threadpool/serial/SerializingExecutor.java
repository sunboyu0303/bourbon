package com.github.bourbon.base.threadpool.serial;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/22 16:36
 */
public final class SerializingExecutor implements Executor, Runnable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AtomicBoolean atomicBoolean = new AtomicBoolean();

    private final Queue<Runnable> runQueue = new ConcurrentLinkedQueue<>();

    private final Executor executor;

    public SerializingExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void run() {
        Runnable r;
        try {
            while ((r = runQueue.poll()) != null) {
                try {
                    r.run();
                } catch (Exception e) {
                    log.error(e);
                }
            }
        } finally {
            atomicBoolean.set(false);
        }
        if (!runQueue.isEmpty()) {
            // we didn't enqueue anything but someone else did.
            schedule(null);
        }
    }

    @Override
    public void execute(Runnable command) {
        runQueue.add(command);
        schedule(command);
    }

    private void schedule(Runnable r) {
        if (atomicBoolean.compareAndSet(false, true)) {
            boolean success = false;
            try {
                executor.execute(this);
                success = true;
            } finally {
                // It is possible that at this point that there are still tasks in the queue,
                // it would be nice to keep trying but the error may not be recoverable.
                // So we update our state and propagate so that if our caller deems it recoverable we won't be stuck.
                if (!success) {
                    if (r != null) {
                        // This case can only be reached if 'this' was not currently running, and we failed to reschedule.
                        // The item should still be in the queue for removal.
                        // ConcurrentLinkedQueue claims that null elements are not allowed, but seems to not throw if the item to remove is null.
                        // If removable is present in the queue twice, the wrong one may be removed.
                        // It doesn't seem possible for this case to exist today.
                        // This is important to run in case of RejectedExectuionException,
                        // so that future calls to execute don't succeed and accidentally run a previous runnable.
                        runQueue.remove(r);
                    }
                    atomicBoolean.set(false);
                }
            }
        }
    }
}