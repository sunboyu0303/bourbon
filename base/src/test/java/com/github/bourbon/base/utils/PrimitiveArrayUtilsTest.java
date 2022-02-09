package com.github.bourbon.base.utils;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/22 14:59
 */
public class PrimitiveArrayUtilsTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        int[] src = {1, 2, 3, 4, 5};
        int[] dest = new int[src.length + 6];
        System.arraycopy(src, 0, dest, 0, src.length);
        Assert.assertEquals(PrimitiveArrayUtils.toString(dest), PrimitiveArrayUtils.toString(PrimitiveArrayUtils.expand(src, 6)));

        boolean[] booleans = {true, false, true, false, false, true, false, false, false};
        Boolean[] booleanWrappers = PrimitiveArrayUtils.wrap(booleans);
        logger.info(ListUtils.newArrayList(booleanWrappers));
    }
}