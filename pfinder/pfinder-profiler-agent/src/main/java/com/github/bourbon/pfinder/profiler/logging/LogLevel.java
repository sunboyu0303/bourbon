package com.github.bourbon.pfinder.profiler.logging;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/5 23:04
 */
public enum LogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN {
        final String[] aliases = new String[]{"WARNING"};

        @Override
        protected String[] aliases() {
            return this.aliases;
        }
    },
    ERROR {
        final String[] aliases = new String[]{"ERR"};

        @Override
        protected String[] aliases() {
            return this.aliases;
        }
    },
    SILENT {
        final String[] aliases = new String[]{"NONE"};

        @Override
        protected String[] aliases() {
            return this.aliases;
        }
    };

    public static final LogLevel DEFAULT_LEVEL = INFO;

    private static final String[] EMPTY_ALIASES = new String[0];

    protected String[] aliases() {
        return EMPTY_ALIASES;
    }

    public static LogLevel parse(String levelStr) {
        if (levelStr != null && !levelStr.isEmpty()) {
            levelStr = levelStr.toUpperCase();
            for (LogLevel level : values()) {
                if (level.name().equals(levelStr)) {
                    return level;
                }
                for (String alias : level.aliases()) {
                    if (alias.equals(levelStr)) {
                        return level;
                    }
                }
            }
            return DEFAULT_LEVEL;
        }
        return DEFAULT_LEVEL;
    }
}