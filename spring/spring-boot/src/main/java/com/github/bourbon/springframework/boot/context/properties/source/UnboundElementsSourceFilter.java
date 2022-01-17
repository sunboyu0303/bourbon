package com.github.bourbon.springframework.boot.context.properties.source;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.base.utils.function.BooleanFunc;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 17:13
 */
public class UnboundElementsSourceFilter implements BooleanFunc<ConfigurationPropertySource> {

    private static final Set<String> BENIGN_PROPERTY_SOURCE_NAMES = SetUtils.unmodifiableSet(
            SetUtils.newHashSet(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME)
    );

    @Override
    public boolean apply(ConfigurationPropertySource configurationPropertySource) {
        return BooleanUtils.defaultIfAssignableFrom(configurationPropertySource.getUnderlyingSource(), PropertySource.class, s -> !BENIGN_PROPERTY_SOURCE_NAMES.contains(((PropertySource<?>) s).getName()), true);
    }
}