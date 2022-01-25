package com.github.bourbon.base.system;

import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.PropertiesUtils;
import com.github.bourbon.base.utils.SystemUtils;

import java.util.Properties;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/1 10:31
 */
public class AppInfo {
    private final Properties properties;
    private final String appId;
    private final String appName;

    AppInfo() {
        properties = PropertiesUtils.getProperties(AppInfo.class.getClassLoader(), "app.properties");
        appId = initAppId();
        appName = initAppName();
    }

    private String initAppId() {
        String appId = SystemUtils.get("app.id");
        if (!CharSequenceUtils.isBlank(appId)) {
            return appId.trim();
        }

        appId = SystemUtils.get("APP_ID");
        if (!CharSequenceUtils.isBlank(appId)) {
            return appId.trim();
        }

        appId = properties.getProperty("app.id");
        if (!CharSequenceUtils.isBlank(appId)) {
            return appId.trim();
        }

        return null;
    }

    private String initAppName() {
        String appName = SystemUtils.get("app.name");
        if (!CharSequenceUtils.isBlank(appName)) {
            return appName.trim();
        }

        appName = SystemUtils.get("APP_NAME");
        if (!CharSequenceUtils.isBlank(appName)) {
            return appName.trim();
        }

        appName = properties.getProperty("app.name");
        if (!CharSequenceUtils.isBlank(appName)) {
            return appName.trim();
        }

        return null;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppName() {
        return appName;
    }
}