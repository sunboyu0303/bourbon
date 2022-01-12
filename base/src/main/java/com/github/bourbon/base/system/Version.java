package com.github.bourbon.base.system;

import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.constant.IntConstants;
import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.CharUtils;

import java.net.URL;
import java.security.CodeSource;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/16 13:55
 */
public final class Version {

    private Version() {
    }

    public static String getVersion(Class<?> cls) {
        return getVersion(cls, StringConstants.EMPTY);
    }

    public static String getVersion(Class<?> cls, String defaultVersion) {
        try {
            // find version info from MANIFEST.MF first
            Package pkg = cls.getPackage();
            String version = null;
            if (pkg != null) {
                version = pkg.getImplementationVersion();
                if (version != null && version.trim().length() > 0) {
                    return version;
                }
                version = pkg.getSpecificationVersion();
                if (version != null && version.trim().length() > 0) {
                    return version;
                }
            }

            // guess version from jar file name if nothing's found from MANIFEST.MF
            CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
            if (codeSource == null) {
                return defaultVersion;
            }

            URL location = codeSource.getLocation();
            if (location == null) {
                return defaultVersion;
            }
            String file = location.getFile();
            if (file != null && file.trim().length() > 0 && file.endsWith(StringConstants.JAR)) {
                version = getFromFile(file);
            }

            // return default version if no version info is found
            return (version == null || version.trim().length() == 0) ? defaultVersion : version;
        } catch (Exception e) {
            // return default version when any exception is thrown
            return defaultVersion;
        }
    }

    private static String getFromFile(String file) {
        // remove suffix ".jar": "path/to/group-module-x.y.z"
        file = file.substring(0, file.length() - IntConstants.I_4);
        // remove path: "group-module-x.y.z"
        int i = file.lastIndexOf(CharConstants.SLASH);
        if (i >= 0) {
            file = file.substring(i + 1);
        }
        // remove group: "module-x.y.z"
        i = file.indexOf(CharConstants.HYPHEN);
        if (i >= 0) {
            file = file.substring(i + 1);
        }
        // remove module: "x.y.z"
        while (file.length() > 0 && !Character.isDigit(file.charAt(0))) {
            i = file.indexOf(CharConstants.HYPHEN);
            if (i >= 0) {
                file = file.substring(i + 1);
            } else {
                break;
            }
        }
        return file;
    }

    public static int fromVersion(String verStr) {
        if (CharSequenceUtils.isBlank(verStr)) {
            return 0;
        }
        int[] versions = new int[]{0, 0, 0, 0};
        int index = 0;
        String segment;
        int cur = 0;
        int pos;
        do {
            if (index >= versions.length) {
                // More dots than "x.y.z.b" contains
                return 0;
            }
            pos = verStr.indexOf(CharConstants.DOT, cur);
            if (pos == -1) {
                segment = verStr.substring(cur);
            } else if (cur < pos) {
                segment = verStr.substring(cur, pos);
            } else {
                // Illegal format
                return 0;
            }
            versions[index] = parseInt(segment);
            if (versions[index] < 0 || versions[index] > IntConstants.I_255) {
                // Out of range [0, 255]
                return 0;
            }
            cur = pos + 1;
            index++;
        } while (pos > 0);
        return ((versions[0] & IntConstants.I_255) << IntConstants.I_24) |
                ((versions[1] & IntConstants.I_255) << IntConstants.I_16) |
                ((versions[2] & IntConstants.I_255) << IntConstants.I_8) |
                (versions[3] & IntConstants.I_255);
    }

    private static int parseInt(String str) {
        return BooleanUtils.defaultIfPredicate(str, s -> !CharSequenceUtils.isBlank(s), s -> {
            int num = 0;
            for (char ch : s.toCharArray()) {
                if (!CharUtils.isNumber(ch)) {
                    break;
                }
                num = num * 10 + (ch - CharConstants.ZERO);
            }
            return num;
        }, 0);
    }
}