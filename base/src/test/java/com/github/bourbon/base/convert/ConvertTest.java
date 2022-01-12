package com.github.bourbon.base.convert;

import com.github.bourbon.base.convert.impl.ObjectToByteConverter;
import com.github.bourbon.base.convert.impl.ObjectToNumberConverter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 10:15
 */
public class ConvertTest {

    @Test
    public void testObjectConvert() {
        ObjectConverter<Number> numberConverter = new ObjectToNumberConverter(Byte.class);
        ObjectConverter<Number> byteConverter = new ObjectToByteConverter();
        Assert.assertTrue(numberConverter.convert("1", null) instanceof Byte);
        Assert.assertTrue(byteConverter.convert("1", null) instanceof Byte);
        Assert.assertEquals(Convert.toString(1), "1");
    }
}