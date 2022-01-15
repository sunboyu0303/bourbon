package com.github.bourbon.springframework.boot.context.properties.bind;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationProperty;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/25 23:57
 */
public class UnboundConfigurationPropertiesException extends RuntimeException {

    private static final long serialVersionUID = 1065774180788999549L;

    private final transient Set<ConfigurationProperty> unboundProperties;

    public UnboundConfigurationPropertiesException(Set<ConfigurationProperty> unboundProperties) {
        super(buildMessage(unboundProperties));
        this.unboundProperties = SetUtils.unmodifiableSet(unboundProperties);
    }

    public Set<ConfigurationProperty> getUnboundProperties() {
        return unboundProperties;
    }

    private static String buildMessage(Set<ConfigurationProperty> unboundProperties) {
        StringBuilder builder = new StringBuilder();
        builder.append("The elements [");
        String message = unboundProperties.stream().map(p -> p.getName().toString()).collect(Collectors.joining(StringConstants.COMMA));
        builder.append(message).append("] were left unbound.");
        return builder.toString();
    }
}