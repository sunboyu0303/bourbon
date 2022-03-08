package com.github.bourbon.pfinder.profiler.service;

import com.github.bourbon.pfinder.profiler.consts.AddonTags;
import com.github.bourbon.pfinder.profiler.utils.ServiceFactoryUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2022/3/6 17:04
 */
public class SpiServiceLoader implements PFinderServiceLoader {

    private final List<ServiceFactory> registeredServices = new ArrayList<>();

    public SpiServiceLoader register(Object serviceInstance) {
        ServiceFactory serviceFactory = ServiceFactoryUtils.instanceToFactory(serviceInstance);
        serviceFactory.addon().getTags().put(AddonTags.SERVICE_TYPE.key(), AddonTags.ServiceTypes.BASIC.getType());
        this.registeredServices.add(serviceFactory);
        return this;
    }

    @Override
    public Iterable<ServiceFactory> load() {
        return () -> new Iterator<ServiceFactory>() {
            final Iterator<ServiceFactory> registeredIterator = SpiServiceLoader.this.registeredServices.iterator();
            final Iterator<Addon> addonIterator = AddonLoader.load("service").iterator();

            @Override
            public boolean hasNext() {
                return this.registeredIterator.hasNext() || this.addonIterator.hasNext();
            }

            @Override
            public ServiceFactory next() {
                return this.registeredIterator.hasNext() ? this.registeredIterator.next() : ServiceFactoryUtils.addonToFactory((Addon) this.addonIterator.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("unsupported remove action");
            }
        };
    }
}