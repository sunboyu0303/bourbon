package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.bind.*;
import com.github.bourbon.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
import com.github.bourbon.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import com.github.bourbon.springframework.boot.context.properties.bind.handler.NoUnboundElementsBindHandler;
import com.github.bourbon.springframework.boot.context.properties.bind.validation.ValidationBindHandler;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertySource;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertySources;
import com.github.bourbon.springframework.boot.context.properties.source.UnboundElementsSourceFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.PropertySources;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 19:34
 */
class ConfigurationPropertiesBinder {

    private static final String BEAN_NAME = "com.github.bourbon.springframework.boot.context.internalConfigurationPropertiesBinder";

    private static final String FACTORY_BEAN_NAME = "com.github.bourbon.springframework.boot.context.internalConfigurationPropertiesBinderFactory";

    private static final String VALIDATOR_BEAN_NAME = EnableConfigurationProperties.VALIDATOR_BEAN_NAME;

    private final ApplicationContext applicationContext;

    private final PropertySources propertySources;

    private final Validator configurationPropertiesValidator;

    private final boolean jsr303Present;

    private volatile Validator jsr303Validator;

    private volatile Binder binder;

    ConfigurationPropertiesBinder(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.propertySources = new PropertySourcesDeducer(applicationContext).getPropertySources();
        this.configurationPropertiesValidator = getConfigurationPropertiesValidator(applicationContext);
        this.jsr303Present = ConfigurationPropertiesJsr303Validator.isJsr303Present(applicationContext);
    }

    BindResult<?> bind(ConfigurationPropertiesBean propertiesBean) {
        Bindable<?> target = propertiesBean.asBindTarget();
        ConfigurationProperties annotation = propertiesBean.getAnnotation();
        return getBinder().bind(annotation.prefix(), target, getBindHandler(target, annotation));
    }

    Object bindOrCreate(ConfigurationPropertiesBean propertiesBean) {
        Bindable<?> target = propertiesBean.asBindTarget();
        ConfigurationProperties annotation = propertiesBean.getAnnotation();
        return getBinder().bindOrCreate(annotation.prefix(), target, getBindHandler(target, annotation));
    }

    private Validator getConfigurationPropertiesValidator(ApplicationContext ac) {
        return BooleanUtils.defaultIfFalse(ac.containsBean(VALIDATOR_BEAN_NAME), () -> ac.getBean(VALIDATOR_BEAN_NAME, Validator.class));
    }

    private <T> BindHandler getBindHandler(Bindable<T> target, ConfigurationProperties annotation) {
        List<Validator> validators = getValidators(target);
        BindHandler handler = getHandler();
        handler = new ConfigurationPropertiesBindHandler(handler);
        if (annotation.ignoreInvalidFields()) {
            handler = new IgnoreErrorsBindHandler(handler);
        }
        if (!annotation.ignoreUnknownFields()) {
            handler = new NoUnboundElementsBindHandler(handler, new UnboundElementsSourceFilter());
        }
        if (!validators.isEmpty()) {
            handler = new ValidationBindHandler(handler, validators.toArray(new Validator[0]));
        }
        for (ConfigurationPropertiesBindHandlerAdvisor advisor : getBindHandlerAdvisors()) {
            handler = advisor.apply(handler);
        }
        return handler;
    }

    private IgnoreTopLevelConverterNotFoundBindHandler getHandler() {
        return ObjectUtils.defaultSupplierIfNull(BoundConfigurationProperties.get(applicationContext),
                b -> new IgnoreTopLevelConverterNotFoundBindHandler(new BoundPropertiesTrackingBindHandler(b::add)),
                IgnoreTopLevelConverterNotFoundBindHandler::new
        );
    }

