package com.github.bourbon.base.system;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.ReflectUtils;

import java.io.Console;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/16 10:42
 */
public enum JavaVersion {
    EIGHT("1.8", Optional.class, "empty"),
    NINE("9", Optional.class, "stream"),
    TEN("10", Optional.class, "orElseThrow"),
    ELEVEN("11", String.class, "strip"),
    TWELVE("12", String.class, "describeConstable"),
    THIRTEEN("13", String.class, "stripIndent"),
    FOURTEEN("14", MethodHandles.Lookup.class, "hasFullPrivilegeAccess"),
    FIFTEEN("15", CharSequence.class, "isEmpty"),
    SIXTEEN("16", Stream.class, "toList"),
    SEVENTEEN("17", Console.class, "charset");

    private final String name;
    private final boolean available;
    private static JavaVersion candidate;

    static {
        List<JavaVersion> candidates = Arrays.asList(values());
        Collections.reverse(candidates);
        Iterator<JavaVersion> iterator = candidates.iterator();
        do {
            if (!iterator.hasNext()) {
                candidate = EIGHT;
            }
            candidate = iterator.next();
        } while (!candidate.available);
    }

    JavaVersion(String name, Class<?> clazz, String methodName) {
        this.name = name;
        this.available = ObjectUtils.nonNull(ReflectUtils.getMethod(clazz, methodName));
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isEqualOrNewerThan(JavaVersion version) {
        return !isOlderThan(version);
    }

    public boolean isOlderThan(JavaVersion version) {
        return compareTo(version) < 0;
    }

    public static JavaVersion getJavaVersion() {
        return candidate;
    }
}