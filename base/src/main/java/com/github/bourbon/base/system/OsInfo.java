package com.github.bourbon.base.system;

import static com.github.bourbon.base.utils.SystemUtils.get;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/15 17:10
 */
public class OsInfo {

    private final String osVersion = get("os.version");
    private final String osArch = get("os.arch");
    private final String osName = get("os.name");

    private final String fileEncoding = get("file.encoding");
    private final String fileSeparator = get("file.separator");
    private final String lineSeparator = get("line.separator");
    private final String pathSeparator = get("path.separator");

    private final boolean IS_OS_AIX = getOSMatches("AIX");
    private final boolean IS_OS_HP_UX = getOSMatches("HP-UX");
    private final boolean IS_OS_IRIX = getOSMatches("Irix");
    private final boolean IS_OS_LINUX = getOSMatches("Linux") || getOSMatches("LINUX");
    private final boolean IS_OS_MAC = getOSMatches("Mac");
    private final boolean IS_OS_MAC_OSX = getOSMatches("Mac OS X");
    private final boolean IS_OS_OS2 = getOSMatches("OS/2");
    private final boolean IS_OS_SOLARIS = getOSMatches("Solaris");
    private final boolean IS_OS_SUN_OS = getOSMatches("SunOS");
    private final boolean IS_OS_WINDOWS = getOSMatches("Windows");
    private final boolean IS_OS_WINDOWS_2000 = getOSMatches("Windows", "5.0");
    private final boolean IS_OS_WINDOWS_95 = getOSMatches("Windows 9", "4.0");
    private final boolean IS_OS_WINDOWS_98 = getOSMatches("Windows 9", "4.1");
    private final boolean IS_OS_WINDOWS_ME = getOSMatches("Windows", "4.9");
    private final boolean IS_OS_WINDOWS_NT = getOSMatches("Windows NT");
    private final boolean IS_OS_WINDOWS_XP = getOSMatches("Windows", "5.1");
    private final boolean IS_OS_WINDOWS_7 = getOSMatches("Windows", "6.1");
    private final boolean IS_OS_WINDOWS_8 = getOSMatches("Windows", "6.2");
    private final boolean IS_OS_WINDOWS_8_1 = getOSMatches("Windows", "6.3");
    private final boolean IS_OS_WINDOWS_10 = getOSMatches("Windows", "10.0");

    OsInfo() {
    }

    public final String getArch() {
        return osArch;
    }

    public final String getName() {
        return osName;
    }

    public final String getVersion() {
        return osVersion;
    }

    public final String getFileEncoding() {
        return fileEncoding;
    }

    public final String getFileSeparator() {
        return fileSeparator;
    }

    public final String getLineSeparator() {
        return lineSeparator;
    }

    public final String getPathSeparator() {
        return pathSeparator;
    }

    public final boolean isAix() {
        return IS_OS_AIX;
    }

    public final boolean isHpUx() {
        return IS_OS_HP_UX;
    }

    public final boolean isIrix() {
        return IS_OS_IRIX;
    }

    public final boolean isLinux() {
        return IS_OS_LINUX;
    }

    public final boolean isMac() {
        return IS_OS_MAC;
    }

    public final boolean isMacOsX() {
        return IS_OS_MAC_OSX;
    }

    public final boolean isOs2() {
        return IS_OS_OS2;
    }

    public final boolean isSolaris() {
        return IS_OS_SOLARIS;
    }

    public final boolean isSunOS() {
        return IS_OS_SUN_OS;
    }

    public final boolean isWindows() {
        return IS_OS_WINDOWS;
    }

    public final boolean isWindows2000() {
        return IS_OS_WINDOWS_2000;
    }

    public final boolean isWindows95() {
        return IS_OS_WINDOWS_95;
    }

    public final boolean isWindows98() {
        return IS_OS_WINDOWS_98;
    }

    public final boolean isWindowsME() {
        return IS_OS_WINDOWS_ME;
    }

    public final boolean isWindowsNT() {
        return IS_OS_WINDOWS_NT;
    }

    public final boolean isWindowsXP() {
        return IS_OS_WINDOWS_XP;
    }

    public final boolean isWindows7() {
        return IS_OS_WINDOWS_7;
    }

    public final boolean isWindows8() {
        return IS_OS_WINDOWS_8;
    }

    public final boolean isWindows8_1() {
        return IS_OS_WINDOWS_8_1;
    }

    public final boolean isWindows10() {
        return IS_OS_WINDOWS_10;
    }

    private boolean getOSMatches(String prefix) {
        return osName != null && osName.startsWith(prefix);
    }

    private boolean getOSMatches(String namePrefix, String versionPrefix) {
        return osName != null && osVersion != null && osName.startsWith(namePrefix) && osVersion.startsWith(versionPrefix);
    }
}