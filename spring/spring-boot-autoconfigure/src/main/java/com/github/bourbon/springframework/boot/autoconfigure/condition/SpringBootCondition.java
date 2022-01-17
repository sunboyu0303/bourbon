package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ClassUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/15 11:09
 */
public abstract class SpringBootCondition implements Condition {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected ConditionContext context;
    protected AnnotatedTypeMetadata metadata;

    public final boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        this.context = context;
        this.metadata = metadata;

        String classOrMethodName = getClassOrMethodName();
        try {
            ConditionOutcome outcome = getMatchOutcome();
            logOutcome(classOrMethodName, outcome);
            recordEvaluation(classOrMethodName, outcome);
            return outcome.isMatch();
        } catch (NoClassDefFoundError ex) {
            throw new IllegalStateException("Could not evaluate condition on " + classOrMethodName + " due to " + ex.getMessage() +
                    " not found. Make sure your own configuration does not rely on that class." +
                    " This can also happen if you are @ComponentScanning a springframework package" +
                    " (e.g. if you put a @ComponentScan in the default package by mistake)", ex);
        } catch (RuntimeException ex) {
            throw new IllegalStateException("Error processing condition on " + getName(metadata), ex);
        }
    }

    private String getName(AnnotatedTypeMetadata metadata) {
        if (metadata instanceof AnnotationMetadata) {
            return ((AnnotationMetadata) metadata).getClassName();
        }
        if (metadata instanceof MethodMetadata) {
            MethodMetadata methodMetadata = (MethodMetadata) metadata;
            return methodMetadata.getDeclaringClassName() + StringConstants.DOT + methodMetadata.getMethodName();
        }
        return metadata.toString();
    }

    private String getClassOrMethodName() {
        if (metadata instanceof ClassMetadata) {
            return ((ClassMetadata) metadata).getClassName();
        }
        MethodMetadata methodMetadata = (MethodMetadata) metadata;
        return methodMetadata.getDeclaringClassName() + StringConstants.POUND + methodMetadata.getMethodName();
    }

    protected final void logOutcome(String classOrMethodName, ConditionOutcome outcome) {
        if (logger.isTraceEnabled()) {
            logger.trace(getLogMessage(classOrMethodName, outcome));
        }
    }

    private StringBuilder getLogMessage(String classOrMethodName, ConditionOutcome outcome) {
        StringBuilder message = new StringBuilder();
        message.append("Condition ");
        message.append(ClassUtils.getSimpleClassName(getClass()));
        message.append(" on ");
        message.append(classOrMethodName);
        message.append(outcome.isMatch() ? " matched" : " did not match");
        if (!CharSequenceUtils.isEmpty(outcome.getMessage())) {
            message.append(" due to ");
            message.append(outcome.getMessage());
        }
        return message;
    }

    private void recordEvaluation(String classOrMethodName, ConditionOutcome outcome) {
        if (context.getBeanFactory() != null) {
            ConditionEvaluationReport.get(context.getBeanFactory()).recordConditionEvaluation(classOrMethodName, this, outcome);
        }
    }

    protected abstract ConditionOutcome getMatchOutcome();

    protected final boolean anyMatches(Condition... conditions) {
        for (Condition condition : conditions) {
            if (matches(condition)) {
                return true;
            }
        }
        return false;
    }

    protected final boolean matches(Condition condition) {
        return BooleanUtils.defaultSupplierIfAssignableFrom(condition, SpringBootCondition.class, c -> ((SpringBootCondition) c).getMatchOutcome().isMatch(), () -> condition.matches(context, metadata));
    }

    protected final ConditionOutcome getConditionOutcome(boolean b) {
        return b ? ConditionOutcome.match() : ConditionOutcome.noMatch();
    }
}