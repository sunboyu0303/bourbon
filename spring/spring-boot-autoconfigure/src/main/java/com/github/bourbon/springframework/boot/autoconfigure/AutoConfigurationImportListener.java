package com.github.bourbon.springframework.boot.autoconfigure;

import java.util.EventListener;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 00:05
 */
@FunctionalInterface
public interface AutoConfigurationImportListener extends EventListener {

    void onAutoConfigurationImportEvent(AutoConfigurationImportEvent event);
}