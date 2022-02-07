package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySources;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 17:58
 */
class PropertySourcesDeducer {

    private final ApplicationContext applicationContext;

    PropertySourcesDeducer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    PropertySources getPropertySources() {
        return ObjectUtils.defaultSupplierIfNullElseFunction(getSinglePropertySourcesPlaceholderConfigurer(), PropertySourcesPlaceholderConfigurer::getAppliedPropertySources,
                () -> ObjectUtils.requireNonNull(extractEnvironmentPropertySources(), "Unable to obtain PropertySources from PropertySourcesPlaceholderConfigurer or Environment")
        );
    }

    private PropertySourcesPlaceholderConfigurer getSinglePropertySourcesPlaceholderConfigurer() {
        return BooleanUtils.defaultIfPredicate(applicationContext.getBeansOfType(PropertySourcesPlaceholderConfigurer.class, false, false),
                beans -> beans.size() == 1, beans -> beans.values().iterator().next()
        );
    }

    private MutablePropertySources extractEnvironmentPropertySources() {
        return BooleanUtils.defaultIfAssignableFrom(applicationContext.getEnvironment(), ConfigurableEnvironment.class, e -> ((ConfigurableEnvironment) e).getPropertySources());
    }
}