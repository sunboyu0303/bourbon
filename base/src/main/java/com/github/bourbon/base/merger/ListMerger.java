package com.github.bourbon.base.merger;

import com.github.bourbon.base.lang.Merger;
import com.github.bourbon.base.utils.ListUtils;

import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 22:58
 */
public class ListMerger implements Merger<List<Object>> {

    @Override
    public List<Object> merge(List<Object>[] items) {
        return ListUtils.merge(items);
    }
}