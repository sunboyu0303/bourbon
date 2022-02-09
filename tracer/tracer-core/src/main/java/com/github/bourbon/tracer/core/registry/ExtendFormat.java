package com.github.bourbon.tracer.core.registry;

import com.github.bourbon.base.constant.StringConstants;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/2/9 16:57
 */
public interface ExtendFormat<C> extends Format<C> {
    final class Builtin<C> implements ExtendFormat<C> {
        private final String name;

        private Builtin(String name) {
            this.name = name;
        }

        public static final Format<TextMap> B3_TEXT_MAP = new ExtendFormat.Builtin<>("B3_TEXT_MAP");
        public static final Format<TextMap> B3_HTTP_HEADERS = new ExtendFormat.Builtin<>("B3_HTTP_HEADERS");

        @Override
        public String toString() {
            return ExtendFormat.Builtin.class.getSimpleName() + StringConstants.DOT + name;
        }
    }
}