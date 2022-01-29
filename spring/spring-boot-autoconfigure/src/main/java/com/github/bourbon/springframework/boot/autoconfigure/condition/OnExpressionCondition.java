package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.expression.StandardBeanExpressionResolver;
import org.springframework.core.annotation.AnnotationAttributes;

import java.util.function.Supplier;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/16 19:52
 */
class OnExpressionCondition extends SpringBootCondition implements Condition {

    @Override
    public ConditionOutcome getMatchOutcome() {
        AnnotationAttributes attributes = AnnotationHelperUtils.getAnnotationAttributes(metadata, ConditionalOnExpression.class);
        String expression = wrapIfNecessary(attributes.getString("value"));
        ConditionMessage.Builder messageBuilder = ConditionMessage.forCondition(ConditionalOnExpression.class, StringConstants.LEFT_PARENTHESES + expression + StringConstants.RIGHT_PARENTHESES);
        expression = context.getEnvironment().resolvePlaceholders(expression);

        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        if (beanFactory != null) {
            boolean result = evaluateExpression(beanFactory, expression);
            return ConditionOutcome.of(result, messageBuilder.resultedIn(result));
        }
        return ConditionOutcome.noMatch(messageBuilder.because("no BeanFactory available."));
    }

    private Boolean evaluateExpression(ConfigurableListableBeanFactory beanFactory, String expression) {
        BeanExpressionResolver resolver = ObjectUtils.defaultSupplierIfNull(beanFactory.getBeanExpressionResolver(), (Supplier<BeanExpressionResolver>) StandardBeanExpressionResolver::new);
        Object result = resolver.evaluate(expression, new BeanExpressionContext(beanFactory, null));
        return result != null && (boolean) result;
    }

    private String wrapIfNecessary(String expression) {
        return BooleanUtils.defaultIfFalse(!expression.startsWith(StringConstants.POUND_LEFT_BRACES), () -> StringConstants.POUND_LEFT_BRACES + expression + StringConstants.RIGHT_BRACES, expression);
    }
}