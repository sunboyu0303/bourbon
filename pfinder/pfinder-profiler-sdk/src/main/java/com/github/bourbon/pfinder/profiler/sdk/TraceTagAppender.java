package com.github.bourbon.pfinder.profiler.sdk;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 17:08
 */
@FunctionalInterface
public interface TraceTagAppender {

    /**
     * append a tag to trace
     *
     * @param key   tag key (max length: 50)
     * @param value tag value (max length: 200)
     */
    TraceTagAppender append(String key, String value);

    TraceTagAppender NOOP = new TraceTagAppender() {
        @Override
        public TraceTagAppender append(String key, String value) {
            return this;
        }
    };
}