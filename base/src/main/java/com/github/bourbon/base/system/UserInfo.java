package com.github.bourbon.base.system;

import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.SystemUtils;

import java.io.File;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/15 17:31
 */
public class UserInfo {

    private final String userName = fixPath(SystemUtils.get("user.name"));
    private final String userHome = fixPath(SystemUtils.get("user.home"));
    private final String userDir = fixPath(SystemUtils.get("user.dir"));
    private final String javaIoTmpDir = fixPath(SystemUtils.get("java.io.tmpdir"));
    private final String userLanguage = SystemUtils.get("user.language");
    private final String userCountry = SystemUtils.get("user.country");

    UserInfo() {
    }

    public final String getName() {
        return userName;
    }

    public final String getHome() {
        return userHome;
    }

    public final String getDir() {
        return userDir;
    }

    public final String getTempDir() {
        return javaIoTmpDir;
    }

    public final String getLanguage() {
        return userLanguage;
    }

    public final String getCountry() {
        return userCountry;
    }

    private static String fixPath(String path) {
        return CharSequenceUtils.addSuffixIfNot(path, File.separator);
    }
}