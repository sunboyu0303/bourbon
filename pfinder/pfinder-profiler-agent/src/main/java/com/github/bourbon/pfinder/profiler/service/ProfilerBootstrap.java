package com.github.bourbon.pfinder.profiler.service;

import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.ReflectUtils;
import com.github.bourbon.pfinder.profiler.consts.AddonTags;
import com.github.bourbon.pfinder.profiler.logging.Logger;
import com.github.bourbon.pfinder.profiler.logging.LoggerFactory;
import com.github.bourbon.pfinder.profiler.service.tribe.ServiceTribe;
import com.github.bourbon.pfinder.profiler.service.tribe.ServiceTribeUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 14:55
 */
public class ProfilerBootstrap implements ProfilerContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilerBootstrap.class);
    private ConcurrentMap<Class<?>, ServiceTribe> serviceTribeMap = new ConcurrentHashMap<>();
    private final AtomicBoolean running = new AtomicBoolean(false);

    public void shutdown() {
        if (running.get()) {
            List<ActivityService> activityServices = ListUtils.newArrayList(getAllService(ActivityService.class));
            ListIterator<ActivityService> listIterator = activityServices.listIterator(activityServices.size());
            while (listIterator.hasPrevious()) {
                ActivityService service = listIterator.previous();
                try {
                    service.shutdown();
                } catch (Exception e) {
                    LOGGER.error("shutdown service error. service={}", service.getClass().getName(), e);
                }
            }
            serviceTribeMap.clear();
            running.set(false);
        }
    }

    public void boot(PFinderServiceLoader serviceLoader) {
        if (running.compareAndSet(false, true)) {
            List<ServiceFactory> basicService = new ArrayList<>();
            List<ServiceFactory> otherService = new ArrayList<>();

            for (ServiceFactory factory : serviceLoader.load()) {
                if (factory.addon().getTag(AddonTags.SERVICE_TYPE, AddonTags.ServiceTypes.NATIVE).equals(AddonTags.ServiceTypes.BASIC)) {
                    basicService.add(factory);
                } else {
                    otherService.add(factory);
                }
            }

            bootService(basicService);

            Collection<ServiceLoadFilter> serviceLoadFilters = getAllService(ServiceLoadFilter.class);
            List<ServiceFactory> filteredService = new ArrayList<>();
            for (ServiceFactory serviceFactory : otherService) {
                boolean isNeedLoaded = true;
                for (ServiceLoadFilter serviceLoadFilter : serviceLoadFilters) {
                    if (!serviceLoadFilter.filterServiceAddon(serviceFactory.addon())) {
                        isNeedLoaded = false;
                        break;
                    }
                }
                if (isNeedLoaded) {
                    filteredService.add(serviceFactory);
                }
            }

            bootService(filteredService);

            getAllService(ServicesInitializedHook.class).forEach(hook -> hook.onInitialized(this));
        }
    }

    public void register(Object service) {
        for (Class<?> type : ReflectUtils.getAllInterfaceAndClass(service.getClass())) {
            ServiceTribe serviceTribe = serviceTribeMap.computeIfAbsent(type, c -> ObjectUtils.defaultIfNull(ServiceTribeUtils.tryCreateServiceTribe(c), ServiceTribe.NOOP));
            if (serviceTribe != ServiceTribe.NOOP) {
                serviceTribe.add(service);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clazz) {
        return (T) getServiceTribe(clazz).chief();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Collection<T> getAllService(Class<T> clazz) {
        return (Collection<T>) getServiceTribe(clazz).all();
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    private ServiceTribe getServiceTribe(Class<?> type) {
        return serviceTribeMap.getOrDefault(type, ServiceTribe.NOOP);
    }

    private void bootService(Iterable<ServiceFactory> serviceFactories) {
        ServiceTribe serviceTribe = ServiceTribeUtils.tryCreateServiceTribe(ActivityService.class);
        assert serviceTribe != null;
        Iterator<ServiceFactory> iterator = serviceFactories.iterator();
        while (true) {
            Object service;
            do {
                if (!iterator.hasNext()) {
                    for (Object o : serviceTribe.all()) {
                        ((ActivityService) o).initialize(this);
                    }
                    return;
                }
                service = iterator.next().get();
            } while (service instanceof Optional && !((Optional) service).available());

            this.register(service);

            if (service instanceof ActivityService) {
                serviceTribe.add(service);
            }
        }
    }
}