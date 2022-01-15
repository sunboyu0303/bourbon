package com.github.bourbon.base.merger;

import com.github.bourbon.base.lang.Merger;
import com.github.bourbon.base.utils.PrimitiveArrayUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 22:09
 */
public class DoubleArrayMerger implements Merger<double[]> {

    @Override
    public double[] merge(double[][] items) {
        return PrimitiveArrayUtils.merge(items);
    }
}