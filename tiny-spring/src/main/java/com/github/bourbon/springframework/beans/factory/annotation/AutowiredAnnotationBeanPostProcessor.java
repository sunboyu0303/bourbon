package com.github.bourbon.springframework.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.TypeUtil;
import com.github.bourbon.base.utils.ClassUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.PropertyValues;
import com.github.bourbon.springframework.beans.factory.BeanFactory;
import com.github.bourbon.springframework.beans.factory.BeanFactoryAware;
import com.github.bourbon.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.github.bourbon.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.github.bourbon.springframework.core.convert.ConversionService;

import java.lang.reflect.Field;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/8 17:03
 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        Class<?> clazz = bean.getClass();

        for (Field field : (ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz).getDeclaredFields()) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (null != valueAnnotation) {
                setFieldValue(bean, field, valueAnnotation);
            }

            if (null != field.getAnnotation(Autowired.class)) {
                setFieldValue(bean, field);
            }
        }

        return pvs;
    }

    private void setFieldValue(Object bean, Field field, Value valueAnnotation) {
        Object value = beanFactory.resolveEmbeddedValue(valueAnnotation.value());
        ConversionService conversionService = beanFactory.getConversionService();
        if (conversionService != null) {
            Class<?> targetType = (Class<?>) TypeUtil.getType(field);
            if (conversionService.canConvert(value.getClass(), targetType)) {
                value = conversionService.convert(value, targetType);
            }
        }
        BeanUtil.setFieldValue(bean, field.getName(), value);
    }

    private void setFieldValue(Object bean, Field field) {
        BeanUtil.setFieldValue(bean, field.getName(), ObjectUtils.defaultSupplierIfNullElseFunction(field.getAnnotation(Qualifier.class), q -> beanFactory.getBean(q.value(), field.getType()), () -> beanFactory.getBean(field.getType())));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return null;
    }
}