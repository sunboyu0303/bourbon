package com.github.bourbon.base.cache.impl;

import com.github.bourbon.base.cache.GlobalPruneTimer;
import com.github.bourbon.base.utils.MapUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/18 18:50
 */
public class TimedCache<K, V> extends AbstractCache<K, V> {
    private static final long serialVersionUID = -3944475587193994865L;
    private ScheduledFuture<?> pruneJobFuture;

    public TimedCache(long timeout) {
        this(timeout, MapUtils.newHashMap());
    }

    public TimedCache(long timeout, Map<K, CacheObj<K, V>> map) {
        this.timeout = timeout;
        this.cacheMap = map;
    }

    @Override
    protected int pruneCache() {
        int count = 0;
        Iterator<CacheObj<K, V>> values = cacheMap.values().iterator();
        while (values.hasNext()) {
            CacheObj<K, V> co = values.next();
            if (co.isExpired()) {
                values.remove();
                onRemove(co.getKey(), co.getValue());
                ++count;
            }
        }
        return count;
    }

    public void schedulePrune(long delay) {
        pruneJobFuture = GlobalPruneTimer.INSTANCE.schedule(this::prune, delay);
    }

    public void cancelPruneSchedule() {
        if (null != pruneJobFuture) {
            pruneJobFuture.cancel(true);
        }
    }
}