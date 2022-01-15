package com.github.bourbon.base.merger;

import com.github.bourbon.base.lang.Merger;
import com.github.bourbon.base.utils.ArrayUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 17:42
 */
public class ArrayMerger implements Merger<Object[]> {

    @Override
    public Object[] merge(Object[][] items) {
        return ArrayUtils.merge(items);
    }
}