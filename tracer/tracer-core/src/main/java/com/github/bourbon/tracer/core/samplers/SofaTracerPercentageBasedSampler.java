package com.github.bourbon.tracer.core.samplers;

import com.github.bourbon.base.lang.Pair;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;
import com.github.bourbon.tracer.core.span.SofaTracerSpan;

import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/30 17:42
 */
public class SofaTracerPercentageBasedSampler implements Sampler {

    private static final String TYPE = "PercentageBasedSampler";

    private final AtomicLong counter = new AtomicLong(0);
    private final BitSet sampleDecisions;
    private final SamplerProperties configuration;

    public SofaTracerPercentageBasedSampler(SamplerProperties configuration) {
        this.sampleDecisions = randomBitSet((int) (configuration.getPercentage()));
        this.configuration = configuration;
    }

    @Override
    public SamplingStatus sample(SofaTracerSpan sofaTracerSpan) {
        SamplingStatus samplingStatus = new SamplingStatus();
        Pair<String, Object> typePair = Pair.of(SofaTracerConstants.SAMPLER_TYPE_TAG_KEY, TYPE);
        Pair<String, Object> paramPair = Pair.of(SofaTracerConstants.SAMPLER_PARAM_TAG_KEY, configuration.getPercentage());
        samplingStatus.setTags(MapUtils.unmodifiableMap(MapUtils.ofPair(typePair, paramPair)));
        if (this.configuration.getPercentage() == 0) {
            return samplingStatus.setSampled(false);
        }
        if (this.configuration.getPercentage() == 100) {
            return samplingStatus.setSampled(true);
        }
        return samplingStatus.setSampled(this.sampleDecisions.get((int) (this.counter.getAndIncrement() % 100)));
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void close() {
        //do nothing
    }

    private BitSet randomBitSet(int cardinality) {
        int size = 100;
        BitSet result = new BitSet(size);
        int[] chosen = new int[cardinality];
        int i;
        for (i = 0; i < cardinality; ++i) {
            chosen[i] = i;
            result.set(i);
        }
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        for (; i < size; ++i) {
            int j = threadLocalRandom.nextInt(i + 1);
            if (j < cardinality) {
                result.clear(chosen[j]);
                result.set(i);
                chosen[j] = i;
            }
        }
        return result;
    }
}