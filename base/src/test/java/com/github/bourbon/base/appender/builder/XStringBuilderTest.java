package com.github.bourbon.base.appender.builder;

import com.github.bourbon.base.utils.MapUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/26 10:21
 */
public class XStringBuilderTest {

    @Test
    public void test() {
        Assert.assertEquals("test=test\r\n", new XStringBuilder().appendEnd(MapUtils.of("test", "test")).toString());

        XStringBuilder builder = new XStringBuilder(128);
        builder.append(1L, ",");
        builder.append(1L, ',');
        builder.append("test1", ",");

        Map<String, String> map = new HashMap<>();
        builder.append(1);
        builder.append(',');
        builder.append(map);
        builder.appendEnd(1);
        builder.appendEnd(',');
        builder.appendEnd(1l);
        builder.appendEscape("test2");
        XStringBuilder append = builder.appendEscapeRaw("test3");

        Assert.assertEquals("1,1,test1,1,,,,1\r\n,\r\n1\r\ntest2,test3", append.toString());
    }
}