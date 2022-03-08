package com.github.bourbon.pfinder.profiler.sdk.metric;

import java.util.Collection;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/4 23:55
 */
public interface BasicBuilder<B extends BasicBuilder<B>> {

    B tags(MetricTag... tags);

    B tags(Collection<? extends MetricTag> tags);
}