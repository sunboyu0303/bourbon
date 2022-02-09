package com.github.bourbon.common.utils;

import com.alibaba.fastjson.JSON;
import com.github.bourbon.base.appender.builder.JsonStringBuilder;
import com.github.bourbon.base.lang.SystemClock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/25 15:40
 */
public class ToStringTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        SystemClock.currentTimeMillis();

        User user = new User("张三", 33);

        Assert.assertEquals(JSON.toJSONString(user), new JsonStringBuilder(true).appendBegin().append("age", user.age).append("name", user.name).appendEnd(false).toString());

        long startTime = SystemClock.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            JSON.toJSONString(user);
        }
        logger.info(SystemClock.currentTimeMillis() - startTime);

        startTime = SystemClock.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            new JsonStringBuilder(true).appendBegin().append("age", user.age).append("name", user.name).appendEnd(false);
        }
        logger.info(SystemClock.currentTimeMillis() - startTime);

        startTime = SystemClock.currentTimeMillis();
        final JsonStringBuilder builder = new JsonStringBuilder(true);
        for (int i = 0; i < 1000000; i++) {
            builder.appendBegin().append("age", user.age).append("name", user.name).appendEnd(false);
            builder.reset();
        }
        logger.info(SystemClock.currentTimeMillis() - startTime);
    }

    private static class User {
        private final String name;
        private final int age;

        private User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}