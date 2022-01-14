package com.github.bourbon.base.system;

import com.github.bourbon.base.lang.Singleton;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/15 11:34
 */
public final class SystemInfo {

    public static final HostInfo hostInfo = Singleton.get(HostInfo.class, HostInfo::new);

    public static final JvmSpecInfo jvmSpecInfo = Singleton.get(JvmSpecInfo.class, JvmSpecInfo::new);

    public static final JvmInfo jvmInfo = Singleton.get(JvmInfo.class, JvmInfo::new);

    public static final JavaSpecInfo javaSpecInfo = Singleton.get(JavaSpecInfo.class, JavaSpecInfo::new);

    public static final JavaInfo javaInfo = Singleton.get(JavaInfo.class, JavaInfo::new);

    public static final JavaRuntimeInfo javaRuntimeInfo = Singleton.get(JavaRuntimeInfo.class, JavaRuntimeInfo::new);

    public static final OsInfo osInfo = Singleton.get(OsInfo.class, OsInfo::new);

    public static final UserInfo userInfo = Singleton.get(UserInfo.class, UserInfo::new);

    public static final RuntimeInfo runtimeInfo = Singleton.get(RuntimeInfo.class, RuntimeInfo::new);

    public static final AppInfo appInfo = Singleton.get(AppInfo.class, AppInfo::new);

    private SystemInfo() {
    }
}