package com.github.bourbon.base.logger;

import lombok.Builder;
import lombok.Data;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/25 11:04
 */
@Data
@Builder
public class LogParamInfo {
    private final String charset;
    private final String pattern;
    private final boolean immediateFlush;

    private final String logPath;
    private final String fileNamePrefix;
    private final boolean additive;
    private final int maxHistory;
    private final String maxFileSize;
    private final String totalSizeCap;
}