package com.github.bourbon.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/1/29 09:37
 */
public final class ResourceUtils {

    public static URL[] getResources(String resourceName) {
        List<URL> urls = ListUtils.newLinkedList();
        boolean found = getResources(urls, resourceName, ClassLoaderUtils.getContextClassLoader(), false);
        if (!found) {
            getResources(urls, resourceName, ResourceUtils.class.getClassLoader(), false);
        }
        if (!found) {
            getResources(urls, resourceName, null, true);
        }
        return getDistinctURLs(urls);
    }

    public static URL[] getResources(String resourceName, Class<?> referrer) {
        ClassLoader classLoader = ClassLoaderUtils.getReferrerClassLoader(referrer);
        List<URL> urls = ListUtils.newLinkedList();
        getResources(urls, resourceName, classLoader, classLoader == null);
        return getDistinctURLs(urls);
    }

    public static URL[] getResources(String resourceName, ClassLoader classLoader) {
        List<URL> urls = ListUtils.newLinkedList();
        getResources(urls, resourceName, classLoader, classLoader == null);
        return getDistinctURLs(urls);
    }

    private static boolean getResources(List<URL> urlList, String resourceName, ClassLoader classLoader, boolean sysClassLoader) {
        if (resourceName == null) {
            return false;
        }
        Enumeration<URL> i = null;
        try {
            if (classLoader != null) {
                i = classLoader.getResources(resourceName);
            } else if (sysClassLoader) {
                i = ClassLoader.getSystemResources(resourceName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (i != null && i.hasMoreElements()) {
            while (i.hasMoreElements()) {
                urlList.add(i.nextElement());
            }
            return true;
        } else {
            return false;
        }
    }

    private static URL[] getDistinctURLs(List<URL> urls) {
        if (!CollectionUtils.isEmpty(urls)) {
            return ListUtils.newArrayList(SetUtils.newLinkedHashSet(urls)).toArray(new URL[0]);
        } else {
            return new URL[0];
        }
    }

    public static URL getResource(String resourceName) {
        if (resourceName == null) {
            return null;
        }
        URL url;
        ClassLoader classLoader = ClassLoaderUtils.getContextClassLoader();
        if (classLoader != null) {
            url = classLoader.getResource(resourceName);
            if (url != null) {
                return url;
            }
        }

        classLoader = ResourceUtils.class.getClassLoader();
        if (classLoader != null) {
            url = classLoader.getResource(resourceName);
            if (url != null) {
                return url;
            }
        }

        return ClassLoader.getSystemResource(resourceName);
    }

    public static URL getResource(String resourceName, Class<?> referrer) {
        return ObjectUtils.defaultIfNullElseFunction(resourceName, name -> BooleanUtils.defaultSupplierIfPredicate(
                ClassLoaderUtils.getReferrerClassLoader(referrer), ObjectUtils::nonNull, cl -> cl.getResource(name), () -> ResourceUtils.class.getClassLoader().getResource(name)
        ));
    }

    public static URL getResource(String resourceName, ClassLoader classLoader) {
        return ObjectUtils.defaultIfNullElseFunction(resourceName, name -> ObjectUtils.defaultSupplierIfNullElseFunction(
                classLoader, cl -> cl.getResource(name), () -> ResourceUtils.class.getClassLoader().getResource(name)
        ));
    }

    public static InputStream getResourceAsStream(String resourceName) {
        URL url = getResource(resourceName);
        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static InputStream getResourceAsStream(String resourceName, Class<?> referrer) {
        URL url = getResource(resourceName, referrer);
        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static InputStream getResourceAsStream(String resourceName, ClassLoader classLoader) {
        URL url = getResource(resourceName, classLoader);
        try {
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static URL[] whichClasses(String className) {
        return getResources(ClassUtils.getClassNameAsResource(className));
    }

    public static URL[] whichClasses(String className, Class<?> referrer) {
        return getResources(ClassUtils.getClassNameAsResource(className), referrer);
    }

    public static URL[] whichClasses(String className, ClassLoader classLoader) {
        return getResources(ClassUtils.getClassNameAsResource(className), classLoader);
    }

    public static URL whichClass(String className) {
        return getResource(ClassUtils.getClassNameAsResource(className));
    }

    public static URL whichClass(String className, Class<?> referrer) {
        return getResource(ClassUtils.getClassNameAsResource(className), referrer);
    }

    public static URL whichClass(String className, ClassLoader classLoader) {
        return getResource(ClassUtils.getClassNameAsResource(className), classLoader);
    }

    private ResourceUtils() {
    }
}