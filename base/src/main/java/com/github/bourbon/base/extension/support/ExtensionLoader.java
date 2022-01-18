package com.github.bourbon.base.extension.support;


import com.github.bourbon.base.constant.CharConstants;
import com.github.bourbon.base.extension.ExtensionAccessorAware;
import com.github.bourbon.base.extension.Lifecycle;
import com.github.bourbon.base.extension.annotation.Inject;
import com.github.bourbon.base.extension.annotation.SPI;
import com.github.bourbon.base.extension.model.ScopeModel;
import com.github.bourbon.base.lang.Holder;
import com.github.bourbon.base.lang.Prioritized;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.SystemLogger;
import com.github.bourbon.base.utils.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/11/23 14:37
 */
public class ExtensionLoader<T> {

    private static final Logger LOGGER = new SystemLogger();

    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
    private final AtomicReference<Map<String, Class<?>>> cachedClasses = new AtomicReference<>();
    private final ConcurrentMap<Class<?>, Object> extensionInstances = new ConcurrentHashMap<>(64);

    private final Class<T> type;
    private final ExtensionDirector extensionDirector;
    private final ScopeModel scopeModel;
    private final InstantiationStrategy instantiationStrategy;

    private String cachedDefaultName;

    ExtensionLoader(Class<T> type, ExtensionDirector extensionDirector, ScopeModel scopeModel) {
        this.type = type;
        this.extensionDirector = extensionDirector;
        this.scopeModel = scopeModel;
        this.instantiationStrategy = new InstantiationStrategy(scopeModel.getScopeModelAccessor());
    }

    public T getOrDefaultExtension(String name) {
        return BooleanUtils.defaultSupplierIfFalse(containsExtension(name), () -> getExtension(name), this::getDefaultExtension);
    }

    public boolean containsExtension(String name) {
        return getExtensionClasses().containsKey(name);
    }

    public T getExtension(String name) {
        return ObjectUtils.requireNonNull(getOrCreateExtension(name), () -> new IllegalArgumentException("Not find extension: " + name));
    }

    public T getDefaultExtension() {
        getExtensionClasses();
        return BooleanUtils.defaultIfFalse(!CharSequenceUtils.isBlank(cachedDefaultName), () -> getExtension(cachedDefaultName));
    }

