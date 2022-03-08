package com.github.bourbon.pfinder.profiler.sdk.trace;

import com.github.bourbon.pfinder.profiler.sdk.PfinderContext;

import java.util.concurrent.Executor;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 17:08
 */
public class TracingExecutor implements Executor {

    private final Executor origin;

    public TracingExecutor(Executor origin) {
        this.origin = origin;
    }

    @Override
    public void execute(Runnable command) {
        origin.execute(PfinderContext.asyncWrapper(command));
    }
}