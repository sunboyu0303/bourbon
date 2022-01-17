package com.github.bourbon.springframework.boot.autoconfigure;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.*;
import com.github.bourbon.springframework.boot.context.properties.bind.Binder;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import com.github.bourbon.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 00:54
 */
public class AutoConfigurationImportSelector implements DeferredImportSelector, BeanClassLoaderAware, ResourceLoaderAware, BeanFactoryAware, EnvironmentAware, Ordered {

    private static final AutoConfigurationEntry EMPTY_ENTRY = new AutoConfigurationEntry();

    private static final String[] NO_IMPORTS = {};

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoConfigurationImportSelector.class);

    private static final String PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE = "spring.autoconfigure.exclude";

    private ConfigurableListableBeanFactory beanFactory;

    private Environment environment;

    private ClassLoader beanClassLoader;

    private ResourceLoader resourceLoader;

    private ConfigurationClassFilter configurationClassFilter;

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return BooleanUtils.defaultIfPredicate(annotationMetadata, this::isEnabled, m -> CollectionUtils.toStringArray(getAutoConfigurationEntry(m).getConfigurations()), NO_IMPORTS);
    }

    @Override
    public Predicate<String> getExclusionFilter() {
        return this::shouldExclude;
    }

    private boolean shouldExclude(String configurationClassName) {
        return getConfigurationClassFilter().filter(ListUtils.newArrayList(configurationClassName)).isEmpty();
    }

    protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
        return BooleanUtils.defaultIfPredicate(annotationMetadata, this::isEnabled, m -> {
            AnnotationAttributes attributes = getAttributes(m);
            List<String> configurations = removeDuplicates(getCandidateConfigurations(m, attributes));
            Set<String> exclusions = getExclusions(m, attributes);
            checkExcludedClasses(configurations, exclusions);
            configurations.removeAll(exclusions);
            configurations = getConfigurationClassFilter().filter(configurations);
            fireAutoConfigurationImportEvents(configurations, exclusions);
            return new AutoConfigurationEntry(configurations, exclusions);
        }, EMPTY_ENTRY);
    }

    @Override
    public Class<? extends Group> getImportGroup() {
        return AutoConfigurationGroup.class;
    }

    protected boolean isEnabled(AnnotationMetadata metadata) {
        return BooleanUtils.defaultIfFalse(getClass() == AutoConfigurationImportSelector.class, () -> getEnvironment().getProperty(EnableAutoConfiguration.ENABLED_OVERRIDE_PROPERTY, Boolean.class, true), true);
    }

    protected AnnotationAttributes getAttributes(AnnotationMetadata metadata) {
        Class<? extends Annotation> clazz = getAnnotationClass();
        AnnotationAttributes attributes = AnnotationHelperUtils.getAnnotationAttributes(metadata, clazz, true);
        Assert.notNull(attributes, () -> "No auto-configuration attributes found. Is " + metadata.getClassName() + " annotated with " + ClassUtils.getSimpleClassName(clazz) + StringConstants.QUESTION_MASK);
        return attributes;
    }

    protected Class<? extends Annotation> getAnnotationClass() {
        return EnableAutoConfiguration.class;
    }

    protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
        List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(), getBeanClassLoader());
        Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/bourbon/spring.factories. If you are using a custom packaging, make sure that file is correct.");
        return configurations;
    }

    protected Class<?> getSpringFactoriesLoaderFactoryClass() {
        return EnableAutoConfiguration.class;
    }

    private void checkExcludedClasses(List<String> configurations, Set<String> exclusions) {
        List<String> invalidExcludes = exclusions.stream().filter(e -> ClassUtils.isPresent(e, getClass().getClassLoader()) && !configurations.contains(e)).collect(Collectors.toList());
        if (!invalidExcludes.isEmpty()) {
            handleInvalidExcludes(invalidExcludes);
        }
    }

    protected void handleInvalidExcludes(List<String> invalidExcludes) {
        StringBuilder message = new StringBuilder();
        for (String exclude : invalidExcludes) {
            message.append("\t- ").append(exclude).append(String.format("%n"));
        }
        throw new IllegalStateException(String.format("The following classes could not be excluded because they are not auto-configuration classes:%n%s", message));
    }

    protected Set<String> getExclusions(AnnotationMetadata metadata, AnnotationAttributes attributes) {
        Set<String> excluded = new LinkedHashSet<>();
        excluded.addAll(SetUtils.newHashSet(attributes.getStringArray("exclude")));
        excluded.addAll(SetUtils.newHashSet(attributes.getStringArray("excludeName")));
        excluded.addAll(getExcludeAutoConfigurationsProperty());
        return excluded;
    }

    protected List<String> getExcludeAutoConfigurationsProperty() {
        return ObjectUtils.defaultSupplierIfNull(getEnvironment(), environment -> BooleanUtils.defaultSupplierIfAssignableFrom(environment, ConfigurableEnvironment.class,
                e -> Binder.get(e).bind(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, String[].class).map(ListUtils::newArrayList).orElse(Collections.emptyList()),
                () -> ObjectUtils.defaultSupplierIfNull(environment.getProperty(PROPERTY_NAME_AUTOCONFIGURE_EXCLUDE, String[].class), ListUtils::newArrayList, Collections::emptyList)
        ), Collections::emptyList);
    }

    protected List<AutoConfigurationImportFilter> getAutoConfigurationImportFilters() {
        return SpringFactoriesLoader.loadFactories(AutoConfigurationImportFilter.class, beanClassLoader);
    }

    private ConfigurationClassFilter getConfigurationClassFilter() {
        return ObjectUtils.defaultSupplierIfNull(configurationClassFilter, () -> {
            List<AutoConfigurationImportFilter> filters = getAutoConfigurationImportFilters();
            for (AutoConfigurationImportFilter filter : filters) {
                invokeAwareMethods(filter);
            }
            configurationClassFilter = new ConfigurationClassFilter(beanClassLoader, filters);
            return configurationClassFilter;
        });
    }

    protected final <T> List<T> removeDuplicates(List<T> list) {
        return ListUtils.newArrayList(SetUtils.newLinkedHashSet(list));
    }

    protected final List<String> asList(AnnotationAttributes attributes, String name) {
        return ListUtils.newArrayList(attributes.getStringArray(name));
    }

    private void fireAutoConfigurationImportEvents(List<String> configurations, Set<String> exclusions) {
        List<AutoConfigurationImportListener> listeners = getAutoConfigurationImportListeners();
        if (!listeners.isEmpty()) {
            AutoConfigurationImportEvent event = new AutoConfigurationImportEvent(this, configurations, exclusions);
            for (AutoConfigurationImportListener listener : listeners) {
                invokeAwareMethods(listener);
                listener.onAutoConfigurationImportEvent(event);
            }
        }
    }

    protected List<AutoConfigurationImportListener> getAutoConfigurationImportListeners() {
        return SpringFactoriesLoader.loadFactories(AutoConfigurationImportListener.class, beanClassLoader);
    }

    private void invokeAwareMethods(Object instance) {
        if (instance instanceof Aware) {
            if (instance instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) instance).setBeanClassLoader(beanClassLoader);
            }
            if (instance instanceof BeanFactoryAware) {
                ((BeanFactoryAware) instance).setBeanFactory(beanFactory);
            }
            if (instance instanceof EnvironmentAware) {
                ((EnvironmentAware) instance).setEnvironment(environment);
            }
            if (instance instanceof ResourceLoaderAware) {
                ((ResourceLoaderAware) instance).setResourceLoader(resourceLoader);
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        Assert.isInstanceOf(ConfigurableListableBeanFactory.class, beanFactory);
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    protected final ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    protected ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    protected final Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    protected final ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }

    private static class ConfigurationClassFilter {

        private final AutoConfigurationMetadata autoConfigurationMetadata;

        private final List<AutoConfigurationImportFilter> filters;

        ConfigurationClassFilter(ClassLoader classLoader, List<AutoConfigurationImportFilter> filters) {
            autoConfigurationMetadata = AutoConfigurationMetadataLoader.loadMetadata(classLoader);
            this.filters = filters;
        }

        List<String> filter(List<String> configurations) {
            long startTime = Clock.currentTimeMillis();
            String[] candidates = CollectionUtils.toStringArray(configurations);
            boolean skipped = false;
            for (AutoConfigurationImportFilter filter : filters) {
                boolean[] match = filter.match(candidates, autoConfigurationMetadata);
                for (int i = 0; i < match.length; i++) {
                    if (!match[i]) {
                        candidates[i] = null;
                        skipped = true;
                    }
                }
            }
            return BooleanUtils.defaultIfFalse(skipped, () -> {
                List<String> result = Arrays.stream(candidates).filter(ObjectUtils::nonNull).collect(Collectors.toList());
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Filtered " + (configurations.size() - result.size()) + " auto configuration class in " + (Clock.currentTimeMillis() - startTime) + " ms");
                }
                return result;
            }, configurations);
        }
    }

    private static class AutoConfigurationGroup implements DeferredImportSelector.Group, BeanClassLoaderAware, BeanFactoryAware, ResourceLoaderAware {

        private final Map<String, AnnotationMetadata> entries = new LinkedHashMap<>();

        private final List<AutoConfigurationEntry> autoConfigurationEntries = new ArrayList<>();

        private ClassLoader beanClassLoader;

        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;

        private AutoConfigurationMetadata autoConfigurationMetadata;

        @Override
        public void setBeanClassLoader(ClassLoader classLoader) {
            this.beanClassLoader = classLoader;
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }

        @Override
        public void process(AnnotationMetadata annotationMetadata, DeferredImportSelector deferredImportSelector) {

            Assert.isTrue(deferredImportSelector instanceof AutoConfigurationImportSelector, () -> String.format("Only %s implementations are supported, got %s", AutoConfigurationImportSelector.class.getSimpleName(), deferredImportSelector.getClass().getName()));

            AutoConfigurationEntry autoConfigurationEntry = ((AutoConfigurationImportSelector) deferredImportSelector).getAutoConfigurationEntry(annotationMetadata);

            autoConfigurationEntries.add(autoConfigurationEntry);

            for (String importClassName : autoConfigurationEntry.getConfigurations()) {
                entries.putIfAbsent(importClassName, annotationMetadata);
            }
        }

        @Override
        public Iterable<Entry> selectImports() {
            return BooleanUtils.defaultSupplierIfFalse(!autoConfigurationEntries.isEmpty(), () -> {
                Set<String> allExclusions = autoConfigurationEntries.stream().map(AutoConfigurationEntry::getExclusions).flatMap(Collection::stream).collect(Collectors.toSet());
                Set<String> processedConfigurations = autoConfigurationEntries.stream().map(AutoConfigurationEntry::getConfigurations).flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
                processedConfigurations.removeAll(allExclusions);
                return sortAutoConfigurations(processedConfigurations, getAutoConfigurationMetadata()).stream().map(n -> new Entry(entries.get(n), n)).collect(Collectors.toList());
            }, Collections::emptyList);
        }

        private AutoConfigurationMetadata getAutoConfigurationMetadata() {
            return ObjectUtils.defaultSupplierIfNull(autoConfigurationMetadata, () -> {
                autoConfigurationMetadata = AutoConfigurationMetadataLoader.loadMetadata(beanClassLoader);
                return autoConfigurationMetadata;
            });
        }

        private List<String> sortAutoConfigurations(Set<String> configurations, AutoConfigurationMetadata autoConfigurationMetadata) {
            return new AutoConfigurationSorter(getMetadataReaderFactory(), autoConfigurationMetadata).getInPriorityOrder(configurations);
        }

        private MetadataReaderFactory getMetadataReaderFactory() {
            try {
                return beanFactory.getBean(SharedMetadataReaderFactoryContextInitializer.BEAN_NAME, MetadataReaderFactory.class);
            } catch (NoSuchBeanDefinitionException ex) {
                return new CachingMetadataReaderFactory(resourceLoader);
            }
        }
    }

    protected static class AutoConfigurationEntry {

        private final List<String> configurations;

        private final Set<String> exclusions;

        private AutoConfigurationEntry() {
            configurations = Collections.emptyList();
            exclusions = Collections.emptySet();
        }

        AutoConfigurationEntry(Collection<String> configurations, Collection<String> exclusions) {
            this.configurations = ListUtils.newArrayList(configurations);
            this.exclusions = SetUtils.newHashSet(exclusions);
        }

        public List<String> getConfigurations() {
            return configurations;
        }

        public Set<String> getExclusions() {
            return exclusions;
        }
    }
}