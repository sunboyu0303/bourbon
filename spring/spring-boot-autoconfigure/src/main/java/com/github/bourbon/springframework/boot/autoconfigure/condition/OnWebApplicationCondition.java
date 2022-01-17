package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.ArrayUtils;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.annotation.Condition;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/17 16:42
 */
class OnWebApplicationCondition extends FilteringSpringBootCondition implements Condition {

    @Override
    protected ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        return ArrayUtils.of(getOutcome());
    }

    private ConditionOutcome getOutcome() {
        return BooleanUtils.defaultIfFalse(!ClassNameFilter.isPresent(GenericWebApplicationContext.class.getName(), context.getClassLoader()),
                () -> ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnWebApplication.class).didNotFind("servlet web application classes").atAll())
        );
    }

    @Override
    protected ConditionOutcome getMatchOutcome() {
        boolean required = metadata.isAnnotated(ConditionalOnWebApplication.class.getName());
        ConditionOutcome outcome = isWebApplication();
        if (required && !outcome.isMatch()) {
            return ConditionOutcome.noMatch(outcome.getConditionMessage());
        }
        if (!required && outcome.isMatch()) {
            return ConditionOutcome.noMatch(outcome.getConditionMessage());
        }
        return ConditionOutcome.match(outcome.getConditionMessage());
    }

    private ConditionOutcome isWebApplication() {

        ConditionMessage.Builder message = ConditionMessage.forCondition(StringConstants.EMPTY);
        if (!ClassNameFilter.isPresent(GenericWebApplicationContext.class.getName(), context.getClassLoader())) {
            return ConditionOutcome.noMatch(message.didNotFind("servlet web application classes").atAll());
        }

        if (context.getBeanFactory() != null) {
            String[] scopes = context.getBeanFactory().getRegisteredScopeNames();
            if (ArrayUtils.contains(scopes, "session")) {
                return ConditionOutcome.match(message.foundExactly("'session' scope"));
            }
        }

        if (context.getEnvironment() instanceof ConfigurableWebEnvironment) {
            return ConditionOutcome.match(message.foundExactly("ConfigurableWebEnvironment"));
        }

        if (context.getResourceLoader() instanceof WebApplicationContext) {
            return ConditionOutcome.match(message.foundExactly("WebApplicationContext"));
        }

        return ConditionOutcome.noMatch(message.because("not a servlet web application"));
    }
}