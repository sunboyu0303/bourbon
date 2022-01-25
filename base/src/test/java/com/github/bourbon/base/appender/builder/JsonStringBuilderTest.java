package com.github.bourbon.base.appender.builder;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 15:10
 */
public class JsonStringBuilderTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        JsonStringBuilder builder = new JsonStringBuilder(true);
        builder.appendBegin().append("name", "张三").append("age", 33).append("sex", null).appendEnd();
        logger.error(builder.toString());
    }
}