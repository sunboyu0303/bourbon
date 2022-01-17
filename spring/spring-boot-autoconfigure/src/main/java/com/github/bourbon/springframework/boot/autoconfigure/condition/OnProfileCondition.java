package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.utils.ArrayUtils;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.core.env.Profiles;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/16 17:07
 */
class OnProfileCondition extends SpringBootCondition implements Condition {

    @Override
    protected ConditionOutcome getMatchOutcome() {
        String[] value = AnnotationHelperUtils.getAnnotationAttributes(metadata, ConditionalOnProfile.class, true).getStringArray("value");
        return BooleanUtils.defaultSupplierIfFalse(ArrayUtils.isEmpty(value), ConditionOutcome::noMatch, () -> getConditionOutcome(context.getEnvironment().acceptsProfiles(Profiles.of(value))));
    }
}