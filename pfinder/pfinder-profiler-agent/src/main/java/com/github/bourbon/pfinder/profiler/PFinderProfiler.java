package com.github.bourbon.pfinder.profiler;

import com.github.bourbon.pfinder.profiler.service.AgentEnvService;
import com.github.bourbon.pfinder.profiler.service.ProfilerBootstrap;
import com.github.bourbon.pfinder.profiler.service.SpiServiceLoader;
import com.github.bourbon.pfinder.profiler.tracer.context.ContextManager;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 22:59
 */
public class PFinderProfiler {
    
    private static final AtomicBoolean booted = new AtomicBoolean(false);
    private static final AtomicBoolean destroyed = new AtomicBoolean(false);

    public static void boot(final String agentArgs, final Instrumentation instrumentation) {
        if (booted.compareAndSet(false, true)) {
            ProfilerBootstrap profilerBootstrap = new ProfilerBootstrap();

            profilerBootstrap.boot(new SpiServiceLoader().register(new AgentEnvService() {
                @Override
                public String agentArgs() {
                    return agentArgs;
                }

                @Override
                public Instrumentation instrumentation() {
                    return instrumentation;
                }
            }));

            ContextManager.initialize(profilerBootstrap);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (PFinderProfiler.destroyed.compareAndSet(false, true)) {
                    profilerBootstrap.shutdown();
                }
            }));
        }
    }
}