package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.springframework.boot.context.properties.source.*;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.env.Environment;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 14:58
 */
public class Binder {

    private static final Set<Class<?>> NON_BEAN_CLASSES = SetUtils.unmodifiableSet(SetUtils.newHashSet(Object.class, Class.class));

    private final Iterable<ConfigurationPropertySource> sources;

    private final PlaceholdersResolver placeholdersResolver;

    private final BindConverter bindConverter;

    private final BindHandler defaultBindHandler;

    private final List<DataObjectBinder> dataObjectBinders;

    public Binder(ConfigurationPropertySource... sources) {
        this(ListUtils.newArrayList(sources), null, null, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources) {
        this(sources, null, null, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources, PlaceholdersResolver resolver) {
        this(sources, resolver, null, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources, PlaceholdersResolver resolver, ConversionService service) {
        this(sources, resolver, service, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources, PlaceholdersResolver resolver, ConversionService service, Consumer<PropertyEditorRegistry> consumer) {
        this(sources, resolver, service, consumer, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources, PlaceholdersResolver resolver, ConversionService service, Consumer<PropertyEditorRegistry> consumer, BindHandler handler) {
        this(sources, resolver, service, consumer, handler, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources, PlaceholdersResolver resolver, ConversionService service, Consumer<PropertyEditorRegistry> consumer, BindHandler handler, BindConstructorProvider provider) {
        this(sources, resolver, ObjectUtils.defaultIfNull(service, ListUtils::newArrayList, (List<ConversionService>) null), consumer, handler, provider);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources, PlaceholdersResolver resolver, List<ConversionService> services, Consumer<PropertyEditorRegistry> consumer, BindHandler handler, BindConstructorProvider provider) {
        this.sources = ObjectUtils.requireNonNull(sources, "Sources must not be null");
        this.placeholdersResolver = ObjectUtils.defaultIfNull(resolver, PlaceholdersResolver.NONE);
        this.bindConverter = BindConverter.get(services, consumer);
        this.defaultBindHandler = ObjectUtils.defaultIfNull(handler, BindHandler.DEFAULT);
        this.dataObjectBinders = ListUtils.unmodifiableList(ListUtils.newArrayList(new ValueObjectBinder(ObjectUtils.defaultIfNull(provider, BindConstructorProvider.DEFAULT)), JavaBeanBinder.INSTANCE));
    }

    public <T> BindResult<T> bind(String name, Class<T> target) {
        return bind(name, Bindable.of(target));
    }

    public <T> BindResult<T> bind(String name, Bindable<T> target) {
        return bind(name, target, null);
    }

    public <T> BindResult<T> bind(ConfigurationPropertyName name, Class<T> target) {
        return bind(name, Bindable.of(target));
    }

    public <T> BindResult<T> bind(ConfigurationPropertyName name, Bindable<T> target) {
        return bind(name, target, null);
    }

    public <T> BindResult<T> bind(String name, Bindable<T> target, BindHandler handler) {
        return bind(ConfigurationPropertyName.of(name), target, handler);
    }

    public <T> BindResult<T> bind(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler) {
        return BindResult.of(bind(name, target, handler, false));
    }

    public <T> T bindOrCreate(String name, Class<T> target) {
        return bindOrCreate(name, Bindable.of(target));
    }


    public <T> T bindOrCreate(String name, Bindable<T> target) {
        return bindOrCreate(name, target, null);
    }

    public <T> T bindOrCreate(String name, Bindable<T> target, BindHandler handler) {
        return bindOrCreate(ConfigurationPropertyName.of(name), target, handler);
    }

    public <T> T bindOrCreate(ConfigurationPropertyName name, Class<T> target) {
        return bindOrCreate(name, Bindable.of(target));
    }

    public <T> T bindOrCreate(ConfigurationPropertyName name, Bindable<T> target) {
        return bindOrCreate(name, target, null);
    }

    public <T> T bindOrCreate(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler) {
        return bind(name, target, handler, true);
    }

    private <T> T bind(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, boolean create) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(target, "Target must not be null");
        return bind(name, target, ObjectUtils.defaultIfNull(handler, defaultBindHandler), new Context(), false, create);
    }

    private <T> T bind(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context, boolean allowRecursiveBinding, boolean create) {
        try {
            Bindable<T> replacementTarget = handler.onStart(name, target, context);
            if (replacementTarget == null) {
                return handleBindResult(name, target, handler, context, null, create);
            }
            target = replacementTarget;
            return handleBindResult(name, target, handler, context, bindObject(name, target, handler, context, allowRecursiveBinding), create);
        } catch (Exception ex) {
            return handleBindError(name, target, handler, context, ex);
        }
    }

    private <T> T handleBindResult(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context, Object result, boolean create) throws Exception {
        if (result != null) {
            result = handler.onSuccess(name, target, context, result);
            result = context.getConverter().convert(result, target);
        }
        if (result == null && create) {
            result = create(target, context);
            result = handler.onCreate(name, target, context, result);
            result = context.getConverter().convert(result, target);
            Assert.notNull(result, () -> "Unable to create instance for " + target.getType());
        }
        handler.onFinish(name, target, context, result);
        return context.getConverter().convert(result, target);
    }

    private Object create(Bindable<?> target, Context context) {
        for (DataObjectBinder dataObjectBinder : dataObjectBinders) {
            Object instance = dataObjectBinder.create(target, context);
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }

    private <T> T handleBindError(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context, Exception error) {
        try {
            return context.getConverter().convert(handler.onFailure(name, target, context, error), target);
        } catch (BindException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BindException(name, target, context.getConfigurationProperty(), ex);
        }
    }

    private <T> Object bindObject(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context, boolean allowRecursiveBinding) {
        ConfigurationProperty property = findProperty(name, target, context);
        if (property == null && context.depth != 0 && containsNoDescendantOf(context.getSources(), name)) {
            return null;
        }
        AggregateBinder<?> aggregateBinder = getAggregateBinder(target, context);
        if (aggregateBinder != null) {
            return bindAggregate(name, target, handler, context, aggregateBinder);
        }
        return ObjectUtils.defaultSupplierIfNull(property, p -> {
            try {
                return bindProperty(target, context, p);
            } catch (ConverterNotFoundException ex) {
                return ObjectUtils.requireNonNull(bindDataObject(name, target, handler, context, allowRecursiveBinding), () -> ex);
            }
        }, () -> bindDataObject(name, target, handler, context, allowRecursiveBinding));
    }

    private AggregateBinder<?> getAggregateBinder(Bindable<?> target, Context context) {
        Class<?> resolvedType = target.getType().resolve(Object.class);
        if (Map.class.isAssignableFrom(resolvedType)) {
            return new MapBinder(context);
        }
        if (Collection.class.isAssignableFrom(resolvedType)) {
            return new CollectionBinder(context);
        }
        if (target.getType().isArray()) {
            return new ArrayBinder(context);
        }
        return null;
    }

    private <T> Object bindAggregate(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context, AggregateBinder<?> aggregateBinder) {
        AggregateElementBinder elementBinder = (itemName, itemTarget, source) -> {
            boolean allow = aggregateBinder.isAllowRecursiveBinding(source);
            return context.withSource(source, () -> bind(itemName, itemTarget, handler, context, allow, false));
        };
        return context.withIncreasedDepth(() -> aggregateBinder.bind(name, target, elementBinder));
    }

    private <T> ConfigurationProperty findProperty(ConfigurationPropertyName name, Bindable<T> target, Context context) {
        if (name.isEmpty() || target.hasBindRestriction(Bindable.BindRestriction.NO_DIRECT_PROPERTY)) {
            return null;
        }
        for (ConfigurationPropertySource source : context.getSources()) {
            ConfigurationProperty property = source.getConfigurationProperty(name);
            if (property != null) {
                return property;
            }
        }
        return null;
    }

    private <T> Object bindProperty(Bindable<T> target, Context context, ConfigurationProperty property) {
        context.setConfigurationProperty(property);
        return context.getConverter().convert(placeholdersResolver.resolvePlaceholders(property.getValue()), target);
    }

    private Object bindDataObject(ConfigurationPropertyName name, Bindable<?> target, BindHandler handler, Context context, boolean allowRecursiveBinding) {
        if (isUnbindableBean(name, target, context)) {
            return null;
        }
        Class<?> type = target.getType().resolve(Object.class);
        if (!allowRecursiveBinding && context.isBindingDataObject(type)) {
            return null;
        }
        DataObjectPropertyBinder propertyBinder = (propertyName, propertyTarget) -> bind(name.append(propertyName), propertyTarget, handler, context, false, false);
        return context.withDataObject(type, () -> {
            for (DataObjectBinder dataObjectBinder : dataObjectBinders) {
                Object instance = dataObjectBinder.bind(name, target, context, propertyBinder);
                if (instance != null) {
                    return instance;
                }
            }
            return null;
        });
    }

    private boolean isUnbindableBean(ConfigurationPropertyName name, Bindable<?> target, Context context) {
        for (ConfigurationPropertySource source : context.getSources()) {
            if (source.containsDescendantOf(name) == ConfigurationPropertyState.PRESENT) {
                return false;
            }
        }
        Class<?> resolved = target.getType().resolve(Object.class);
        if (resolved.isPrimitive() || NON_BEAN_CLASSES.contains(resolved)) {
            return true;
        }
        return resolved.getName().startsWith("java.");
    }

    private boolean containsNoDescendantOf(Iterable<ConfigurationPropertySource> sources, ConfigurationPropertyName name) {
        for (ConfigurationPropertySource source : sources) {
            if (source.containsDescendantOf(name) != ConfigurationPropertyState.ABSENT) {
                return false;
            }
        }
        return true;
    }

    public static Binder get(Environment environment) {
        return get(environment, null);
    }

    public static Binder get(Environment environment, BindHandler defaultBindHandler) {
        return new Binder(ConfigurationPropertySources.get(environment), new PropertySourcesPlaceholdersResolver(environment), null, null, defaultBindHandler);
    }

    final class Context implements BindContext {

        private int depth;

        private final List<ConfigurationPropertySource> source = ListUtils.newArrayList((ConfigurationPropertySource) null);

        private int sourcePushCount;

        private final Deque<Class<?>> dataObjectBindings = new ArrayDeque<>();

        private final Deque<Class<?>> constructorBindings = new ArrayDeque<>();

        private ConfigurationProperty configurationProperty;

        private void increaseDepth() {
            depth++;
        }

        private void decreaseDepth() {
            depth--;
        }

        private <T> T withSource(ConfigurationPropertySource source, Supplier<T> supplier) {
            if (source == null) {
                return supplier.get();
            }
            this.source.set(0, source);
            this.sourcePushCount++;
            try {
                return supplier.get();
            } finally {
                this.sourcePushCount--;
            }
        }

        private <T> T withDataObject(Class<?> type, Supplier<T> supplier) {
            dataObjectBindings.push(type);
            try {
                return withIncreasedDepth(supplier);
            } finally {
                dataObjectBindings.pop();
            }
        }

        private boolean isBindingDataObject(Class<?> type) {
            return dataObjectBindings.contains(type);
        }

        private <T> T withIncreasedDepth(Supplier<T> supplier) {
            increaseDepth();
            try {
                return supplier.get();
            } finally {
                decreaseDepth();
            }
        }

        void setConfigurationProperty(ConfigurationProperty configurationProperty) {
            this.configurationProperty = configurationProperty;
        }

        void clearConfigurationProperty() {
            configurationProperty = null;
        }

        void pushConstructorBoundTypes(Class<?> value) {
            constructorBindings.push(value);
        }

        boolean isNestedConstructorBinding() {
            return !constructorBindings.isEmpty();
        }

        void popConstructorBoundTypes() {
            constructorBindings.pop();
        }

        PlaceholdersResolver getPlaceholdersResolver() {
            return Binder.this.placeholdersResolver;
        }

        BindConverter getConverter() {
            return Binder.this.bindConverter;
        }

        @Override
        public Binder getBinder() {
            return Binder.this;
        }

        @Override
        public int getDepth() {
            return depth;
        }

        @Override
        public Iterable<ConfigurationPropertySource> getSources() {
            return BooleanUtils.defaultIfFalse(sourcePushCount > 0, source, Binder.this.sources);
        }

        @Override
        public ConfigurationProperty getConfigurationProperty() {
            return configurationProperty;
        }
    }
}