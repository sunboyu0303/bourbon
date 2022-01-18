package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.system.SystemInfo;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.annotation.Condition;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/16 17:50
 */
class OnClassCondition extends FilteringSpringBootCondition implements Condition {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnClassCondition.class);

    @Override
    protected final ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        int len = autoConfigurationClasses.length;
        return BooleanUtils.defaultSupplierIfFalse(len > 1 && SystemInfo.runtimeInfo.availableProcessors() > 1,
                () -> resolveOutcomesThreaded(autoConfigurationClasses, autoConfigurationMetadata),
                () -> new StandardOutcomesResolver(autoConfigurationClasses, 0, len, autoConfigurationMetadata, context.getClassLoader()).resolveOutcomes()
        );
    }

    private ConditionOutcome[] resolveOutcomesThreaded(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        int len = autoConfigurationClasses.length;
        int split = len / 2;
        OutcomesResolver firstHalfResolver = createOutcomesResolver(autoConfigurationClasses, 0, split, autoConfigurationMetadata);
        OutcomesResolver secondHalfResolver = new StandardOutcomesResolver(autoConfigurationClasses, split, len, autoConfigurationMetadata, context.getClassLoader());
        ConditionOutcome[] firstHalf = firstHalfResolver.resolveOutcomes();
        ConditionOutcome[] secondHalf = secondHalfResolver.resolveOutcomes();
        ConditionOutcome[] outcomes = new ConditionOutcome[len];
        System.arraycopy(firstHalf, 0, outcomes, 0, firstHalf.length);
        System.arraycopy(secondHalf, 0, outcomes, split, secondHalf.length);
        return outcomes;
    }

    private OutcomesResolver createOutcomesResolver(String[] autoConfigurationClasses, int start, int end, AutoConfigurationMetadata autoConfigurationMetadata) {
        OutcomesResolver outcomesResolver = new StandardOutcomesResolver(autoConfigurationClasses, start, end, autoConfigurationMetadata, context.getClassLoader());
        try {
            return new ThreadedOutcomesResolver(outcomesResolver);
        } catch (AccessControlException ex) {
            return outcomesResolver;
        }
    }

    @Override
    public ConditionOutcome getMatchOutcome() {
        ConditionMessage matchMessage = ConditionMessage.empty();
        List<String> onClasses = getCandidates(metadata, ConditionalOnClass.class);

        if (onClasses != null) {
            List<String> missing = filter(onClasses, ClassNameFilter.MISSING);
            if (!missing.isEmpty()) {
                return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnClass.class).didNotFind("required class", "required classes").items(ConditionMessage.Style.QUOTE, missing));
            }
            matchMessage = matchMessage.andCondition(ConditionalOnClass.class).found("required class", "required classes").items(ConditionMessage.Style.QUOTE, filter(onClasses, ClassNameFilter.PRESENT));
        }

        List<String> onMissingClasses = getCandidates(metadata, ConditionalOnMissingClass.class);
        if (onMissingClasses != null) {
            List<String> present = filter(onMissingClasses, ClassNameFilter.PRESENT);
            if (!present.isEmpty()) {
                return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnMissingClass.class).found("unwanted class", "unwanted classes").items(ConditionMessage.Style.QUOTE, present));
            }
            matchMessage = matchMessage.andCondition(ConditionalOnMissingClass.class).didNotFind("unwanted class", "unwanted classes").items(ConditionMessage.Style.QUOTE, filter(onMissingClasses, ClassNameFilter.MISSING));
        }

        return ConditionOutcome.match(matchMessage);
    }

    private List<String> getCandidates(AnnotatedTypeMetadata metadata, Class<?> annotationType) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(annotationType.getName(), true);
        if (attributes == null) {
            return null;
        }
        List<String> candidates = ListUtils.newArrayList();
        addAll(candidates, attributes.get("value"));
        addAll(candidates, attributes.get("name"));
        return candidates;
    }

    private void addAll(List<String> list, List<Object> itemsToAdd) {
        if (itemsToAdd != null) {
            for (Object item : itemsToAdd) {
                Collections.addAll(list, (String[]) item);
            }
        }
    }

    private interface OutcomesResolver {

        ConditionOutcome[] resolveOutcomes();

    }

    private static final class ThreadedOutcomesResolver implements OutcomesResolver {

        private final Thread thread;

        private final AtomicReference<ConditionOutcome[]> outcomes = new AtomicReference<>();

        private ThreadedOutcomesResolver(OutcomesResolver outcomesResolver) {
            thread = new Thread(() -> outcomes.set(outcomesResolver.resolveOutcomes()));
            thread.start();
        }

        @Override
        public ConditionOutcome[] resolveOutcomes() {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            return outcomes.get();
        }
    }

    private static final class StandardOutcomesResolver implements OutcomesResolver {

        private final String[] autoConfigurationClasses;

        private final int start;

        private final int end;

        private final AutoConfigurationMetadata autoConfigurationMetadata;

        private final ClassLoader beanClassLoader;

        private StandardOutcomesResolver(String[] autoConfigurationClasses, int start, int end, AutoConfigurationMetadata autoConfigurationMetadata, ClassLoader beanClassLoader) {
            this.autoConfigurationClasses = autoConfigurationClasses;
            this.start = start;
            this.end = end;
            this.autoConfigurationMetadata = autoConfigurationMetadata;
            this.beanClassLoader = beanClassLoader;
        }

        @Override
        public ConditionOutcome[] resolveOutcomes() {
            return getOutcomes(autoConfigurationClasses, start, end, autoConfigurationMetadata);
        }

        private ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses, int start, int end, AutoConfigurationMetadata autoConfigurationMetadata) {
            ConditionOutcome[] outcomes = new ConditionOutcome[end - start];
            for (int i = start; i < end; i++) {
                String autoConfigurationClass = autoConfigurationClasses[i];
                if (autoConfigurationClass != null) {
                    String candidates = autoConfigurationMetadata.get(autoConfigurationClass, ConditionalOnClass.class.getSimpleName());
                    if (candidates != null) {
                        outcomes[i - start] = getOutcome(candidates);
                    }
                }
            }
            return outcomes;
        }

        private ConditionOutcome getOutcome(String candidates) {
            try {
                if (!candidates.contains(StringConstants.COMMA)) {
                    return getOutcome(candidates, beanClassLoader);
                }
                for (String candidate : CharSequenceUtils.commaDelimitedListToStringArray(candidates)) {
                    ConditionOutcome outcome = getOutcome(candidate, beanClassLoader);
                    if (outcome != null) {
                        return outcome;
                    }
                }
            } catch (Exception ex) {
                LOGGER.error(ex);
            }
            return null;
        }

        private ConditionOutcome getOutcome(String className, ClassLoader classLoader) {
            return BooleanUtils.defaultIfFalse(ClassNameFilter.MISSING.matches(className, classLoader),
                    () -> ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnClass.class).didNotFind("required class").items(ConditionMessage.Style.QUOTE, className))
            );
        }
    }
}