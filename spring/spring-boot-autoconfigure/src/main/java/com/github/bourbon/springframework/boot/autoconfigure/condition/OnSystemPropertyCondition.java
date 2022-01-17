package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SystemUtils;
import com.github.bourbon.springframework.context.annotation.PropertiesUtils;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.PropertyPlaceholderHelper;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/17 16:33
 */
class OnSystemPropertyCondition extends SpringBootCondition implements Condition {

    @Override
    protected ConditionOutcome getMatchOutcome() {
        AnnotationAttributes attributes = AnnotationHelperUtils.getAnnotationAttributes(metadata, ConditionalOnSystemProperty.class);
        String value = attributes.getString("value");
        value = new PropertyPlaceholderHelper(StringConstants.DOLLAR_LEFT_BRACES, StringConstants.RIGHT_BRACES).replacePlaceholders(value, PropertiesUtils.getProperties());
        String systemProperty = SystemUtils.get(attributes.getString("key"));
        return getConditionOutcome(ObjectUtils.nonNull(systemProperty) && systemProperty.equals(value));
    }
}