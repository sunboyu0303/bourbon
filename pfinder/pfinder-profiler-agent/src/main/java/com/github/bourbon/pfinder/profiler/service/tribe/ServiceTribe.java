package com.github.bourbon.pfinder.profiler.service.tribe;

import java.util.Collection;
import java.util.Collections;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 15:01
 */
public interface ServiceTribe {

    ServiceTribe NOOP = new ServiceTribe() {
        @Override
        public Object chief() {
            return null;
        }

        @Override
        public Collection<Object> all() {
            return Collections.emptyList();
        }

        @Override
        public boolean add(Object member) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object member) {
            throw new UnsupportedOperationException();
        }
    };

    /**
     * 首节点
     */
    Object chief();

    /**
     * 全部节点
     */
    Collection<Object> all();

    /**
     * 添加节点
     */
    boolean add(Object var1);

    /**
     * 删除节点
     */
    boolean remove(Object var1);
}