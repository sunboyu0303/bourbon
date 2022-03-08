package com.github.bourbon.pfinder.profiler.utils;

import com.github.bourbon.pfinder.profiler.exception.PfinderAddonException;
import com.github.bourbon.pfinder.profiler.load.Addon;
import com.github.bourbon.pfinder.profiler.load.AgentLoader;
import com.github.bourbon.pfinder.profiler.service.ServiceFactory;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 22:00
 */
public final class ServiceFactoryUtils {

    private ServiceFactoryUtils() {
        throw new UnsupportedOperationException();
    }

    public static ServiceFactory instanceToFactory(final Object serviceInstance) {
        return new ServiceFactory() {
            private Addon addon;

            @Override
            public Addon addon() {
                return this.addon == null ? (this.addon = Addon.parse(serviceInstance.getClass().getName())) : this.addon;
            }

            @Override
            public Class<?> type() {
                return serviceInstance.getClass();
            }

            @Override
            public Object get() {
                return serviceInstance;
            }

            @Override
            public String toString() {
                return "ServiceFactoryForInstance[" + serviceInstance + "]";
            }
        };
    }

    public static ServiceFactory addonToFactory(final Addon addon) {
        return new ServiceFactory() {
            private Class<?> aClass;

            @Override
            public Addon addon() {
                return addon;
            }

            @Override
            public Class<?> type() {
                try {
                    return this.aClass == null ? (this.aClass = Class.forName(addon.getClassName(), true, AgentLoader.getClassLoader())) : this.aClass;
                } catch (ClassNotFoundException e) {
                    throw new PfinderAddonException("load addon class error. addon=" + addon, e);
                }
            }

            @Override
            public Object get() {
                try {
                    return this.type().newInstance();
                } catch (Exception e) {
                    throw new PfinderAddonException("create Pfinder Service instance error. class=" + type().getName(), e);
                }
            }

            @Override
            public String toString() {
                return "ServiceFactoryForAddon[" + addon + "]";
            }
        };
    }
}