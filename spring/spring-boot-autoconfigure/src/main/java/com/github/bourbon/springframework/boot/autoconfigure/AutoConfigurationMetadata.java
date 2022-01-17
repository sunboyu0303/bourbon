package com.github.bourbon.springframework.boot.autoconfigure;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/15 14:16
 */
public interface AutoConfigurationMetadata {

    boolean wasProcessed(String className);

    Integer getInteger(String className, String key);

    Integer getInteger(String className, String key, Integer defaultValue);

    Set<String> getSet(String className, String key);

    Set<String> getSet(String className, String key, Set<String> defaultValue);

    String get(String className, String key);

    String get(String className, String key, String defaultValue);
}