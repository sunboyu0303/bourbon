package com.github.bourbon.springframework.context.annotation;

import com.github.bourbon.base.utils.BooleanUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 18:34
 */
public final class SpringContextUtils {

    private static final AtomicBoolean initialized = new AtomicBoolean();

    private static ConfigurableListableBeanFactory beanFactory;

    static void init(BeanDefinitionRegistry registry) {
        if (initialized.compareAndSet(false, true)) {
            beanFactory = BooleanUtils.defaultSupplierIfAssignableFrom(registry, ConfigurableListableBeanFactory.class, ConfigurableListableBeanFactory.class::cast,
                    () -> BooleanUtils.defaultSupplierIfAssignableFrom(registry, ConfigurableApplicationContext.class, r -> ((ConfigurableApplicationContext) r).getBeanFactory())
            );
        }
    }

    private SpringContextUtils() {
    }
}