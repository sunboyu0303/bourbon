package com.github.bourbon.springframework.aop.bean;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 15:57
 */
public class UserServiceInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = Clock.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            LOGGER.info("监控 - Begin By AOP");
            LOGGER.info("方法名称：" + invocation.getMethod());
            LOGGER.info("方法耗时：" + (Clock.currentTimeMillis() - start) + " ms");
            LOGGER.info("监控 - End\r\n");
        }
    }
}