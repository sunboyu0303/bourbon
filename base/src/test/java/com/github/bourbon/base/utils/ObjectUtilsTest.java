package com.github.bourbon.base.utils;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/22 15:46
 */
public class ObjectUtilsTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        Integer a = new Integer(1);
        Assert.assertEquals(
                ObjectUtils.defaultIfNull(a, new Integer(1290)),
                ObjectUtils.defaultSupplierIfNull(a, () -> new Integer(1290))
        );

        Assert.assertEquals(a instanceof Object, Object.class.isAssignableFrom(a.getClass()));

        Double d = 3.14D;
        Assert.assertEquals(d instanceof Number, Number.class.isAssignableFrom(d.getClass()));
    }

    private class Integer {

        private int value;

        private Integer(int value) {
            logger.error(value);
            this.value = value;
        }
    }
}