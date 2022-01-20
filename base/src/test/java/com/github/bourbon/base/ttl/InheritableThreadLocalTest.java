package com.github.bourbon.base.ttl;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.TimeUnitUtils;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.base.utils.concurrent.ThreadPoolExecutorFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 12:50
 * @code {https://www.cnblogs.com/hama1993/p/10400265.html}
 */
public class InheritableThreadLocalTest {

    private final Logger logger = LoggerFactory.getLogger(InheritableThreadLocalTest.class);
    //    private final ThreadLocal<Integer> tl = new ThreadLocal<>();
    private final ThreadLocal<Integer> tl = new InheritableThreadLocal<>();
    private final ThreadLocal<Hello> tl2 = new InheritableThreadLocal<>();

    @Test
    public void test() {
        Assert.assertNotNull(tl);

        tl.set(1);
        logger.info("main方法内获取线程内数据为:" + tl.get());
        fc();

        new Thread(this::fc).start();

        TimeUnitUtils.sleepMilliSeconds(100L);
        fc();
    }

    @Test
    public void test1() {
        Assert.assertNotNull(tl2);

        Hello hello = new Hello();
        hello.name = ("init");
        tl2.set(hello);

        logger.info("main方法内获取线程内数据为:" + tl2.get().name);

        fc1();

        new Thread(() -> {
            tl2.get().name = "init2";
            fc1();
        }).start();

        TimeUnitUtils.sleepMilliSeconds(100L);

        fc1();
    }

    private ExecutorService executor = ThreadPoolExecutorFactory.newFixedThreadPool("test", 1, new NamedThreadFactory("InheritableThreadLocalTest"));

    @Test
    public void test2() {
        Assert.assertNotNull(tl);

        tl.set(1);

        logger.info(tl.get());

        executor.execute(() -> logger.info(tl.get()));

        executor.execute(() -> logger.info(tl.get()));

        logger.info(tl.get());
    }

    @Test
    public void test3() {
        Assert.assertNotNull(tl);

        logger.info(tl.get());

        executor.execute(() -> logger.info(tl.get()));

        tl.set(1);

        executor.execute(() -> logger.info(tl.get()));

        logger.info(tl.get());
    }

    private void fc() {
        logger.info("fc方法内获取线程内数据为:" + tl.get());
    }

    private void fc1() {
        logger.info("fc方法内获取线程内数据为:" + tl2.get().name);
    }

    private static class Hello {
        private String name;
    }
}