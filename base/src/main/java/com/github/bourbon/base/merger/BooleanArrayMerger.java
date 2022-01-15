package com.github.bourbon.base.merger;

import com.github.bourbon.base.lang.Merger;
import com.github.bourbon.base.utils.PrimitiveArrayUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 21:36
 */
public class BooleanArrayMerger implements Merger<boolean[]> {

    @Override
    public boolean[] merge(boolean[][] items) {
        return PrimitiveArrayUtils.merge(items);
    }
}