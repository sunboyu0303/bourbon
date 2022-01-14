package com.github.bourbon.base.system;

import com.github.bourbon.base.lang.Singleton;

import static com.github.bourbon.base.utils.CharSequenceUtils.splitToArray;
import static com.github.bourbon.base.utils.SystemUtils.get;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/15 15:01
 */
public class JavaRuntimeInfo {

    private final String classPath = get("sun.boot.class.path");
    private final String dataModel = get("sun.arch.data.model");
    private final String javaRuntimeName = get("java.runtime.name");
    private final String javaRuntimeVersion = get("java.runtime.version");
    private final String javaHome = get("java.home");
    private final String javaExtDirs = get("java.ext.dirs");
    private final String javaEndorsedDirs = get("java.endorsed.dirs");
    private final String javaClassPath = get("java.class.path");
    private final String javaClassVersion = get("java.class.version");
    private final String javaLibraryPath = get("java.library.path");
    private final String pathSeparator = Singleton.get(OsInfo.class, OsInfo::new).getPathSeparator();
    private final String protocolPackages = get("java.protocol.handler.pkgs");

    JavaRuntimeInfo() {
    }

    public final String getSunBoothClassPath() {
        return classPath;
    }

    public final String getSunArchDataModel() {
        return dataModel;
    }

    public final String getJavaRuntimeName() {
        return javaRuntimeName;
    }

    public final String getJavaRuntimeVersion() {
        return javaRuntimeVersion;
    }

    public final String getJavaHome() {
        return javaHome;
    }

    public final String getJavaExtDirs() {
        return javaExtDirs;
    }

    public final String getJavaEndorsedDirs() {
        return javaEndorsedDirs;
    }

    public final String getJavaClassPath() {
        return javaClassPath;
    }

    public final String[] getJavaClassPathArray() {
        return splitToArray(javaClassPath, pathSeparator);
    }

    public final String getJavaClassVersion() {
        return javaClassVersion;
    }

    public final String getJavaLibraryPath() {
        return javaLibraryPath;
    }

    public final String[] getJavaLibraryPathArray() {
        return splitToArray(javaLibraryPath, pathSeparator);
    }

    public final String getProtocolPackages() {
        return protocolPackages;
    }
}