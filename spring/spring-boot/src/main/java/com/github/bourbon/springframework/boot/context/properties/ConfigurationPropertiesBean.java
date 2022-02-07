package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.MapUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.boot.context.properties.bind.Bindable;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 17:10
 */
public final class ConfigurationPropertiesBean {

    private final String name;

    private final Object instance;

    private final ConfigurationProperties annotation;

    private final Bindable<?> bindTarget;

    private final BindMethod bindMethod;

    private ConfigurationPropertiesBean(String name, Object instance, ConfigurationProperties annotation, Bindable<?> bindTarget) {
        this.name = name;
        this.instance = instance;
        this.annotation = annotation;
        this.bindTarget = bindTarget;
        this.bindMethod = BindMethod.forType(bindTarget.getType().resolve());
    }

    public String getName() {
        return name;
    }

    public Object getInstance() {
        return instance;
    }

    Class<?> getType() {
        return bindTarget.getType().resolve();
    }

    public BindMethod getBindMethod() {
        return bindMethod;
    }

    public ConfigurationProperties getAnnotation() {
        return annotation;
    }

    public Bindable<?> asBindTarget() {
        return bindTarget;
    }

    public static Map<String, ConfigurationPropertiesBean> getAll(ApplicationContext applicationContext) {
        Assert.notNull(applicationContext, "ApplicationContext must not be null");
        return BooleanUtils.defaultSupplierIfAssignableFrom(applicationContext, ConfigurableApplicationContext.class, ac -> getAll((ConfigurableApplicationContext) ac),
                () -> applicationContext.getBeansWithAnnotation(ConfigurationProperties.class).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> get(applicationContext, entry.getValue(), entry.getKey())))
        );
    }

    private static Map<String, ConfigurationPropertiesBean> getAll(ConfigurableApplicationContext applicationContext) {
        Map<String, ConfigurationPropertiesBean> propertiesBeans = MapUtils.newLinkedHashMap();
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        Iterator<String> beanNames = beanFactory.getBeanNamesIterator();
        while (beanNames.hasNext()) {
            String beanName = beanNames.next();
            if (isConfigurationPropertiesBean(beanFactory, beanName)) {
                try {
                    propertiesBeans.put(beanName, get(applicationContext, beanFactory.getBean(beanName), beanName));
                } catch (Exception ex) {
                    // ignore
                }
            }
        }
        return propertiesBeans;
    }

    private static boolean isConfigurationPropertiesBean(ConfigurableListableBeanFactory beanFactory, String beanName) {
        try {
            return BooleanUtils.defaultIfFalse(!beanFactory.getBeanDefinition(beanName).isAbstract(), () -> BooleanUtils.defaultIfFalse(
                    beanFactory.findAnnotationOnBean(beanName, ConfigurationProperties.class) == null,
                    () -> findMergedAnnotation(findFactoryMethod(beanFactory, beanName), ConfigurationProperties.class).isPresent(),
                    true
            ), false);
        } catch (NoSuchBeanDefinitionException ex) {
            return false;
        }
    }

    public static ConfigurationPropertiesBean get(ApplicationContext applicationContext, Object bean, String beanName) {
        return create(beanName, bean, bean.getClass(), findFactoryMethod(applicationContext, beanName));
    }

    private static Method findFactoryMethod(ApplicationContext applicationContext, String beanName) {
        return BooleanUtils.defaultIfAssignableFrom(applicationContext, ConfigurableApplicationContext.class, ac -> findFactoryMethod((ConfigurableApplicationContext) ac, beanName));
    }

    private static Method findFactoryMethod(ConfigurableApplicationContext applicationContext, String beanName) {
        return findFactoryMethod(applicationContext.getBeanFactory(), beanName);
    }

    private static Method findFactoryMethod(ConfigurableListableBeanFactory beanFactory, String beanName) {
        return BooleanUtils.defaultIfFalse(beanFactory.containsBeanDefinition(beanName), () -> {
            BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
            if (beanDefinition instanceof RootBeanDefinition) {
                Method resolvedFactoryMethod = ((RootBeanDefinition) beanDefinition).getResolvedFactoryMethod();
                if (resolvedFactoryMethod != null) {
                    return resolvedFactoryMethod;
                }
            }
            return findFactoryMethodUsingReflection(beanFactory, beanDefinition);
        });
    }

    private static Method findFactoryMethodUsingReflection(ConfigurableListableBeanFactory beanFactory, BeanDefinition beanDefinition) {

        String factoryMethodName = beanDefinition.getFactoryMethodName();
        String factoryBeanName = beanDefinition.getFactoryBeanName();
        if (factoryMethodName == null || factoryBeanName == null) {
            return null;
        }

        Class<?> factoryType = beanFactory.getType(factoryBeanName);
        if (factoryType.getName().contains(StringConstants.DOLLARS)) {
            factoryType = factoryType.getSuperclass();
        }

        AtomicReference<Method> factoryMethod = new AtomicReference<>();
        ReflectionUtils.doWithMethods(factoryType, method -> {
            if (method.getName().equals(factoryMethodName)) {
                factoryMethod.set(method);
            }
        });
        return factoryMethod.get();
    }

    static ConfigurationPropertiesBean forValueObject(Class<?> beanClass, String beanName) {
        ConfigurationPropertiesBean propertiesBean = create(beanName, null, beanClass, null);
        Assert.isTrue(propertiesBean != null && propertiesBean.getBindMethod() == BindMethod.VALUE_OBJECT,
                () -> "Bean '" + beanName + "' is not a @ConfigurationProperties value object");
        return propertiesBean;
    }

    private static ConfigurationPropertiesBean create(String name, Object instance, Class<?> type, Method factory) {
        return ObjectUtils.defaultIfNullElseFunction(findAnnotation(instance, type, factory, ConfigurationProperties.class), a -> {
            Bindable<Object> bindTarget = Bindable.of(ObjectUtils.defaultSupplierIfNullElseFunction(factory, ResolvableType::forMethodReturnType, () -> ResolvableType.forClass(type)))
                    .withAnnotations(ObjectUtils.defaultSupplierIfNullElseFunction(findAnnotation(instance, type, factory, Validated.class), v -> new Annotation[]{a, v}, () -> new Annotation[]{a}));
            if (instance != null) {
                bindTarget = bindTarget.withExistingValue(instance);
            }
            return new ConfigurationPropertiesBean(name, instance, a, bindTarget);
        });
    }

    private static <A extends Annotation> A findAnnotation(Object instance, Class<?> type, Method factory, Class<A> annotationType) {
        MergedAnnotation<A> annotation = ObjectUtils.defaultIfNullElseFunction(factory, f -> findMergedAnnotation(f, annotationType), MergedAnnotation.missing());

        if (!annotation.isPresent()) {
            annotation = findMergedAnnotation(type, annotationType);
        }
        if (!annotation.isPresent() && AopUtils.isAopProxy(instance)) {
            annotation = MergedAnnotations.from(AopUtils.getTargetClass(instance), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(annotationType);
        }
        return annotation.isPresent() ? annotation.synthesize() : null;
    }

    private static <A extends Annotation> MergedAnnotation<A> findMergedAnnotation(AnnotatedElement element, Class<A> annotationType) {
        return ObjectUtils.defaultIfNullElseFunction(element, e -> MergedAnnotations.from(e, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(annotationType), MergedAnnotation.missing());
    }

    public enum BindMethod {
        JAVA_BEAN, VALUE_OBJECT;

        static BindMethod forType(Class<?> type) {
            return BooleanUtils.defaultIfFalse(ConfigurationPropertiesBindConstructorProvider.INSTANCE.getBindConstructor(type, false) != null, VALUE_OBJECT, JAVA_BEAN);
        }
    }
}