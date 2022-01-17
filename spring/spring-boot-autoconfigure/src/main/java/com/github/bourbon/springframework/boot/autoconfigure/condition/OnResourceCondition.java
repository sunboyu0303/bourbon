package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.core.annotation.AnnotationAttributes;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/17 17:42
 */
class OnResourceCondition extends SpringBootCondition implements Condition {

    @Override
    public ConditionOutcome getMatchOutcome() {
        AnnotationAttributes annotationAttributes = AnnotationHelperUtils.getAnnotationAttributes(metadata, ConditionalOnResource.class);
        List<String> locations = ListUtils.newArrayList();
        Collections.addAll(locations, annotationAttributes.getStringArray("resources"));
        Assert.notEmpty(locations, "@ConditionalOnResource annotations must specify at least one resource location");
        List<String> missing = locations.stream().filter(location -> !context.getResourceLoader().getResource(context.getEnvironment().resolvePlaceholders(location)).exists()).collect(Collectors.toList());
        return BooleanUtils.defaultSupplierIfFalse(missing.isEmpty(),
                () -> ConditionOutcome.match(ConditionMessage.forCondition(ConditionalOnResource.class).found("location", "locations").items(locations)),
                () -> ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnResource.class).didNotFind("resource", "resources").items(ConditionMessage.Style.QUOTE, missing))
        );
    }
}