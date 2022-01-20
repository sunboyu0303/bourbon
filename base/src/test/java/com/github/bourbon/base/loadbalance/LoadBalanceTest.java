package com.github.bourbon.base.loadbalance;

import com.github.bourbon.base.extension.model.ScopeModelUtils;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.MapUtils;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/3 15:28
 */
public class LoadBalanceTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        List<Invoker<String>> invokers = ListUtils.newLinkedList(Invoker.of("db_0"), Invoker.of("db_1"), Invoker.of("db_2"), Invoker.of("db_3"));
        ILoadBalance lb = ScopeModelUtils.getExtensionLoader(ILoadBalance.class).getDefaultExtension();
        Map<String, AtomicLong> map = MapUtils.newHashMap();
        for (int i = 0; i < 100; i++) {
            map.computeIfAbsent(lb.select(invokers, null).get(), o -> new AtomicLong()).incrementAndGet();
        }
        logger.info(map);

        lb = ScopeModelUtils.getExtensionLoader(ILoadBalance.class).getExtension("round-robin");
        map = MapUtils.newHashMap();
        for (int i = 0; i < 100; i++) {
            map.computeIfAbsent(lb.select(invokers, new Invocation("key", "1")).get(), o -> new AtomicLong()).incrementAndGet();
        }
        logger.info(map);

        lb = ScopeModelUtils.getExtensionLoader(ILoadBalance.class).getExtension("consistent-hash");
        map = MapUtils.newHashMap();
        for (int i = 0; i < 100; i++) {
            map.computeIfAbsent(lb.select(invokers, new Invocation("key", i % 10)).get(), o -> new AtomicLong()).incrementAndGet();
        }
        logger.info(map);

        invokers = ListUtils.newLinkedList(Invoker.of("db_0", 1), Invoker.of("db_1", 2), Invoker.of("db_2", 3), Invoker.of("db_3", 4));
        lb = ScopeModelUtils.getExtensionLoader(ILoadBalance.class).getDefaultExtension();
        map = MapUtils.newHashMap();
        for (int i = 0; i < 100; i++) {
            map.computeIfAbsent(lb.select(invokers, null).get(), o -> new AtomicLong()).incrementAndGet();
        }
        logger.info(map);

        lb = ScopeModelUtils.getExtensionLoader(ILoadBalance.class).getExtension("round-robin");
        map = MapUtils.newHashMap();
        for (int i = 0; i < 100; i++) {
            map.computeIfAbsent(lb.select(invokers, new Invocation("key", "1")).get(), o -> new AtomicLong()).incrementAndGet();
        }
        logger.info(map);

        lb = ScopeModelUtils.getExtensionLoader(ILoadBalance.class).getExtension("consistent-hash");
        map = MapUtils.newHashMap();
        for (int i = 0; i < 100; i++) {
            map.computeIfAbsent(lb.select(invokers, new Invocation("key", i % 10 + "")).get(), o -> new AtomicLong()).incrementAndGet();
        }
        logger.info(map);
    }
}