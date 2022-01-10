package com.github.bourbon.bytecode.bytebuddy;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.bytecode.bytebuddy.support.Wrapped;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 23:21
 */
public class WrappedTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void useWrap() throws Exception {
        BizService proxy = new Wrapped<>(BizService.class).getObject();

        UserInfo userInfo = new UserInfo("zhangsan", 30, "北京市");
        proxy.create(userInfo);

        userInfo.setAge(31);
        proxy.update(userInfo);

        logger.info(proxy.read("zhangsan"));
        logger.info(proxy.delete("zhangsan"));

        Assert.assertNull(proxy.read("zhangsan"));

        proxy = new Wrapped<>(BizService.class, TestMonitor.class).getObject();

        userInfo = new UserInfo("zhangsan", 30, "北京市");
        proxy.create(userInfo);

        userInfo.setAge(31);
        proxy.update(userInfo);

        logger.info(proxy.read("zhangsan"));
        logger.info(proxy.delete("zhangsan"));
    }
}