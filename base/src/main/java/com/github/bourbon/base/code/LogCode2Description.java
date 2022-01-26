package com.github.bourbon.base.code;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.space.SpaceId;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/27 15:01
 */
public final class LogCode2Description {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogCode2Description.class);

    private static final Map<SpaceId, LogCode2Description> LOG_CODE_2_DESCRIPTION_MAP = MapUtils.newConcurrentHashMap();

    public static String convert(String spaceName, String code) {
        return create(spaceName).convert(code);
    }

    public static String convert(SpaceId spaceId, String code) {
        return create(spaceId).convert(code);
    }

    public static LogCode2Description create(String spaceName) {
        return create(SpaceId.withSpaceName(spaceName));
    }

    public static LogCode2Description create(SpaceId spaceId) {
        return LOG_CODE_2_DESCRIPTION_MAP.computeIfAbsent(spaceId, LogCode2Description::new);
    }

    public static void remove(String spaceName) {
        remove(SpaceId.withSpaceName(spaceName));
    }

    public static void remove(SpaceId spaceId) {
        if (spaceId != null) {
            LOG_CODE_2_DESCRIPTION_MAP.remove(spaceId);
        }
    }

    private String logFormat;
    private Properties properties;

    private LogCode2Description(SpaceId spaceId) {
        logFormat = spaceId.getSpaceName().toUpperCase() + "-%s: %s";
        String prefix = spaceId.getSpaceName().replace(StringConstants.DOT, StringConstants.SLASH) + "/log-codes";
        String encoding = BooleanUtils.defaultSupplierIfPredicate(Locale.getDefault().toString(), t -> !CharSequenceUtils.isEmpty(t), t -> t, Locale.ENGLISH::toString);
        String fileName = prefix + StringConstants.UNDERLINE + encoding + StringConstants.PROPERTIES_FILE_SUFFIX;
        if (getClass().getClassLoader().getResource(fileName) == null) {
            fileName = prefix + StringConstants.PROPERTIES_FILE_SUFFIX;
        }
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileName)) {
            properties = new Properties();
            if (in == null) {
                LOGGER.error("Code file for CodeSpace \"{}\" doesn't exist!", spaceId.getSpaceName());
            } else {
                properties.load(new InputStreamReader(in));
            }
        } catch (Throwable e) {
            LOGGER.error("Code space \"{}\" initializing failed!", spaceId.getSpaceName(), e);
        }
    }

    private String convert(String code) {
        return String.format(logFormat, code, ObjectUtils.defaultSupplierIfNull(properties.get(code), () -> "Unknown Code"));
    }
}