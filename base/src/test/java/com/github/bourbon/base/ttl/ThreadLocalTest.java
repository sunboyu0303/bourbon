package com.github.bourbon.base.ttl;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.TimeUnitUtils;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 11:53
 * @code {https://www.cnblogs.com/hama1993/p/10382523.html}
 */
public class ThreadLocalTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ThreadLocal<Integer> tl = new ThreadLocal<>();

    @Test
    public void test() {
        tl.set(1);
        logger.info("main方法内获取线程内数据为: {}", tl.get());
        fc();
        new Thread(this::fc).start();
    }

    @Test
    public void test1() {
        tl.set(1);
        logger.info("main方法内获取线程内数据为: {}", tl.get());
        fc();

        new Thread(() -> {
            tl.set(2);
            fc();
        }).start();

        TimeUnitUtils.sleepMilliSeconds(100L);
        fc();
    }

    private void fc() {
        logger.info("fc方法内获取线程内数据为: {}", tl.get());
    }
}