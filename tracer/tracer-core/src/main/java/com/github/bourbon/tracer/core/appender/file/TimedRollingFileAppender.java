package com.github.bourbon.tracer.core.appender.file;

import com.github.bourbon.base.appender.config.LogReserveConfig;
import com.github.bourbon.base.code.LogCode2Description;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.utils.*;
import com.github.bourbon.tracer.core.appender.self.SelfLog;
import com.github.bourbon.tracer.core.appender.self.TracerDaemon;
import com.github.bourbon.tracer.core.constants.SofaTracerConstants;
import com.github.bourbon.tracer.core.utils.TracerUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.github.bourbon.base.utils.RollingCalendar.RollingCalendarType.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/5 11:49
 */
public class TimedRollingFileAppender extends AbstractRollingFileAppender {

    public static final String DAILY_ROLLING_PATTERN = "'.'yyyy-MM-dd";
    public static final String HOURLY_ROLLING_PATTERN = "'.'yyyy-MM-dd_HH";
    public static final List<RollingCalendar.RollingCalendarType> TYPE_LIST = ListUtils.newArrayList(SECOND, MINUTE, HOUR_OF_DAY, DAY_OF_MONTH, WEEK_OF_YEAR, MONTH);

    private String scheduledFilename;

    private long nextCheck = Clock.currentTimeMillis() - 1;

    private final String datePattern;

    private final Date now = new Date();

    private final RollingCalendar rc;

    private final LogReserveConfig logReserveConfig;

    public TimedRollingFileAppender(String file, boolean append) {
        this(file, DEFAULT_BUFFER_SIZE, append);
    }

    public TimedRollingFileAppender(String file, int bufferSize, boolean append) {
        this(file, bufferSize, append, null);
    }

    public TimedRollingFileAppender(String file, String datePattern) {
        this(file, datePattern, null);
    }

    public TimedRollingFileAppender(String file, String datePattern, String logReserveConfigString) {
        this(file, DEFAULT_BUFFER_SIZE, true, datePattern, logReserveConfigString);
    }

    public TimedRollingFileAppender(String file, int bufferSize, boolean append, String datePatternParam) {
        this(file, bufferSize, append, datePatternParam, null);
    }

    public TimedRollingFileAppender(String file, int bufferSize, boolean append, String datePatternParam, String logReserveConfigString) {
        super(file, bufferSize, append);
        this.datePattern = BooleanUtils.defaultIfPredicate(datePatternParam, CharSequenceUtils::isNotBlank, p -> p, DAILY_ROLLING_PATTERN);
        this.rc = new RollingCalendar().setType(computeCheckPeriod());
        this.scheduledFilename = fileName + new SimpleDateFormat(datePattern).format(new Date(logFile.lastModified()));
        this.logReserveConfig = TracerUtils.parseLogReserveConfig(logReserveConfigString);

        TracerDaemon.watch(this);
    }

    @Override
    public boolean shouldRollOverNow() {
        long n = Clock.currentTimeMillis();
        if (n >= nextCheck) {
            now.setTime(n);
            nextCheck = rc.getNextCheckMillis(now);
            return true;
        }
        return false;
    }

    @Override
    public void cleanup() {
        try {
            File parentDirectory = logFile.getParentFile();
            if (parentDirectory == null || !parentDirectory.isDirectory()) {
                return;
            }

            final String baseName = logFile.getName();
            if (CharSequenceUtils.isBlank(baseName)) {
                return;
            }

            File[] logFiles = parentDirectory.listFiles((dir, name) -> CharSequenceUtils.isNotBlank(name) && name.startsWith(baseName));
            if (ArrayUtils.isEmpty(logFiles)) {
                return;
            }

            for (File logFile : logFiles) {
                String logFileName = logFile.getName();
                int lastDot = logFileName.lastIndexOf(StringConstants.DOT);
                if (lastDot < 0) {
                    continue;
                }
                String logTime = logFileName.substring(lastDot);
                if (StringConstants.LOG_FILE_SUFFIX.equalsIgnoreCase(logTime)) {
                    continue;
                }

                SimpleDateFormat dailyRollingSdf = new SimpleDateFormat(DAILY_ROLLING_PATTERN);
                SimpleDateFormat hourlyRollingSdf = new SimpleDateFormat(HOURLY_ROLLING_PATTERN);

                Date date = null;
                try {
                    date = hourlyRollingSdf.parse(logTime);
                } catch (ParseException e) {
                    try {
                        date = dailyRollingSdf.parse(logTime);
                    } catch (ParseException pe) {
                        SelfLog.error(String.format(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00004"), logFileName, pe.getMessage()));
                    }
                }

                if (date == null) {
                    continue;
                }

                Calendar now = Calendar.getInstance();
                now.add(Calendar.DATE, 0 - logReserveConfig.getDay());
                if (logReserveConfig.getHour() > 0) {
                    now.add(Calendar.HOUR_OF_DAY, 0 - logReserveConfig.getHour());
                } else {
                    now.set(Calendar.HOUR_OF_DAY, 0);
                }
                now.set(Calendar.MINUTE, 0);
                now.set(Calendar.SECOND, 0);
                now.set(Calendar.MILLISECOND, 0);

                Calendar compareCal = Calendar.getInstance();
                compareCal.clear();
                compareCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
                compareCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
                compareCal.set(Calendar.DATE, now.get(Calendar.DATE));
                compareCal.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));

                Calendar logCal = Calendar.getInstance();
                logCal.setTime(date);

                if (!logCal.before(compareCal)) {
                    continue;
                }

                Files.deleteIfExists(logFile.toPath());
                if (Files.notExists(logFile.toPath())) {
                    SelfLog.info("Deleted log file: " + logFileName);
                } else {
                    SelfLog.error(String.format(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00005"), logFileName));
                }
            }
        } catch (Throwable e) {
            SelfLog.error(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00006"), e);
        }
    }

    @Override
    public void rollOver() {
        String datedFilename = fileName + new SimpleDateFormat(datePattern).format(now);
        // It is too early to roll over because we are still within the bounds of the current interval. Rollover will occur once the next interval is reached.
        if (scheduledFilename.equals(datedFilename)) {
            return;
        }

        try {
            bos.close();
        } catch (IOException e) {
            SelfLog.error(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00008"), e);
        }

        File target = new File(scheduledFilename);
        try {
            Files.deleteIfExists(target.toPath());
        } catch (IOException e) {
            // ignore
        }

        boolean result = logFile.renameTo(target);
        if (result) {
            SelfLog.info(fileName + StringConstants.SPACE_HYPHEN_GREATER_THAN_SPACE + scheduledFilename);
        } else {
            SelfLog.error(String.format(LogCode2Description.convert(SofaTracerConstants.SPACE_ID, "01-00009"), fileName, scheduledFilename));
        }

        setFile(false);
        scheduledFilename = datedFilename;
    }

    private RollingCalendar.RollingCalendarType computeCheckPeriod() {
        RollingCalendar rollingCalendar = new RollingCalendar(TracerUtils.getGMTDefaultTimeZone(), Locale.getDefault());
        Date epoch = new Date(0);
        for (RollingCalendar.RollingCalendarType type : TYPE_LIST) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
            simpleDateFormat.setTimeZone(TracerUtils.getGMTDefaultTimeZone());
            String r0 = simpleDateFormat.format(epoch);
            rollingCalendar.setType(type);
            String r1 = simpleDateFormat.format(new Date(rollingCalendar.getNextCheckMillis(epoch)));
            if (!r0.equals(r1)) {
                return type;
            }
        }
        return TROUBLE;
    }
}