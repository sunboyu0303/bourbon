package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/22 15:26
 */
public class ArrayUtilsTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        Integer[] src = {1, 2, 3, 4, 5};
        Integer[] dest = new Integer[src.length + 6];
        System.arraycopy(src, 0, dest, 0, src.length);
        Assert.assertEquals(ArrayUtils.toString(dest), ArrayUtils.toString(ArrayUtils.expand(src, 6)));

        Object[] objects = null;
        Assert.assertEquals(
                ArrayUtils.toString(objects, CharConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES),
                ArrayUtils.toString(objects, StringConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES)
        );
        objects = ArrayUtils.of();
        Assert.assertEquals(
                ArrayUtils.toString(objects, CharConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES),
                ArrayUtils.toString(objects, StringConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES)
        );
        objects = ArrayUtils.of(null);
        Assert.assertEquals(
                ArrayUtils.toString(objects, CharConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES),
                ArrayUtils.toString(objects, StringConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES)
        );
        objects = ArrayUtils.of(1);
        Assert.assertEquals(
                ArrayUtils.toString(objects, CharConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES),
                ArrayUtils.toString(objects, StringConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES)
        );
        objects = ArrayUtils.of(1, 2, 3, 4, 5);
        Assert.assertEquals(
                ArrayUtils.toString(objects, CharConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES),
                ArrayUtils.toString(objects, StringConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES)
        );
        objects = ArrayUtils.of(1, null, 3, null, 5);
        Assert.assertEquals(
                ArrayUtils.toString(objects, CharConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES),
                ArrayUtils.toString(objects, StringConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES)
        );
        objects = ArrayUtils.of(null, null, null, null, 5);
        Assert.assertEquals(
                ArrayUtils.toString(objects, CharConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES),
                ArrayUtils.toString(objects, StringConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES)
        );
        objects = ArrayUtils.of(null, null, null, null, null);
        Assert.assertEquals(
                ArrayUtils.toString(objects, CharConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES),
                ArrayUtils.toString(objects, StringConstants.COMMA, StringConstants.LEFT_BRACES, StringConstants.RIGHT_BRACES)
        );
    }
}