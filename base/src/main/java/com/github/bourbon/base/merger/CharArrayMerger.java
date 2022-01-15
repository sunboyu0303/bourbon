package com.github.bourbon.base.merger;

import com.github.bourbon.base.lang.Merger;
import com.github.bourbon.base.utils.PrimitiveArrayUtils;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/21 22:08
 */
public class CharArrayMerger implements Merger<char[]> {

    @Override
    public char[] merge(char[][] items) {
        return PrimitiveArrayUtils.merge(items);
    }
}