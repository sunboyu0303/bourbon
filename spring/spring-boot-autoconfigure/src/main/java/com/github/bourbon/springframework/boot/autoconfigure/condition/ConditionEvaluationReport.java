package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ListUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.base.utils.SetUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/15 11:23
 */
public final class ConditionEvaluationReport {

    private static final String BEAN_NAME = "autoConfigurationReport";

    private static final AncestorsMatchedCondition ANCESTOR_CONDITION = new AncestorsMatchedCondition();

    private final SortedMap<String, ConditionAndOutcomes> outcomes = new TreeMap<>();

    private boolean addedAncestorOutcomes;

    private ConditionEvaluationReport parent;

    private final List<String> exclusions = new ArrayList<>();

    private final Set<String> unconditionalClasses = new HashSet<>();

    private ConditionEvaluationReport() {
    }

    public void recordConditionEvaluation(String source, Condition condition, ConditionOutcome outcome) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(condition, "Condition must not be null");
        Assert.notNull(outcome, "Outcome must not be null");
        unconditionalClasses.remove(source);
        outcomes.computeIfAbsent(source, o -> new ConditionAndOutcomes()).add(condition, outcome);
        addedAncestorOutcomes = false;
    }

    public void recordExclusions(Collection<String> exclusions) {
        Assert.notNull(exclusions, "exclusions must not be null");
        this.exclusions.addAll(exclusions);
    }

    public void recordEvaluationCandidates(List<String> evaluationCandidates) {
        Assert.notNull(evaluationCandidates, "evaluationCandidates must not be null");
        unconditionalClasses.addAll(evaluationCandidates);
    }

    public Map<String, ConditionAndOutcomes> getConditionAndOutcomesBySource() {
        if (!addedAncestorOutcomes) {
            outcomes.entrySet().stream().filter(e -> !e.getValue().isFullMatch()).forEach(e -> addNoMatchOutcomeToAncestors(e.getKey()));
            addedAncestorOutcomes = true;
        }
        return Collections.unmodifiableMap(outcomes);
    }

    private void addNoMatchOutcomeToAncestors(String source) {
        String prefix = source + StringConstants.DOLLAR;
        outcomes.forEach((candidateSource, sourceOutcomes) -> {
            if (candidateSource.startsWith(prefix)) {
                sourceOutcomes.add(ANCESTOR_CONDITION, ConditionOutcome.noMatch(ConditionMessage.forCondition("Ancestor " + source).because("did not match")));
            }
        });
    }

    public List<String> getExclusions() {
        return Collections.unmodifiableList(exclusions);
    }

    public Set<String> getUnconditionalClasses() {
        Set<String> filtered = new HashSet<>(unconditionalClasses);
        filtered.removeAll(exclusions);
        return Collections.unmodifiableSet(filtered);
    }

    public ConditionEvaluationReport getParent() {
        return parent;
    }

    public static ConditionEvaluationReport find(BeanFactory beanFactory) {
        return BooleanUtils.defaultIfAssignableFrom(beanFactory, ConfigurableBeanFactory.class, bf -> ConditionEvaluationReport.get((ConfigurableListableBeanFactory) bf));
    }

    public static ConditionEvaluationReport get(ConfigurableListableBeanFactory beanFactory) {
        synchronized (beanFactory) {
            ConditionEvaluationReport report;
            if (beanFactory.containsSingleton(BEAN_NAME)) {
                report = beanFactory.getBean(BEAN_NAME, ConditionEvaluationReport.class);
            } else {
                report = new ConditionEvaluationReport();
                beanFactory.registerSingleton(BEAN_NAME, report);
            }
            locateParent(beanFactory.getParentBeanFactory(), report);
            return report;
        }
    }

    private static void locateParent(BeanFactory beanFactory, ConditionEvaluationReport report) {
        if (beanFactory != null && report.parent == null && beanFactory.containsBean(BEAN_NAME)) {
            report.parent = beanFactory.getBean(BEAN_NAME, ConditionEvaluationReport.class);
        }
    }

    public ConditionEvaluationReport getDelta(ConditionEvaluationReport previousReport) {
        ConditionEvaluationReport delta = new ConditionEvaluationReport();
        outcomes.forEach((source, sourceOutcomes) -> {
            ConditionAndOutcomes previous = previousReport.outcomes.get(source);
            if (previous == null || previous.isFullMatch() != sourceOutcomes.isFullMatch()) {
                sourceOutcomes.forEach(come -> delta.recordConditionEvaluation(source, come.getCondition(), come.getOutcome()));
            }
        });
        List<String> newExclusions = ListUtils.newArrayList(exclusions);
        newExclusions.removeAll(previousReport.getExclusions());
        delta.recordExclusions(newExclusions);
        List<String> newUnconditionalClasses = ListUtils.newArrayList(unconditionalClasses);
        newUnconditionalClasses.removeAll(previousReport.unconditionalClasses);
        delta.unconditionalClasses.addAll(newUnconditionalClasses);
        return delta;
    }

    public static class ConditionAndOutcomes implements Iterable<ConditionAndOutcome> {

        private final Set<ConditionAndOutcome> outcomes = SetUtils.newLinkedHashSet();

        public void add(Condition condition, ConditionOutcome outcome) {
            outcomes.add(new ConditionAndOutcome(condition, outcome));
        }

        public boolean isFullMatch() {
            for (ConditionAndOutcome conditionAndOutcomes : this) {
                if (!conditionAndOutcomes.getOutcome().isMatch()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public Iterator<ConditionAndOutcome> iterator() {
            return Collections.unmodifiableSet(outcomes).iterator();
        }
    }

    public static class ConditionAndOutcome {

        private final Condition condition;

        private final ConditionOutcome outcome;

        public ConditionAndOutcome(Condition condition, ConditionOutcome outcome) {
            this.condition = condition;
            this.outcome = outcome;
        }

        public Condition getCondition() {
            return condition;
        }

        public ConditionOutcome getOutcome() {
            return outcome;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ConditionAndOutcome o = (ConditionAndOutcome) obj;
            return ObjectUtils.nullSafeEquals(condition.getClass(), o.condition.getClass()) && ObjectUtils.nullSafeEquals(outcome, o.outcome);
        }

        @Override
        public int hashCode() {
            return condition.getClass().hashCode() * 31 + outcome.hashCode();
        }

        @Override
        public String toString() {
            return condition.getClass() + StringConstants.SPACE + outcome;
        }
    }

    private static class AncestorsMatchedCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            throw new UnsupportedOperationException();
        }
    }
}