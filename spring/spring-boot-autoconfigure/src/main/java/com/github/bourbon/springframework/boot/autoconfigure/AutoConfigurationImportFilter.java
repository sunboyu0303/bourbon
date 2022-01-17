package com.github.bourbon.springframework.boot.autoconfigure;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/15 14:15
 */
@FunctionalInterface
public interface AutoConfigurationImportFilter {

    boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata);
}