    private List<Validator> getValidators(Bindable<?> target) {
        List<Validator> validators = ListUtils.newArrayList(3);
        if (configurationPropertiesValidator != null) {
            validators.add(configurationPropertiesValidator);
        }
        if (jsr303Present && target.getAnnotation(Validated.class) != null) {
            validators.add(getJsr303Validator());
        }
        if (target.getValue() != null && target.getValue().get() instanceof Validator) {
            validators.add((Validator) target.getValue().get());
        }
        return validators;
    }

    private Validator getJsr303Validator() {
        return ObjectUtils.defaultSupplierIfNull(jsr303Validator, () -> {
            jsr303Validator = new ConfigurationPropertiesJsr303Validator(applicationContext);
            return jsr303Validator;
        });
    }

    private List<ConfigurationPropertiesBindHandlerAdvisor> getBindHandlerAdvisors() {
        return applicationContext.getBeanProvider(ConfigurationPropertiesBindHandlerAdvisor.class).orderedStream().collect(Collectors.toList());
    }

    private Binder getBinder() {
        return ObjectUtils.defaultSupplierIfNull(binder, () -> {
            binder = new Binder(getConfigurationPropertySources(), getPropertySourcesPlaceholdersResolver(), getConversionServices(), getPropertyEditorInitializer(), null, ConfigurationPropertiesBindConstructorProvider.INSTANCE);
            return binder;
        });
    }

    private Iterable<ConfigurationPropertySource> getConfigurationPropertySources() {
        return ConfigurationPropertySources.from(propertySources);
    }

    private PropertySourcesPlaceholdersResolver getPropertySourcesPlaceholdersResolver() {
        return new PropertySourcesPlaceholdersResolver(propertySources);
    }

    private List<ConversionService> getConversionServices() {
        return new ConversionServiceDeducer(applicationContext).getConversionServices();
    }

    private Consumer<PropertyEditorRegistry> getPropertyEditorInitializer() {
        return BooleanUtils.defaultIfAssignableFrom(applicationContext, ConfigurableApplicationContext.class, ac -> ((ConfigurableApplicationContext) ac).getBeanFactory()::copyRegisteredEditorsTo);
    }

    static void register(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(FACTORY_BEAN_NAME)) {
            registry.registerBeanDefinition(FACTORY_BEAN_NAME, BeanDefinitionBuilder.rootBeanDefinition(Factory.class).setRole(BeanDefinition.ROLE_INFRASTRUCTURE).getBeanDefinition());
        }

        if (!registry.containsBeanDefinition(BEAN_NAME)) {
            registry.registerBeanDefinition(BEAN_NAME, BeanDefinitionBuilder.rootBeanDefinition(ConfigurationPropertiesBinder.class, () -> ((BeanFactory) registry).getBean(FACTORY_BEAN_NAME, Factory.class).create()).setRole(BeanDefinition.ROLE_INFRASTRUCTURE).getBeanDefinition());
        }
    }

    static ConfigurationPropertiesBinder get(BeanFactory beanFactory) {
        return beanFactory.getBean(BEAN_NAME, ConfigurationPropertiesBinder.class);
    }

    static class Factory implements ApplicationContextAware {

        private ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        ConfigurationPropertiesBinder create() {
            return new ConfigurationPropertiesBinder(applicationContext);
        }
    }

    private static class ConfigurationPropertiesBindHandler extends AbstractBindHandler {

        ConfigurationPropertiesBindHandler(BindHandler handler) {
            super(handler);
        }

        @Override
        public <T> Bindable<T> onStart(ConfigurationPropertyName name, Bindable<T> target, BindContext context) {
            return BooleanUtils.defaultIfFalse(isConfigurationProperties(target.getType().resolve()), () -> target.withBindRestrictions(Bindable.BindRestriction.NO_DIRECT_PROPERTY), target);
        }

        private boolean isConfigurationProperties(Class<?> target) {
            return ObjectUtils.defaultIfNull(target, clazz -> MergedAnnotations.from(clazz).isPresent(ConfigurationProperties.class), false);
        }
    }
}