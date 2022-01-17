package com.github.bourbon.springframework.boot.type.classreading;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.io.IOException;
import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 13:00
 */
public class ConcurrentReferenceCachingMetadataReaderFactory extends SimpleMetadataReaderFactory {

    private final Map<Resource, MetadataReader> cache = new ConcurrentReferenceHashMap<>();

    public ConcurrentReferenceCachingMetadataReaderFactory() {
    }

    public ConcurrentReferenceCachingMetadataReaderFactory(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    public ConcurrentReferenceCachingMetadataReaderFactory(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public MetadataReader getMetadataReader(Resource resource) throws IOException {
        MetadataReader metadataReader = cache.get(resource);
        if (metadataReader == null) {
            metadataReader = createMetadataReader(resource);
            cache.put(resource, metadataReader);
        }
        return metadataReader;
    }

    protected MetadataReader createMetadataReader(Resource resource) throws IOException {
        return super.getMetadataReader(resource);
    }

    public void clearCache() {
        cache.clear();
    }
}