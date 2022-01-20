package com.github.bourbon.common.logger;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.LogParamInfo;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.FileTools;
import com.github.bourbon.base.utils.LocalDateTimeUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/26 17:58
 */
public class LoggerTest {

    @Test
    public void testLogger() {

        String logPath = "/export/home/tomcat/UMP/logs/";
        FileTools.createPathIfNecessary(logPath);

        String fileNamePrefix = logPath + LocalDateTimeUtils.localDateTimeNowFormat() + StringConstants.UNDERLINE + "123123" + StringConstants.UNDERLINE;

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

        Logger logger = LoggerFactory.getLogger("LoggerTest", info);
        Assert.assertNotNull(logger);
        logger.info("logger factory test!");
    }
}