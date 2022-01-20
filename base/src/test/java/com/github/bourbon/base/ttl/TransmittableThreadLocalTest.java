package com.github.bourbon.base.ttl;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.ttl.threadpool.TtlExecutors;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.base.utils.concurrent.ThreadPoolExecutorFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/12 13:15
 * @code {https://www.cnblogs.com/hama1993/p/10409740.html}
 */
public class TransmittableThreadLocalTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ExecutorService executor = TtlExecutors.getTtlExecutorService(ThreadPoolExecutorFactory.newFixedThreadPool("test", 2, new NamedThreadFactory("TransmittableThreadLocalTest")));

    private final ThreadLocal<Integer> tl = new TransmittableThreadLocal<>();

    @Test
    public void test() {
        Assert.assertNotNull(tl);

        new Thread(() -> {

            tl.set(1);

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之前(1), 变量值=" + tl.get());
            });

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之前(1), 变量值=" + tl.get());
            });

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之前(1), 变量值=" + tl.get());
            });

            sleep();
            tl.set(2);

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之后(2), 变量值=" + tl.get());
            });

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之后(2), 变量值=" + tl.get());
            });

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之后(2), 变量值=" + tl.get());
            });

            logger.info("变量值=" + tl.get());

        }).start();

        new Thread(() -> {

            tl.set(3);

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之前(3), 变量值=" + tl.get());
            });

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之前(3), 变量值=" + tl.get());
            });

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之前(3), 变量值=" + tl.get());
            });

            sleep();
            tl.set(4);

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之后(4), 变量值=" + tl.get());
            });

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之后(4), 变量值=" + tl.get());
            });

            executor.execute(() -> {
                sleep();
                logger.info("本地变量改变之后(4), 变量值=" + tl.get());
            });

            logger.info("变量值=" + tl.get());

        }).start();


        sleep(10L);
    }

    @Test
    public void test1() {
        Assert.assertNotNull(tl);

        new Thread(() -> {

            tl.set(1);

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之前(1), 变量值=" + tl.get());
            }).start();

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之前(1), 变量值=" + tl.get());
            }).start();

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之前(1), 变量值=" + tl.get());
            }).start();

            sleep();
            tl.set(2);

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之后(2), 变量值=" + tl.get());
            }).start();

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之后(2), 变量值=" + tl.get());
            }).start();

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之后(2), 变量值=" + tl.get());
            }).start();

            logger.info("变量值=" + tl.get());
        }).start();

        new Thread(() -> {

            tl.set(3);

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之前(3), 变量值=" + tl.get());
            }).start();

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之前(3), 变量值=" + tl.get());
            }).start();

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之前(3), 变量值=" + tl.get());
            }).start();

            sleep();
            tl.set(4);

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之后(4), 变量值=" + tl.get());
            }).start();

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之后(4), 变量值=" + tl.get());
            }).start();

            new Thread(() -> {
                sleep();
                logger.info("本地变量改变之后(4), 变量值=" + tl.get());
            }).start();

            logger.info("变量值=" + tl.get());
        }).start();

        sleep(10L);
    }

    private void sleep() {
        sleep(1L);
    }

    private void sleep(long time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}