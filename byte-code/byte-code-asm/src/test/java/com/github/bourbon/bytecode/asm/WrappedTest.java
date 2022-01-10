package com.github.bourbon.bytecode.asm;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.bytecode.asm.support.Wrapped;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 19:48
 */
public class WrappedTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() throws Exception {
        TestService test = new Wrapped<>(new TestService()).getObject();
        logger.error(test.getTime());
        logger.error(test.sum(1L, 2L));
        logger.error(test.sum(new Integer(3), new Integer(4)));
        logger.error(test.getUser("zhangsan"));
        test.saveUser(new User());
        logger.error(test.getMap("zhangsan", 5));
        logger.error(test.sum(6.0D, 7.0D, 8.0D));
        logger.error(test.sum(new int[]{1, 2, 3, 4, 5, 6}));
        logger.error(test.convert(new User[]{new User("zhangsan", 10), new User("lisi", 20), new User("wangwu", 30)}));

        test = new Wrapped<>(new TestService(), TestMonitor.class, "init", "destroy").getObject();
        logger.error(test.getTime());
    }
}