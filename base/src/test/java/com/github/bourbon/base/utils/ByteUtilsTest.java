package com.github.bourbon.base.utils;

import com.github.bourbon.base.constant.*;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/3 22:33
 */
public class ByteUtilsTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        short s = ShortConstants.MAX_VALUE;
        Assert.assertEquals((byte) s, (byte) (s & 255));
        logger.error(PrimitiveArrayUtils.toString(ByteUtils.shortToBytes(s)));
        Assert.assertEquals(s, ByteUtils.bytesToShort(ByteUtils.shortToBytes(s)));
        logger.error(ByteUtils.bytesToShort(ByteUtils.shortToBytes(s)));

        int i = IntConstants.MAX_VALUE;
        logger.error(PrimitiveArrayUtils.toString(ByteUtils.intToBytes(i)));
        Assert.assertEquals(i, ByteUtils.bytesToInt(ByteUtils.intToBytes(i)));
        logger.error(ByteUtils.bytesToInt(ByteUtils.intToBytes(i)));

        long l = LongConstants.MAX_VALUE;
        logger.error(PrimitiveArrayUtils.toString(ByteUtils.longToBytes(l)));
        Assert.assertEquals(l, ByteUtils.bytesToLong(ByteUtils.longToBytes(l)));
        logger.error(ByteUtils.bytesToLong(ByteUtils.longToBytes(l)));

        float f = FloatConstants.MAX_VALUE;
        logger.error(PrimitiveArrayUtils.toString(ByteUtils.floatToBytes(f)));
        Assert.assertTrue(f == ByteUtils.bytesToFloat(ByteUtils.floatToBytes(f)));
        logger.error(ByteUtils.bytesToFloat(ByteUtils.floatToBytes(f)));

        double d = DoubleConstants.MAX_VALUE;
        logger.error(PrimitiveArrayUtils.toString(ByteUtils.doubleToBytes(d)));
        Assert.assertTrue(d == ByteUtils.bytesToDouble(ByteUtils.doubleToBytes(d)));
        logger.error(ByteUtils.bytesToDouble(ByteUtils.doubleToBytes(d)));
    }
}