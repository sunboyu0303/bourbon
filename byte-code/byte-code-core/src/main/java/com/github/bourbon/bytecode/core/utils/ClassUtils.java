package com.github.bourbon.bytecode.core.utils;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.SetUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/10 16:25
 */
public final class ClassUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);
    private static final String PATH = ClassUtils.class.getResource(StringConstants.SLASH).getPath();
    private static final Set<String> exceptMethods = SetUtils.newHashSet(
            "getClass", "hashCode", "equals", "clone", "toString", "notify", "notifyAll", "wait", "finalize"
    );
    private static final GeneratorClassLoader classLoader = new GeneratorClassLoader();

    public static void outputClazz(String internalName, byte[] bytes) {
        try (FileOutputStream out = new FileOutputStream(new File(PATH + internalName + StringConstants.CLASS_FILE_SUFFIX))) {
            out.write(bytes);
        } catch (Exception e) {
            LOGGER.error(StringConstants.EMPTY, e);
        }
    }

    public static boolean exceptMethod(String methodName) {
        return !exceptMethods.contains(methodName);
    }

    public static Class<?> defineClassFromClassFile(String className, byte[] classFile) {
        return classLoader.defineClassFromClassFile(className, classFile);
    }

    private static class GeneratorClassLoader extends ClassLoader {
        Class<?> defineClassFromClassFile(String className, byte[] classFile) {
            return defineClass(className, classFile, 0, classFile.length);
        }
    }

    private ClassUtils() {
    }
}