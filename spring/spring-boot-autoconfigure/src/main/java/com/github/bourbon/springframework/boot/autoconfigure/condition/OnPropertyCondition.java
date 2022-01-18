package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.ArrayUtils;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ListUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.PropertyResolver;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/17 15:54
 */
class OnPropertyCondition extends SpringBootCondition implements Condition {

    @Override
    protected ConditionOutcome getMatchOutcome() {
        List<AnnotationAttributes> allAnnotationAttributes = annotationAttributesFromMultiValueMap(metadata.getAllAnnotationAttributes(ConditionalOnProperty.class.getName()));
        List<ConditionMessage> noMatch = ListUtils.newArrayList();
        List<ConditionMessage> match = ListUtils.newArrayList();
        for (AnnotationAttributes annotationAttributes : allAnnotationAttributes) {
            ConditionOutcome outcome = determineOutcome(annotationAttributes);
            (outcome.isMatch() ? match : noMatch).add(outcome.getConditionMessage());
        }
        return BooleanUtils.defaultSupplierIfFalse(noMatch.isEmpty(), () -> ConditionOutcome.match(ConditionMessage.of(match)), () -> ConditionOutcome.noMatch(ConditionMessage.of(noMatch)));
    }

    private List<AnnotationAttributes> annotationAttributesFromMultiValueMap(MultiValueMap<String, Object> multiValueMap) {
        List<Map<String, Object>> maps = ListUtils.newArrayList();
        multiValueMap.forEach((key, value) -> {
            for (int i = 0; i < value.size(); i++) {
                Map<String, Object> map;
                if (i < maps.size()) {
                    map = maps.get(i);
                } else {
                    map = MapUtils.newHashMap();
                    maps.add(map);
                }
                map.put(key, value.get(i));
            }
        });
        return maps.stream().map(AnnotationAttributes::fromMap).collect(Collectors.toList());
    }

    private ConditionOutcome determineOutcome(AnnotationAttributes annotationAttributes) {
        Spec spec = new Spec(annotationAttributes);
        List<String> missingProperties = ListUtils.newArrayList();
        List<String> nonMatchingProperties = ListUtils.newArrayList();
        spec.collectProperties(context.getEnvironment(), missingProperties, nonMatchingProperties);
        if (!missingProperties.isEmpty()) {
            return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnProperty.class, spec).didNotFind("property", "properties").items(ConditionMessage.Style.QUOTE, missingProperties));
        }
        if (!nonMatchingProperties.isEmpty()) {
            return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnProperty.class, spec).found("different value in property", "different value in properties").items(ConditionMessage.Style.QUOTE, nonMatchingProperties));
        }
        return ConditionOutcome.match(ConditionMessage.forCondition(ConditionalOnProperty.class, spec).because("matched"));
    }

    private static class Spec {

        private final String prefix;

        private final String havingValue;

        private final String[] names;

        private final boolean matchIfMissing;

        Spec(AnnotationAttributes annotationAttributes) {
            String prefix = annotationAttributes.getString("prefix").trim();
            if (!CharSequenceUtils.isEmpty(prefix) && !prefix.endsWith(StringConstants.DOT)) {
                prefix = prefix + StringConstants.DOT;
            }
            this.prefix = prefix;
            this.havingValue = annotationAttributes.getString("havingValue");
            this.names = getNames(annotationAttributes);
            this.matchIfMissing = annotationAttributes.getBoolean("matchIfMissing");
        }

        private String[] getNames(Map<String, Object> annotationAttributes) {
            String[] value = (String[]) annotationAttributes.get("value");
            String[] name = (String[]) annotationAttributes.get("name");
            Assert.isTrue(value.length > 0 || name.length > 0, "The name or value attribute of @ConditionalOnProperty must be specified");
            Assert.isTrue(value.length == 0 || name.length == 0, "The name and value attributes of @ConditionalOnProperty are exclusive");
            return BooleanUtils.defaultIfFalse(value.length > 0, value, name);
        }

        private void collectProperties(PropertyResolver resolver, List<String> missing, List<String> nonMatching) {
            for (String name : names) {
                String key = prefix + name;
                if (resolver.containsProperty(key)) {
                    if (!isMatch(resolver.getProperty(key))) {
                        nonMatching.add(name);
                    }
                } else {
                    if (!matchIfMissing) {
                        missing.add(name);
                    }
                }
            }
        }

        private boolean isMatch(String value) {
            return BooleanUtils.defaultSupplierIfFalse(CharSequenceUtils.isEmpty(havingValue), () -> !"false".equalsIgnoreCase(value), () -> havingValue.equalsIgnoreCase(value));
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append(StringConstants.LEFT_PARENTHESES);
            result.append(prefix);
            if (names.length == 1) {
                result.append(names[0]);
            } else {
                result.append(StringConstants.LEFT_BRACKETS);
                result.append(ArrayUtils.toCommaDelimitedString(names));
                result.append(StringConstants.RIGHT_BRACKETS);
            }
            if (!CharSequenceUtils.isEmpty(havingValue)) {
                result.append(StringConstants.EQUAL).append(havingValue);
            }
            result.append(StringConstants.RIGHT_PARENTHESES);
            return result.toString();
        }
    }
}