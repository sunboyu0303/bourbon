package com.github.bourbon.springframework.boot.context.properties.bind.handler;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.base.utils.function.BooleanFunc;
import com.github.bourbon.springframework.boot.context.properties.bind.*;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationProperty;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertyName;
import com.github.bourbon.springframework.boot.context.properties.source.ConfigurationPropertySource;
import com.github.bourbon.springframework.boot.context.properties.source.IterableConfigurationPropertySource;

import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/29 16:56
 */
public class NoUnboundElementsBindHandler extends AbstractBindHandler {

    private final Set<ConfigurationPropertyName> boundNames = SetUtils.newHashSet();

    private final Set<ConfigurationPropertyName> attemptedNames = SetUtils.newHashSet();

    private final BooleanFunc<ConfigurationPropertySource> filter;

    NoUnboundElementsBindHandler() {
        this(BindHandler.DEFAULT, c -> true);
    }

    public NoUnboundElementsBindHandler(BindHandler parent) {
        this(parent, c -> true);
    }

    public NoUnboundElementsBindHandler(BindHandler parent, BooleanFunc<ConfigurationPropertySource> filter) {
        super(parent);
        this.filter = filter;
    }

    @Override
    public <T> Bindable<T> onStart(ConfigurationPropertyName name, Bindable<T> target, BindContext context) {
        attemptedNames.add(name);
        return super.onStart(name, target, context);
    }

    @Override
    public Object onSuccess(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) {
        boundNames.add(name);
        return super.onSuccess(name, target, context, result);
    }

    @Override
    public Object onFailure(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Exception error) throws Exception {
        if (error instanceof UnboundConfigurationPropertiesException) {
            throw error;
        }
        return super.onFailure(name, target, context, error);
    }

    @Override
    public void onFinish(ConfigurationPropertyName name, Bindable<?> target, BindContext context, Object result) {
        if (context.getDepth() == 0) {
            checkNoUnboundElements(name, context);
        }
    }

    private void checkNoUnboundElements(ConfigurationPropertyName name, BindContext context) {
        Set<ConfigurationProperty> unbound = SetUtils.newTreeSet();
        for (ConfigurationPropertySource source : context.getSources()) {
            if (source instanceof IterableConfigurationPropertySource && filter.apply(source)) {
                collectUnbound(name, unbound, (IterableConfigurationPropertySource) source);
            }
        }
        if (!unbound.isEmpty()) {
            throw new UnboundConfigurationPropertiesException(unbound);
        }
    }

    private void collectUnbound(ConfigurationPropertyName name, Set<ConfigurationProperty> unbound, IterableConfigurationPropertySource source) {
        for (ConfigurationPropertyName unboundName : source.filter(c -> isUnbound(name, c))) {
            try {
                unbound.add(source.filter(c -> isUnbound(name, c)).getConfigurationProperty(unboundName));
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private boolean isUnbound(ConfigurationPropertyName name, ConfigurationPropertyName candidate) {
        return BooleanUtils.defaultIfFalse(name.isAncestorOf(candidate), () -> !boundNames.contains(candidate) && !isOverriddenCollectionElement(candidate), false);
    }

    private boolean isOverriddenCollectionElement(ConfigurationPropertyName candidate) {
        int lastIndex = candidate.getNumberOfElements() - 1;
        if (candidate.isLastElementIndexed()) {
            return boundNames.contains(candidate.chop(lastIndex));
        }
        Indexed indexed = getIndexed(candidate);
        if (indexed != null) {
            String zeroethProperty = indexed.getName() + StringConstants.LEFT_BRACKETS_ZERO_RIGHT_BRACKETS;
            if (boundNames.contains(ConfigurationPropertyName.of(zeroethProperty))) {
                return isCandidateValidPropertyName(zeroethProperty + StringConstants.DOT + indexed.getNestedPropertyName());
            }
        }
        return false;
    }

    private boolean isCandidateValidPropertyName(String nestedZeroethProperty) {
        return attemptedNames.contains(ConfigurationPropertyName.of(nestedZeroethProperty));
    }

    private Indexed getIndexed(ConfigurationPropertyName candidate) {
        for (int i = 0; i < candidate.getNumberOfElements(); i++) {
            if (candidate.isNumericIndex(i)) {
                return new Indexed(candidate.chop(i).toString(), candidate.getElement(i + 1, ConfigurationPropertyName.Form.UNIFORM));
            }
        }
        return null;
    }

    private static final class Indexed {

        private final String name;

        private final String nestedPropertyName;

        private Indexed(String name, String nestedPropertyName) {
            this.name = name;
            this.nestedPropertyName = nestedPropertyName;
        }

        String getName() {
            return name;
        }

        String getNestedPropertyName() {
            return nestedPropertyName;
        }
    }
}