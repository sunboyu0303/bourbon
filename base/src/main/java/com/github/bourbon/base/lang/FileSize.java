package com.github.bourbon.base.lang;

import com.github.bourbon.base.constant.LongConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/28 14:55
 */
public final class FileSize {
    private static final Pattern FILE_SIZE_PATTERN = Pattern.compile("([0-9]+)\\s*(|kb|mb|gb)s?", 2);

    private final long size;

    private FileSize(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public static FileSize valueOf(String fileSizeStr) {
        Matcher matcher = FILE_SIZE_PATTERN.matcher(fileSizeStr);
        if (matcher.matches()) {
            String lenStr = matcher.group(1);
            String unitStr = matcher.group(2);
            long coefficient;
            if (unitStr.equalsIgnoreCase(Unit.B.caseValue)) {
                coefficient = Unit.B.value;
            } else if (unitStr.equalsIgnoreCase(Unit.KB.caseValue)) {
                coefficient = Unit.KB.value;
            } else if (unitStr.equalsIgnoreCase(Unit.MB.caseValue)) {
                coefficient = Unit.MB.value;
            } else {
                if (!unitStr.equalsIgnoreCase(Unit.GB.caseValue)) {
                    throw new IllegalStateException("Unexpected " + unitStr);
                }
                coefficient = Unit.GB.value;
            }
            return new FileSize(Long.parseLong(lenStr) * coefficient);
        }
        throw new IllegalArgumentException("String value [" + fileSizeStr + "] is not in the expected format.");
    }

    public String toString() {
        long inKB = size / Unit.KB.value;
        if (inKB == 0L) {
            return size + Unit.B.desc;
        }
        long inMB = size / Unit.MB.value;
        if (inMB == 0L) {
            return inKB + Unit.KB.desc;
        }
        long inGB = size / Unit.GB.value;
        return inGB == 0L ? inMB + Unit.MB.desc : inGB + Unit.GB.desc;
    }

    private enum Unit {
        B(LongConstants.B, " Bytes", ""),
        KB(LongConstants.KB, " KB", "kb"),
        MB(LongConstants.MB, " MB", "mb"),
        GB(LongConstants.GB, " GB", "gb");

        private long value;
        private String desc;
        private String caseValue;

        Unit(long value, String desc, String caseValue) {
            this.value = value;
            this.desc = desc;
            this.caseValue = caseValue;
        }
    }
}