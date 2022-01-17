package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.system.JavaVersion;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.core.annotation.AnnotationAttributes;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/17 13:19
 */
class OnJavaCondition extends SpringBootCondition implements Condition {

    private static final JavaVersion JVM_VERSION = JavaVersion.getJavaVersion();

    @Override
    public ConditionOutcome getMatchOutcome() {
        AnnotationAttributes annotationAttributes = AnnotationHelperUtils.getAnnotationAttributes(metadata, ConditionalOnJava.class);
        return getMatchOutcome(annotationAttributes.getEnum("range"), annotationAttributes.getEnum("value"));
    }

    protected ConditionOutcome getMatchOutcome(ConditionalOnJava.Range range, JavaVersion version) {
        String expected = String.format((range != ConditionalOnJava.Range.EQUAL_OR_NEWER) ? "(older than %s)" : "(%s or newer)", version);
        ConditionMessage message = ConditionMessage.forCondition(ConditionalOnJava.class, expected).foundExactly(JVM_VERSION);
        return ConditionOutcome.of(range.isWithin(JVM_VERSION, version), message);
    }
}