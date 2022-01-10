package com.github.bourbon.bytecode.javassist.support;

import com.github.bourbon.base.constant.IntConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.bytecode.core.domain.MethodDescription;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 23:37
 */
public final class DefaultMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMonitor.class);
    private static final AtomicInteger index = new AtomicInteger(0);
    private static final AtomicReferenceArray<MethodDescription> methodTagArr = new AtomicReferenceArray<>(IntConstants.KB32);

    public static int generateMethodId(String clazzName, String methodName, List<String> parameterNameList, List<String> parameterTypeList, String returnType) {
        int methodId = index.getAndIncrement();
        if (methodId > IntConstants.KB32) {
            return -1;
        }
        methodTagArr.set(methodId, new MethodDescription().setFullClassName(clazzName).setMethodName(methodName)
                .setParameterNameList(parameterNameList).setParameterTypeList(parameterTypeList).setReturnType(returnType));
        return methodId;
    }

    public static void point(final int methodId, final long startTime) {
        MethodDescription method = methodTagArr.get(methodId);
        LOGGER.info("监控 - Begin");
        LOGGER.info("方法：{}", method.getFullClassName() + StringConstants.DOT + method.getMethodName());
        LOGGER.info("入参：{}, 入参[类型]：{}", method.getParameterNameList(), method.getParameterTypeList());
        LOGGER.info("出参：{}", method.getReturnType());
        LOGGER.info("耗时：{} (ms)", Clock.currentTimeMillis() - startTime);
        LOGGER.info("监控 - End");
    }

    public static void point(final int methodId, Throwable t) {
        MethodDescription method = methodTagArr.get(methodId);
        LOGGER.info("监控 - Begin");
        LOGGER.info("方法：{}", method.getFullClassName() + StringConstants.DOT + method.getMethodName());
        LOGGER.info("异常：{}", t.getMessage());
        LOGGER.info("监控 - End");
    }

    private DefaultMonitor() {
    }
}