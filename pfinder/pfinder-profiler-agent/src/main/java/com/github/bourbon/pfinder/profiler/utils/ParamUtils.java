package com.github.bourbon.pfinder.profiler.utils;

import com.github.bourbon.base.constant.BooleanConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.MapUtils;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/7 14:49
 */
public final class ParamUtils {

    private ParamUtils() {
        throw new UnsupportedOperationException();
    }

    public static Map<String, String> parseQueryString(String queryString, String charset) {
        if (queryString != null && !queryString.isEmpty()) {
            Map<String, String> parameterMap = MapUtils.newHashMap();
            for (String tagStr : queryString.split(StringConstants.AND)) {
                String[] tagParts = tagStr.split(StringConstants.EQUAL, 2);
                try {
                    if (tagParts.length == 1) {
                        parameterMap.put(URLDecoder.decode(tagStr, charset), BooleanConstants.TRUE.toString());
                    } else {
                        parameterMap.put(URLDecoder.decode(tagParts[0], charset), URLDecoder.decode(tagParts[1], charset));
                    }
                } catch (Exception e) {
                    throw new RuntimeException("parse query parameter error", e);
                }
            }
            return parameterMap;
        }
        return Collections.emptyMap();
    }
}