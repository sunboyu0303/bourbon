package com.github.bourbon.pfinder.profiler.consts;

import com.github.bourbon.pfinder.profiler.utils.SmartKey;

import java.util.Arrays;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 15:45
 */
public final class AddonTags {

    public static final SmartKey<ServiceTypes> SERVICE_TYPE = SmartKey.of("service_type", t -> Arrays.stream(ServiceTypes.values()).filter(v -> v.getType().equals(t)).findFirst().orElse(null), "addonTag-服务类型", SmartKey.RemoteConfigLevel.NOBODY);
    public static final SmartKey<String> JAVA_VERSION = SmartKey.ofString("java_version", "addonTag-java版本", SmartKey.RemoteConfigLevel.NOBODY);
    public static final SmartKey<Integer> ORDER = SmartKey.ofInteger("order", "addonTag-顺序号", SmartKey.RemoteConfigLevel.NOBODY);

    public enum ServiceTypes {
        BASIC("basic"),
        NATIVE("native");

        private final String type;

        ServiceTypes(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }

    private AddonTags() {
        throw new UnsupportedOperationException();
    }
}