package com.github.bourbon.pfinder.profiler.load;

import com.github.bourbon.pfinder.profiler.logging.Logger;
import com.github.bourbon.pfinder.profiler.logging.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 22:05
 */
public class AgentLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentLoader.class);

    private static final Enumeration<URL> EMPTY_RESOURCE = new Enumeration<URL>() {
        @Override
        public boolean hasMoreElements() {
            return false;
        }

        @Override
        public URL nextElement() {
            return null;
        }
    };

    private AgentLoader() {
        throw new UnsupportedOperationException();
    }

    public static ClassLoader getClassLoader() {
        return AgentLoader.class.getClassLoader();
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(String className) throws ClassNotFoundException {
        return (Class<T>) Class.forName(className, true, getClassLoader());
    }

    public static Enumeration<URL> loadResources(String resourcePath) {
        try {
            return getClassLoader().getResources(resourcePath);
        } catch (IOException e) {
            LOGGER.error("load resources error. path={}", resourcePath, e);
            return EMPTY_RESOURCE;
        }
    }
}