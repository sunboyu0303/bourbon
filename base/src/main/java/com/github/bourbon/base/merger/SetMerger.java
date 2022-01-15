package com.github.bourbon.base.merger;

import com.github.bourbon.base.lang.Merger;
import com.github.bourbon.base.utils.SetUtils;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 23:09
 */
public class SetMerger implements Merger<Set<Object>> {

    @Override
    public Set<Object> merge(Set<Object>[] items) {
        return SetUtils.merge(items);
    }
}