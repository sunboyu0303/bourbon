package com.github.bourbon.ump.support;

import com.github.bourbon.base.extension.support.AbstractLifecycle;
import com.github.bourbon.ump.LogHandler;
import com.github.bourbon.ump.Reporter;
import com.github.bourbon.ump.domain.LogType;
import com.github.bourbon.ump.domain.MessagesStore;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/27 17:26
 */
public abstract class AbstractReporter extends AbstractLifecycle implements Reporter {

    private Map<LogType, MessagesStore> buffers;

    @Override
    public boolean report(LogType type, String log) {
        return buffers.get(type).storeMsg(log);
    }

    @Override
    protected void doInitialize() {
        Map<LogType, MessagesStore> map = new EnumMap<>(LogType.class);
        LogHandler handler;
        LogType logType;

        logType = LogType.TP;
        if ((handler = getLogHandler(logType)) != null) {
            map.put(logType, new MessagesStore(handler, 100_000, getName() + logType.getType()));
        }

        logType = LogType.JVM;
        if ((handler = getLogHandler(logType)) != null) {
            map.put(logType, new MessagesStore(handler, 100, getName() + logType.getType()));
        }

        logType = LogType.ALIVE;
        if ((handler = getLogHandler(logType)) != null) {
            map.put(logType, new MessagesStore(handler, 1_000, getName() + logType.getType()));
        }
        buffers = map;
    }

    @Override
    protected void doDestroy() {
        buffers.values().forEach(MessagesStore::close);
    }

    protected abstract LogHandler getLogHandler(LogType logType);

    protected abstract String getName();
}