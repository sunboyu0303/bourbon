package com.github.bourbon.ump.support;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.extension.annotation.Inject;
import com.github.bourbon.base.extension.support.AbstractLifecycle;
import com.github.bourbon.base.logger.LogParamInfo;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.FileTools;
import com.github.bourbon.base.utils.LocalDateTimeUtils;
import com.github.bourbon.ump.RuntimeEnvironmentService;
import com.github.bourbon.ump.UmpLoggerFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 16:58
 */
public class SimpleUmpLoggerFactory extends AbstractLifecycle implements UmpLoggerFactory {

    private Logger tpLogger;
    private Logger jvmLogger;
    private Logger aliveLogger;

    @Inject
    private RuntimeEnvironmentService runtimeEnvironmentService;

    @Override
    protected void doInitialize() {

        String logPath = "/export/home/tomcat/UMP/logs/";
        FileTools.createPathIfNecessary(logPath);

        String fileNamePrefix = logPath + LocalDateTimeUtils.localDateTimeNowFormat() + StringConstants.UNDERLINE + runtimeEnvironmentService.getPid() + StringConstants.UNDERLINE;

        LogParamInfo info = LogParamInfo.builder()
                .charset("UTF-8")
                .pattern("%msg%n")
                .immediateFlush(true)
                .logPath(logPath)
                .fileNamePrefix(fileNamePrefix)
                .additive(false)
                .maxHistory(3)
                .maxFileSize("50MB")
                .totalSizeCap("200MB")
                .build();

        tpLogger = LoggerFactory.getLogger("tp.log", info);
        jvmLogger = LoggerFactory.getLogger("alive.log", info);
        aliveLogger = LoggerFactory.getLogger("jvm.log", info);
    }

    @Override
    public Logger tpLogger() {
        return tpLogger;
    }

    @Override
    public Logger jvmLogger() {
        return jvmLogger;
    }

    @Override
    public Logger aliveLogger() {
        return aliveLogger;
    }
}