package com.github.bourbon.base.utils;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/28 10:47
 */
public class BooleanUtilsTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        logger.info(BooleanUtils.defaultIfAssignableFrom(3, String.class, Object::toString, "error"));
        logger.info(BooleanUtils.defaultSupplierIfAssignableFrom(3, String.class, Object::toString, () -> "error"));

        Assert.assertEquals(
                BooleanUtils.defaultIfAssignableFrom(3, String.class, Object::toString, "error"),
                BooleanUtils.defaultSupplierIfAssignableFrom(3, String.class, Object::toString, () -> "error")
        );

        logger.info(BooleanUtils.defaultIfAssignableFrom(3, Integer.class, Object::toString, "error"));
        logger.info(BooleanUtils.defaultSupplierIfAssignableFrom(3, Integer.class, Object::toString, () -> "error"));

        Assert.assertEquals(
                BooleanUtils.defaultIfAssignableFrom(3, Integer.class, Object::toString, "error"),
                BooleanUtils.defaultSupplierIfAssignableFrom(3, Integer.class, Object::toString, () -> "error")
        );

        logger.info(BooleanUtils.defaultIfAssignableFrom(3, int.class, Object::toString, "error"));
        logger.info(BooleanUtils.defaultSupplierIfAssignableFrom(3, int.class, Object::toString, () -> "error"));

        Iterable<String> iterable = ListUtils.newArrayList("1", "2", "3", "4");
        logger.info(BooleanUtils.defaultSupplierIfAssignableFrom(
                iterable, Collection.class, Collection.class::cast, () -> ListUtils.newArrayList(iterable)
        ));

        String[] array = {"1", "2", "3", "4"};
        logger.info(BooleanUtils.defaultSupplierIfAssignableFrom(
                array, Collection.class, Collection.class::cast, () -> ListUtils.newArrayList(array)
        ));
    }
}