    @SuppressWarnings("unchecked")
    public T getOrCreateExtension(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Extension name == null");
        }
        final Holder<Object> holder = cachedInstances.computeIfAbsent(name, o -> Holder.nullOf());
        if (holder.get() == null) {
            holder.lock();
            try {
                if (holder.get() == null) {
                    holder.set(createExtension(name));
                }
            } finally {
                holder.unlock();
            }
        }
        return (T) holder.get();
    }

    @SuppressWarnings("unchecked")
    private T createExtension(String name) {
        Class<?> clazz = getExtensionClass(name);
        if (clazz == null) {
            throw new NullPointerException("Not find extension class: " + name);
        }
        try {
            T instance = (T) extensionInstances.get(clazz);
            if (instance == null) {
                instance = (T) extensionInstances.putIfAbsent(clazz, instantiationStrategy.instantiate(clazz));
                if (instance == null) {
                    instance = (T) extensionInstances.get(clazz);
                    injectExtension(instance);
                    if (instance instanceof ExtensionAccessorAware) {
                        ((ExtensionAccessorAware) instance).setExtensionAccessor(extensionDirector);
                    }
                    if (instance instanceof Lifecycle) {
                        ((Lifecycle) instance).initialize();
                    }
                }
            }
            return instance;
        } catch (Exception t) {
            throw new IllegalStateException("Extension instance (name: " + name + ", class: " + type + ") couldn't be instantiated: " + t.getMessage(), t);
        }
    }

    private void injectExtension(T instance) {
        try {
            Field[] fields = instance.getClass().getDeclaredFields();
            for (Field field : fields) {
                Class<?> pt = field.getType();
                Inject inject = field.getAnnotation(Inject.class);
                if (inject == null || ClassUtils.isPrimitive(pt)) {
                    continue;
                }
                Object object = extensionDirector.getExtensionLoader(pt).getOrDefaultExtension(inject.value());
                if (object != null) {
                    ReflectUtils.setAccessible(field);
                    field.set(instance, object);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    private Class<?> getExtensionClass(String name) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Extension name == null");
        }
        return getExtensionClasses().get(name);
    }

    private Map<String, Class<?>> getExtensionClasses() {
        if (cachedClasses.get() == null) {
            synchronized (cachedClasses) {
                if (cachedClasses.get() == null) {
                    cachedClasses.set(loadExtensionClasses());
                }
            }
        }
        return cachedClasses.get();
    }

    private Map<String, Class<?>> loadExtensionClasses() {
        cacheDefaultExtensionName();
        String[] paths = {"META-INF/services/", "META-INF/bourbon/"};
        Map<String, Class<?>> extensionClasses = MapUtils.newHashMap();
        Arrays.stream(paths).forEach(path -> loadDirectory(extensionClasses, path));
        return extensionClasses;
    }

    private void cacheDefaultExtensionName() {
        final SPI defaultAnnotation = type.getAnnotation(SPI.class);
        if (defaultAnnotation != null) {
            cachedDefaultName = defaultAnnotation.value().trim();
        }
    }

    private void loadDirectory(Map<String, Class<?>> extensionClasses, String path) {
        String fileName = path + type.getName();
        try {
            List<ClassLoader> classLoadersToLoad = ListUtils.newLinkedList();
            ClassLoader extensionLoaderClassLoader = ExtensionLoader.class.getClassLoader();
            if (ClassLoader.getSystemClassLoader() != extensionLoaderClassLoader) {
                classLoadersToLoad.add(extensionLoaderClassLoader);
            }

            Set<ClassLoader> classLoaders = scopeModel.getClassLoaders();

            if (CollectionUtils.isEmpty(classLoaders)) {
                Enumeration<URL> resources = ClassLoader.getSystemResources(fileName);
                if (resources != null) {
                    while (resources.hasMoreElements()) {
                        loadResource(extensionClasses, null, resources.nextElement());
                    }
                }
            } else {
                classLoadersToLoad.addAll(classLoaders);
            }

            Map<ClassLoader, Set<URL>> resources = ClassLoaderResourceLoader.loadResources(fileName, classLoadersToLoad);

            for (Map.Entry<ClassLoader, Set<URL>> entry : resources.entrySet()) {
                if (!CollectionUtils.isEmpty(entry.getValue())) {
                    for (URL url : entry.getValue()) {
                        loadResource(extensionClasses, entry.getKey(), url);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, URL url) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
            String line;
            String clazz;
            while ((line = reader.readLine()) != null) {
                final int ci = line.indexOf(CharConstants.POUND);
                if (ci >= 0) {
                    line = line.substring(0, ci);
                }
                line = line.trim();
                if (line.length() > 0) {
                    String name = null;
                    int i = line.indexOf(CharConstants.EQUAL);
                    if (i > 0) {
                        name = line.substring(0, i).trim();
                        clazz = line.substring(i + 1).trim();
                    } else {
                        clazz = line;
                    }
                    if (!CharSequenceUtils.isEmpty(clazz)) {
                        try {
                            loadClass(extensionClasses, url, Class.forName(clazz, true, classLoader), name);
                        } catch (Throwable e) {
                            LOGGER.error(e.toString());
                        }
                    }
                }
            }
        } catch (Throwable e) {
            LOGGER.error(e);
        }
    }

    private void loadClass(Map<String, Class<?>> extensionClasses, URL url, Class<?> clazz, String name) {
        if (!type.isAssignableFrom(clazz)) {
            throw new IllegalStateException("Error occurred when loading extension class (interface: " + type + ", class line: " + clazz.getName() + "), class " + clazz.getName() + " is not subtype of interface.");
        }
        if (CharSequenceUtils.isEmpty(name)) {
            name = clazz.getSimpleName();
            if (name.endsWith(type.getSimpleName())) {
                name = name.substring(0, name.length() - type.getSimpleName().length());
            }
            name = name.toLowerCase();
            if (name.length() == 0) {
                throw new IllegalStateException("No such extension name for the class " + clazz.getName() + " in the config " + url);
            }
        }
        Class<?> c = extensionClasses.get(name);
        if (c == null) {
            extensionClasses.put(name, clazz);
        } else if (c != clazz) {
            throw new IllegalStateException("Duplicate extension " + type.getName() + " name " + name + " on " + c.getName() + " and " + clazz.getName());
        }
    }

    public Set<T> getSupportedExtensionInstances() {
        return BooleanUtils.defaultSupplierIfPredicate(getExtensionClasses().keySet(), e -> !CollectionUtils.isEmpty(e),
                e -> SetUtils.newLinkedHashSet(e.stream().map(this::getExtension).sorted(Prioritized.COMPARATOR).collect(Collectors.toList())),
                SetUtils::newLinkedHashSet
        );
    }
}