package com.github.bourbon.base.tracer;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.ObjectUtils;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/26 11:17
 */
public final class TracerIdAdapter {

    private static final Logger logger = LoggerFactory.getLogger(TracerIdAdapter.class);
    private static final TracerIdAdapter INSTANCE = new TracerIdAdapter();
    private TracerIdRetriever tracerIdRetriever;

    private TracerIdAdapter() {
        Iterator<TracerIdRetriever> tracerIdAdapterIterator = ServiceLoader.load(TracerIdRetriever.class).iterator();
        if (tracerIdAdapterIterator.hasNext()) {
            this.tracerIdRetriever = tracerIdAdapterIterator.next();
            if (logger.isInfoEnabled()) {
                logger.info("TracerIdConverter use tracerIdAdapter '{}'", this.tracerIdRetriever.getClass().getName());
            }
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn("TracerIdConverter can not find any tracerIdAdapter");
            }
        }
    }

    public static TracerIdAdapter getInstance() {
        return INSTANCE;
    }

    public String traceIdSafari(Thread t) {
        return ObjectUtils.defaultIfNull(tracerIdRetriever, r -> {
            try {
                return r.getTracerId(t);
            } catch (Exception e) {
                return null;
            }
        });
    }
}