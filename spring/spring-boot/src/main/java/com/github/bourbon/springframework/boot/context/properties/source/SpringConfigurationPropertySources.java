package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.origin.OriginLookup;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.*;
import java.util.function.Function;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/26 00:20
 */
class SpringConfigurationPropertySources implements Iterable<ConfigurationPropertySource> {

    private final Iterable<PropertySource<?>> sources;

    private final Map<PropertySource<?>, ConfigurationPropertySource> cache = new ConcurrentReferenceHashMap<>(16, ConcurrentReferenceHashMap.ReferenceType.SOFT);

    SpringConfigurationPropertySources(Iterable<PropertySource<?>> sources) {
        this.sources = ObjectUtils.requireNonNull(sources, "Sources must not be null");
    }

    boolean isUsingSources(Iterable<PropertySource<?>> sources) {
        return this.sources == sources;
    }

    @Override
    public Iterator<ConfigurationPropertySource> iterator() {
        return new SourcesIterator(sources.iterator(), this::adapt);
    }

    private ConfigurationPropertySource adapt(PropertySource<?> source) {
        ConfigurationPropertySource result = cache.get(source);
        if (result != null && result.getUnderlyingSource() == source) {
            return result;
        }
        result = SpringConfigurationPropertySource.from(source);
        if (source instanceof OriginLookup) {
            result = result.withPrefix(((OriginLookup<?>) source).getPrefix());
        }
        cache.put(source, result);
        return result;
    }

    private static class SourcesIterator implements Iterator<ConfigurationPropertySource> {

        private final Deque<Iterator<PropertySource<?>>> iterators;

        private ConfigurationPropertySource next;

        private final Function<PropertySource<?>, ConfigurationPropertySource> adapter;

        SourcesIterator(Iterator<PropertySource<?>> iterator, Function<PropertySource<?>, ConfigurationPropertySource> adapter) {
            this.iterators = new ArrayDeque<>(4);
            this.iterators.push(iterator);
            this.adapter = adapter;
        }

        @Override
        public boolean hasNext() {
            return fetchNext() != null;
        }

        @Override
        public ConfigurationPropertySource next() {
            ConfigurationPropertySource next = fetchNext();
            if (next == null) {
                throw new NoSuchElementException();
            }
            this.next = null;
            return next;
        }

        private ConfigurationPropertySource fetchNext() {
            if (next == null) {
                if (iterators.isEmpty()) {
                    return null;
                }
                if (!iterators.peek().hasNext()) {
                    iterators.pop();
                    return fetchNext();
                }
                PropertySource<?> candidate = iterators.peek().next();
                if (candidate.getSource() instanceof ConfigurableEnvironment) {
                    push((ConfigurableEnvironment) candidate.getSource());
                    return fetchNext();
                }
                if (isIgnored(candidate)) {
                    return fetchNext();
                }
                next = adapter.apply(candidate);
            }
            return next;
        }

        private void push(ConfigurableEnvironment environment) {
            iterators.push(environment.getPropertySources().iterator());
        }

        private boolean isIgnored(PropertySource<?> candidate) {
            return candidate instanceof PropertySource.StubPropertySource || candidate instanceof ConfigurationPropertySourcesPropertySource;
        }
    }
}