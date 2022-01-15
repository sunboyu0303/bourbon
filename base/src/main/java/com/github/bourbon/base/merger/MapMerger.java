package com.github.bourbon.base.merger;

import com.github.bourbon.base.lang.Merger;
import com.github.bourbon.base.utils.MapUtils;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 23:11
 */
public class MapMerger implements Merger<Map<?, ?>> {

    @Override
    public Map<?, ?> merge(Map<?, ?>[] items) {
        return MapUtils.merge(items);
    }
}