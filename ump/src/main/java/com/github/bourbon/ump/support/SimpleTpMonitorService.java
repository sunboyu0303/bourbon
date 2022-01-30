package com.github.bourbon.ump.support;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.extension.annotation.Inject;
import com.github.bourbon.base.extension.support.AbstractLifecycle;
import com.github.bourbon.base.lang.Pair;
import com.github.bourbon.base.lang.SystemClock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.system.AppInfo;
import com.github.bourbon.base.system.SystemInfo;
import com.github.bourbon.base.threadpool.dynamic.ExecutorFactory;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.CollectionUtils;
import com.github.bourbon.base.utils.concurrent.ConcurrentHashMultiSet;
import com.github.bourbon.base.utils.concurrent.NamedThreadFactory;
import com.github.bourbon.ump.Reporter;
import com.github.bourbon.ump.TpMonitorService;
import com.github.bourbon.ump.constant.UMPConstants;
import com.github.bourbon.ump.domain.CallerInfo;
import com.github.bourbon.ump.domain.LogType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/27 00:53
 */
public class SimpleTpMonitorService extends AbstractLifecycle implements TpMonitorService {

    private static final Logger log = LoggerFactory.getLogger(SimpleTpMonitorService.class);
    private static final AppInfo APP_INFO = SystemInfo.appInfo;

    private final Map<String, Integer> keyMetaMap = new ConcurrentHashMap<>();
    private final Map<String, ConcurrentHashMultiSet<Integer>> tpCountMap = new ConcurrentHashMap<>();

    @Inject
    private Reporter reporter;

    private ScheduledExecutorService executorService;

    @Override
    public void handle(CallerInfo callerInfo, long elapsedTime) {
        String key = callerInfo.getKey();
        keyMetaMap.putIfAbsent(key, UMPConstants.DEFAULT_TP_METRIC_PERIOD);
        ConcurrentHashMultiSet<Integer> elapsedTimeCounter = tpCountMap.get(key);
        if (elapsedTimeCounter == null) {
            if (tpCountMap.size() >= 20_000) {
                return;
            }
            elapsedTimeCounter = tpCountMap.computeIfAbsent(key, o -> new ConcurrentHashMultiSet<>());
        }
        elapsedTimeCounter.add((int) elapsedTime);
    }

    @Override
    protected void doInitialize() {
        executorService = ExecutorFactory.Managed.newSingleScheduledExecutorService("ump", new NamedThreadFactory("UMP-counter4TP", true), (r, e) -> log.warn("SimpleTpService task queue full, begin reject!"));
        executorService.scheduleAtFixedRate(new WriteTPLogTask(), 0L, 1_000L, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void doDestroy() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(5L, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("SimpleTpMonitorService doDestroy error!", e);
        }
        executorService = null;
    }

    private class WriteTPLogTask implements Runnable {
        private static final int LINE_LIMIT_CHARS = 2048;
        private final StringBuilder fullLogBuffer = new StringBuilder(LINE_LIMIT_CHARS);
        private final StringBuilder detailLogBuffer = new StringBuilder(LINE_LIMIT_CHARS);

        @Override
        public void run() {
            try {
                long currentTimePoint = (SystemClock.currentTimeMillis() + 5L) / 1_000L;
                List<Pair<Pair<String, Long>, ConcurrentHashMultiSet<Integer>>> list = new ArrayList<>();

                for (Map.Entry<String, Integer> entry : keyMetaMap.entrySet()) {
                    int period = entry.getValue();
                    period /= 1_000L;
                    if (currentTimePoint % (long) period == 0L) {
                        String key = entry.getKey();
                        list.add(Pair.of(Pair.of(key, currentTimePoint - (long) period), tpCountMap.get(key)));
                        tpCountMap.put(key, new ConcurrentHashMultiSet<>());
                    }
                }

                writeTPLog(list);
            } catch (Exception e) {
                log.error(e);
            }
        }

        private void writeTPLog(List<Pair<Pair<String, Long>, ConcurrentHashMultiSet<Integer>>> list) {
            if (!CollectionUtils.isEmpty(list)) {
                for (Pair<Pair<String, Long>, ConcurrentHashMultiSet<Integer>> item : list) {
                    ConcurrentHashMultiSet<Integer> counter = item.r2;
                    if (counter != null && !counter.isEmpty()) {
                        final Pair<String, Long> r1 = item.r1;
                        String key = r1.r1;
                        String time = r1.r2.toString();

                        for (Integer elapsedTime : counter.elementSet()) {
                            detailLogBuffer.append(elapsedTime).append(StringConstants.COMMA).append(counter.get(elapsedTime)).append(StringConstants.COMMA);
                            if (detailLogBuffer.length() > LINE_LIMIT_CHARS) {
                                detailLogBuffer.deleteCharAt(detailLogBuffer.length() - 1);
                                doWrite(reporter, key, time);
                            }
                        }

                        if (detailLogBuffer.length() > 0) {
                            detailLogBuffer.deleteCharAt(detailLogBuffer.length() - 1);
                            doWrite(reporter, key, time);
                        }
                    }
                }
            }
        }

        private void doWrite(Reporter reporter, String key, String time) {
            fullLogBuffer.append(time);
            fullLogBuffer.append("{\"k\":\"").append(key);
            if (!CharSequenceUtils.isEmpty(APP_INFO.getAppId())) {
                fullLogBuffer.append("\",\"a\":\"").append(APP_INFO.getAppId());
            }
            fullLogBuffer.append("\",\"e\":\"").append(detailLogBuffer).append("\"}");
            reporter.report(LogType.TP, fullLogBuffer.toString());
            fullLogBuffer.delete(0, fullLogBuffer.length());
            detailLogBuffer.delete(0, detailLogBuffer.length());
        }
    }
}