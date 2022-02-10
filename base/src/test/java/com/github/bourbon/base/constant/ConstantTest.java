package com.github.bourbon.base.constant;

import com.github.bourbon.base.utils.PrimitiveArrayUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/27 15:02
 */
public class ConstantTest {

    @Test
    public void testStringConstants() {
        Assert.assertEquals("\\\\", StringConstants.DOUBLE_BACKSLASH);
        Assert.assertEquals("'", StringConstants.SINGLE_QUOTE);
        Assert.assertEquals("' ", StringConstants.SINGLE_QUOTE_SPACE);
        Assert.assertEquals(" '", StringConstants.SPACE_SINGLE_QUOTE);
        Assert.assertEquals(", ", StringConstants.COMMA_SPACE);
        Assert.assertEquals("->", StringConstants.HYPHEN_GREATER_THAN);
        Assert.assertEquals(" -> ", StringConstants.SPACE_HYPHEN_GREATER_THAN_SPACE);
        Assert.assertEquals("'{", StringConstants.SINGLE_QUOTE_LEFT_BRACES);
        Assert.assertEquals("}'", StringConstants.RIGHT_BRACES_SINGLE_QUOTE);
        Assert.assertEquals("[0]", StringConstants.LEFT_BRACKETS_ZERO_RIGHT_BRACKETS);
        Assert.assertEquals("true", BooleanConstants.TRUE.toString());
    }

    @Test
    public void testCharConstants() {
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        Assert.assertEquals(PrimitiveArrayUtils.toString(chars), PrimitiveArrayUtils.toString(CharConstants.BASE16));
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        Assert.assertEquals(s, StringConstants.C64);
        Assert.assertEquals(PrimitiveArrayUtils.toString(s.toCharArray()), PrimitiveArrayUtils.toString(CharConstants.BASE64));
    }
}