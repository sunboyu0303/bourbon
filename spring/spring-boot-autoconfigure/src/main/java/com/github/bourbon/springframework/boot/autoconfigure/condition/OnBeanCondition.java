package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.*;
import com.github.bourbon.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotationCollectors;
import org.springframework.core.annotation.MergedAnnotationPredicates;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/15 11:06
 */
class OnBeanCondition extends FilteringSpringBootCondition implements ConfigurationCondition {

    @Override
    public ConfigurationPhase getConfigurationPhase() {
        return ConfigurationPhase.REGISTER_BEAN;
    }

    @Override
    protected final ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        ConditionOutcome[] outcomes = new ConditionOutcome[autoConfigurationClasses.length];
        for (int i = 0; i < outcomes.length; i++) {
            String autoConfigurationClass = autoConfigurationClasses[i];
            if (autoConfigurationClass != null) {
                outcomes[i] = getOutcome(autoConfigurationMetadata.getSet(autoConfigurationClass, ConditionalOnBean.class.getSimpleName()), ConditionalOnBean.class);
                if (outcomes[i] == null) {
                    outcomes[i] = getOutcome(autoConfigurationMetadata.getSet(autoConfigurationClass, ConditionalOnSingleCandidate.class.getSimpleName()), ConditionalOnSingleCandidate.class);
                }
            }
        }
        return outcomes;
    }

    private ConditionOutcome getOutcome(Set<String> requiredBeanTypes, Class<? extends Annotation> annotation) {
        List<String> missing = filter(requiredBeanTypes, ClassNameFilter.MISSING);
        return BooleanUtils.defaultIfFalse(!missing.isEmpty(), () -> ConditionOutcome.noMatch(
                ConditionMessage.forCondition(annotation).didNotFind("required type", "required types").items(ConditionMessage.Style.QUOTE, missing)
        ));
    }

    @Override
    public ConditionOutcome getMatchOutcome() {
        ConditionMessage matchMessage = ConditionMessage.empty();
        MergedAnnotations annotations = metadata.getAnnotations();

        if (annotations.isPresent(ConditionalOnBean.class)) {
            Spec<ConditionalOnBean> spec = new Spec<>(context, metadata, annotations, ConditionalOnBean.class);
            MatchResult matchResult = getMatchingBeans(spec);
            if (!matchResult.isAllMatched()) {
                return ConditionOutcome.noMatch(spec.message().because(createOnBeanNoMatchReason(matchResult)));
            }
            matchMessage = spec.message(matchMessage).found("bean", "beans").items(ConditionMessage.Style.QUOTE, matchResult.getNamesOfAllMatches());
        }

        if (metadata.isAnnotated(ConditionalOnSingleCandidate.class.getName())) {
            Spec<ConditionalOnSingleCandidate> spec = new SingleCandidateSpec(context, metadata, annotations);
            MatchResult matchResult = getMatchingBeans(spec);
            if (!matchResult.isAllMatched()) {
                return ConditionOutcome.noMatch(spec.message().didNotFind("any beans").atAll());
            } else if (!hasSingleAutowireCandidate(context.getBeanFactory(), matchResult.getNamesOfAllMatches(), spec.getStrategy() == SearchStrategy.ALL)) {
                return ConditionOutcome.noMatch(spec.message().didNotFind("a primary bean from beans").items(ConditionMessage.Style.QUOTE, matchResult.getNamesOfAllMatches()));
            }
            matchMessage = spec.message(matchMessage).found("a primary bean from beans").items(ConditionMessage.Style.QUOTE, matchResult.getNamesOfAllMatches());
        }

        if (metadata.isAnnotated(ConditionalOnMissingBean.class.getName())) {
            Spec<ConditionalOnMissingBean> spec = new Spec<>(context, metadata, annotations, ConditionalOnMissingBean.class);
            MatchResult matchResult = getMatchingBeans(spec);
            if (matchResult.isAnyMatched()) {
                return ConditionOutcome.noMatch(spec.message().because(createOnMissingBeanNoMatchReason(matchResult)));
            }
            matchMessage = spec.message(matchMessage).didNotFind("any beans").atAll();
        }
        return ConditionOutcome.match(matchMessage);
    }

    protected final MatchResult getMatchingBeans(Spec<?> spec) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        boolean considerHierarchy = spec.getStrategy() != SearchStrategy.CURRENT;
        Set<Class<?>> parameterizedContainers = spec.getParameterizedContainers();

        if (spec.getStrategy() == SearchStrategy.ANCESTORS) {
            BeanFactory parent = beanFactory.getParentBeanFactory();
            Assert.isInstanceOf(ConfigurableListableBeanFactory.class, parent, "Unable to use SearchStrategy.ANCESTORS");
            beanFactory = (ConfigurableListableBeanFactory) parent;
        }

        MatchResult result = new MatchResult();
        Set<String> beansIgnoredByType = getNamesOfBeansIgnoredByType(beanFactory, considerHierarchy, spec.getIgnoredTypes(), parameterizedContainers);

        for (String type : spec.getTypes()) {
            Collection<String> typeMatches = getBeanNamesForType(considerHierarchy, beanFactory, type, parameterizedContainers);
            typeMatches.removeIf(match -> beansIgnoredByType.contains(match) || ScopedProxyUtils.isScopedTarget(match));
            if (typeMatches.isEmpty()) {
                result.recordUnmatchedType(type);
            } else {
                result.recordMatchedType(type, typeMatches);
            }
        }

        for (String annotation : spec.getAnnotations()) {
            Set<String> annotationMatches = getBeanNamesForAnnotation(beanFactory, annotation, considerHierarchy);
            annotationMatches.removeAll(beansIgnoredByType);
            if (annotationMatches.isEmpty()) {
                result.recordUnmatchedAnnotation(annotation);
            } else {
                result.recordMatchedAnnotation(annotation, annotationMatches);
            }
        }

        for (String beanName : spec.getNames()) {
            if (!beansIgnoredByType.contains(beanName) && containsBean(beanFactory, beanName, considerHierarchy)) {
                result.recordMatchedName(beanName);
            } else {
                result.recordUnmatchedName(beanName);
            }
        }
        return result;
    }

    private Set<String> getNamesOfBeansIgnoredByType(ListableBeanFactory beanFactory, boolean considerHierarchy, Set<String> ignoredTypes, Set<Class<?>> parameterizedContainers) {
        Set<String> result = null;
        for (String ignoredType : ignoredTypes) {
            result = addAll(result, getBeanNamesForType(considerHierarchy, beanFactory, ignoredType, parameterizedContainers));
        }
        return ObjectUtils.defaultSupplierIfNull(result, Collections::emptySet);
    }

    private Set<String> getBeanNamesForType(boolean considerHierarchy, ListableBeanFactory beanFactory, String type, Set<Class<?>> parameterizedContainers) throws LinkageError {
        try {
            return getBeanNamesForType(beanFactory, considerHierarchy, resolve(type, context.getClassLoader()), parameterizedContainers);
        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
            return Collections.emptySet();
        }
    }

    private Set<String> getBeanNamesForType(ListableBeanFactory beanFactory, boolean considerHierarchy, Class<?> type, Set<Class<?>> parameterizedContainers) {
        return ObjectUtils.defaultSupplierIfNull(collectBeanNamesForType(beanFactory, considerHierarchy, type, parameterizedContainers, null), Collections::emptySet);
    }

    private Set<String> collectBeanNamesForType(ListableBeanFactory beanFactory, boolean considerHierarchy, Class<?> type, Set<Class<?>> parameterizedContainers, Set<String> result) {

        result = addAll(result, beanFactory.getBeanNamesForType(type, true, false));
        for (Class<?> container : parameterizedContainers) {
            ResolvableType generic = ResolvableType.forClassWithGenerics(container, type);
            result = addAll(result, beanFactory.getBeanNamesForType(generic, true, false));
        }

        if (considerHierarchy && beanFactory instanceof HierarchicalBeanFactory) {
            BeanFactory parent = ((HierarchicalBeanFactory) beanFactory).getParentBeanFactory();
            if (parent instanceof ListableBeanFactory) {
                result = collectBeanNamesForType((ListableBeanFactory) parent, considerHierarchy, type, parameterizedContainers, result);
            }
        }
        return result;
    }

    private Set<String> getBeanNamesForAnnotation(ConfigurableListableBeanFactory beanFactory, String type, boolean considerHierarchy) throws LinkageError {
        try {
            return ObjectUtils.defaultSupplierIfNull(collectBeanNamesForAnnotation(beanFactory, resolveAnnotationType(type), considerHierarchy, null), Collections::emptySet);
        } catch (ClassNotFoundException ex) {
            return Collections.emptySet();
        }
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Annotation> resolveAnnotationType(String type) throws ClassNotFoundException {
        return (Class<? extends Annotation>) resolve(type, context.getClassLoader());
    }

    private Set<String> collectBeanNamesForAnnotation(ListableBeanFactory beanFactory, Class<? extends Annotation> annotationType, boolean considerHierarchy, Set<String> result) {
        result = addAll(result, beanFactory.getBeanNamesForAnnotation(annotationType));
        if (considerHierarchy) {
            BeanFactory parent = ((HierarchicalBeanFactory) beanFactory).getParentBeanFactory();
            if (parent instanceof ListableBeanFactory) {
                result = collectBeanNamesForAnnotation((ListableBeanFactory) parent, annotationType, considerHierarchy, result);
            }
        }
        return result;
    }

    private boolean containsBean(ConfigurableListableBeanFactory beanFactory, String beanName, boolean considerHierarchy) {
        return BooleanUtils.defaultSupplierIfFalse(considerHierarchy, () -> beanFactory.containsBean(beanName), () -> beanFactory.containsLocalBean(beanName));
    }

    private String createOnBeanNoMatchReason(MatchResult matchResult) {
        StringBuilder reason = new StringBuilder();
        appendMessageForNoMatches(reason, matchResult.getUnmatchedAnnotations(), "annotated with");
        appendMessageForNoMatches(reason, matchResult.getUnmatchedTypes(), "of type");
        appendMessageForNoMatches(reason, matchResult.getUnmatchedNames(), "named");
        return reason.toString();
    }

    private void appendMessageForNoMatches(StringBuilder reason, Collection<String> unmatched, String description) {
        if (!unmatched.isEmpty()) {
            if (reason.length() > 0) {
                reason.append(" and ");
            }
            reason.append("did not find any beans ");
            reason.append(description);
            reason.append(StringConstants.SPACE);
            reason.append(CollectionUtils.toDelimitedString(unmatched, StringConstants.COMMA_SPACE));
        }
    }

    private String createOnMissingBeanNoMatchReason(MatchResult matchResult) {
        StringBuilder reason = new StringBuilder();
        appendMessageForMatches(reason, matchResult.getMatchedAnnotations(), "annotated with");
        appendMessageForMatches(reason, matchResult.getMatchedTypes(), "of type");
        if (!matchResult.getMatchedNames().isEmpty()) {
            if (reason.length() > 0) {
                reason.append(" and ");
            }
            reason.append("found beans named ");
            reason.append(CollectionUtils.toDelimitedString(matchResult.getMatchedNames(), StringConstants.COMMA_SPACE));
        }
        return reason.toString();
    }

    private void appendMessageForMatches(StringBuilder reason, Map<String, Collection<String>> matches, String description) {
        if (!matches.isEmpty()) {
            matches.forEach((key, value) -> {
                if (reason.length() > 0) {
                    reason.append(" and ");
                }
                reason.append("found beans ");
                reason.append(description);
                reason.append(StringConstants.SPACE_SINGLE_QUOTE);
                reason.append(key);
                reason.append(StringConstants.SINGLE_QUOTE_SPACE);
                reason.append(CollectionUtils.toDelimitedString(value, StringConstants.COMMA_SPACE));
            });
        }
    }

    private boolean hasSingleAutowireCandidate(ConfigurableListableBeanFactory beanFactory, Set<String> beanNames, boolean considerHierarchy) {
        return (beanNames.size() == 1 || getPrimaryBeans(beanFactory, beanNames, considerHierarchy).size() == 1);
    }

    private List<String> getPrimaryBeans(ConfigurableListableBeanFactory beanFactory, Set<String> beanNames, boolean considerHierarchy) {
        return beanNames.stream().filter(beanName -> {
            BeanDefinition beanDefinition = findBeanDefinition(beanFactory, beanName, considerHierarchy);
            return beanDefinition != null && beanDefinition.isPrimary();
        }).collect(Collectors.toList());
    }

    private BeanDefinition findBeanDefinition(ConfigurableListableBeanFactory beanFactory, String beanName, boolean considerHierarchy) {
        if (beanFactory.containsBeanDefinition(beanName)) {
            return beanFactory.getBeanDefinition(beanName);
        }
        if (considerHierarchy) {
            return BooleanUtils.defaultIfAssignableFrom(beanFactory.getParentBeanFactory(), ConfigurableListableBeanFactory.class, bf -> findBeanDefinition(((ConfigurableListableBeanFactory) bf), beanName, considerHierarchy));
        }
        return null;
    }

    private static Set<String> addAll(Set<String> result, Collection<String> additional) {
        if (CollectionUtils.isEmpty(additional)) {
            return result;
        }
        result = ObjectUtils.defaultSupplierIfNull(result, SetUtils::newLinkedHashSet);
        result.addAll(additional);
        return result;
    }

    private static Set<String> addAll(Set<String> result, String[] additional) {
        if (ArrayUtils.isEmpty(additional)) {
            return result;
        }
        result = ObjectUtils.defaultSupplierIfNull(result, LinkedHashSet::new);
        Collections.addAll(result, additional);
        return result;
    }

    private static class Spec<A extends Annotation> {

        private final ConditionContext context;

        private final AnnotatedTypeMetadata metadata;

        private final Class<? extends Annotation> annotationType;

        private final Set<String> names;

        private final Set<String> types;

        private final Set<String> annotations;

        private final Set<String> ignoredTypes;

        private final Set<Class<?>> parameterizedContainers;

        private final SearchStrategy strategy;

        Spec(ConditionContext context, AnnotatedTypeMetadata metadata, MergedAnnotations annotations, Class<A> annotationType) {

            MultiValueMap<String, Object> attributes = annotations.stream(annotationType).filter(MergedAnnotationPredicates.unique(MergedAnnotation::getMetaTypes)).collect(MergedAnnotationCollectors.toMultiValueMap(MergedAnnotation.Adapt.CLASS_TO_STRING));

            this.context = context;
            this.metadata = metadata;
            this.annotationType = annotationType;
            this.names = extract(attributes, "name");
            this.annotations = extract(attributes, "annotation");
            this.ignoredTypes = extract(attributes, "ignored", "ignoredType");
            this.parameterizedContainers = resolveWhenPossible(extract(attributes, "parameterizedContainer"));
            this.strategy = annotations.get(annotationType).getValue("search", SearchStrategy.class).orElse(null);

            Set<String> types = extractTypes(attributes);
            BeanTypeDeductionException deductionException = null;
            if (types.isEmpty() && this.names.isEmpty()) {
                try {
                    types = deducedBeanType();
                } catch (BeanTypeDeductionException ex) {
                    deductionException = ex;
                }
            }
            this.types = types;
            validate(deductionException);
        }

        protected Set<String> extractTypes(MultiValueMap<String, Object> attributes) {
            return extract(attributes, "value", "type");
        }

        private Set<String> extract(MultiValueMap<String, Object> attributes, String... attributeNames) {
            if (attributes.isEmpty()) {
                return Collections.emptySet();
            }
            Set<String> result = new LinkedHashSet<>();
            for (String attributeName : attributeNames) {
                List<Object> values = attributes.getOrDefault(attributeName, Collections.emptyList());
                for (Object value : values) {
                    if (value instanceof String[]) {
                        merge(result, (String[]) value);
                    } else if (value instanceof String) {
                        merge(result, (String) value);
                    }
                }
            }
            return BooleanUtils.defaultIfFalse(!result.isEmpty(), result, Collections.emptySet());
        }

        private void merge(Set<String> result, String... additional) {
            Collections.addAll(result, additional);
        }

        private Set<Class<?>> resolveWhenPossible(Set<String> classNames) {
            if (classNames.isEmpty()) {
                return Collections.emptySet();
            }
            Set<Class<?>> resolved = new LinkedHashSet<>(classNames.size());
            for (String className : classNames) {
                try {
                    resolved.add(resolve(className, context.getClassLoader()));
                } catch (ClassNotFoundException | NoClassDefFoundError ex) {
                    // ignore
                }
            }
            return resolved;
        }

        protected void validate(BeanTypeDeductionException ex) {
            if (!hasAtLeastOneElement(types, names, annotations)) {
                String message = getAnnotationName() + " did not specify a bean using type, name or annotation";
                if (ex == null) {
                    throw new IllegalStateException(message);
                }
                throw new IllegalStateException(message + " and the attempt to deduce the bean's type failed", ex);
            }
        }

        private boolean hasAtLeastOneElement(Set<?>... sets) {
            for (Set<?> set : sets) {
                if (!CollectionUtils.isEmpty(set)) {
                    return true;
                }
            }
            return false;
        }

        protected final String getAnnotationName() {
            return StringConstants.AT + ClassUtils.getSimpleClassName(annotationType);
        }

        private Set<String> deducedBeanType() {
            return BooleanUtils.defaultSupplierIfFalse(metadata instanceof MethodMetadata && metadata.isAnnotated(Bean.class.getName()), this::deducedBeanTypeForBeanMethod, Collections::emptySet);
        }

        private Set<String> deducedBeanTypeForBeanMethod() {
            MethodMetadata methodMetadata = (MethodMetadata) metadata;
            try {
                return SetUtils.newHashSet(getReturnType(methodMetadata).getName());
            } catch (Throwable ex) {
                throw new BeanTypeDeductionException(methodMetadata.getDeclaringClassName(), methodMetadata.getMethodName(), ex);
            }
        }

        private Class<?> getReturnType(MethodMetadata metadata) throws ClassNotFoundException, LinkageError {
            Class<?> returnType = resolve(metadata.getReturnTypeName(), context.getClassLoader());
            return BooleanUtils.defaultIfFalse(isParameterizedContainer(returnType), () -> {
                try {
                    return getReturnTypeGeneric(metadata);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(e);
                }
            }, returnType);
        }

        private boolean isParameterizedContainer(Class<?> type) {
            for (Class<?> parameterizedContainer : parameterizedContainers) {
                if (parameterizedContainer.isAssignableFrom(type)) {
                    return true;
                }
            }
            return false;
        }

        private Class<?> getReturnTypeGeneric(MethodMetadata metadata) throws ClassNotFoundException, LinkageError {
            Class<?> declaringClass = resolve(metadata.getDeclaringClassName(), context.getClassLoader());
            return ResolvableType.forMethodReturnType(findBeanMethod(declaringClass, metadata.getMethodName())).resolveGeneric();
        }

        private Method findBeanMethod(Class<?> declaringClass, String methodName) {
            Method method = ReflectionUtils.findMethod(declaringClass, methodName);
            if (isBeanMethod(method)) {
                return method;
            }
            Method[] candidates = ReflectionUtils.getAllDeclaredMethods(declaringClass);
            for (Method candidate : candidates) {
                if (candidate.getName().equals(methodName) && isBeanMethod(candidate)) {
                    return candidate;
                }
            }
            throw new IllegalStateException("Unable to find bean method " + methodName);
        }

        private boolean isBeanMethod(Method method) {
            return method != null && MergedAnnotations.from(method, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).isPresent(Bean.class);
        }

        private SearchStrategy getStrategy() {
            return ObjectUtils.defaultIfNull(strategy, SearchStrategy.ALL);
        }

        Set<String> getNames() {
            return names;
        }

        Set<String> getTypes() {
            return types;
        }

        Set<String> getAnnotations() {
            return annotations;
        }

        Set<String> getIgnoredTypes() {
            return ignoredTypes;
        }

        Set<Class<?>> getParameterizedContainers() {
            return parameterizedContainers;
        }

        ConditionMessage.Builder message() {
            return ConditionMessage.forCondition(annotationType, this);
        }

        ConditionMessage.Builder message(ConditionMessage message) {
            return message.andCondition(annotationType, this);
        }

        @Override
        public String toString() {
            boolean hasNames = !names.isEmpty();
            boolean hasTypes = !types.isEmpty();
            boolean hasIgnoredTypes = !ignoredTypes.isEmpty();
            StringBuilder string = new StringBuilder();
            string.append(StringConstants.LEFT_PARENTHESES);
            if (hasNames) {
                string.append("names: ");
                string.append(CollectionUtils.toCommaDelimitedString(names));
                string.append(BooleanUtils.defaultIfFalse(hasTypes, StringConstants.SPACE, StringConstants.SEMICOLON_SPACE));
            }
            if (hasTypes) {
                string.append("types: ");
                string.append(CollectionUtils.toCommaDelimitedString(types));
                string.append(BooleanUtils.defaultIfFalse(hasIgnoredTypes, StringConstants.SPACE, StringConstants.SEMICOLON_SPACE));
            }
            if (hasIgnoredTypes) {
                string.append("ignored: ");
                string.append(CollectionUtils.toCommaDelimitedString(ignoredTypes));
                string.append(StringConstants.SEMICOLON_SPACE);
            }
            string.append("SearchStrategy: ");
            string.append(strategy.toString().toLowerCase(Locale.ENGLISH));
            string.append(StringConstants.RIGHT_PARENTHESES);
            return string.toString();
        }
    }

    private static class SingleCandidateSpec extends Spec<ConditionalOnSingleCandidate> {

        private static final Collection<String> FILTERED_TYPES = Arrays.asList(StringConstants.EMPTY, Object.class.getName());

        SingleCandidateSpec(ConditionContext context, AnnotatedTypeMetadata metadata, MergedAnnotations annotations) {
            super(context, metadata, annotations, ConditionalOnSingleCandidate.class);
        }

        @Override
        protected Set<String> extractTypes(MultiValueMap<String, Object> attributes) {
            Set<String> types = super.extractTypes(attributes);
            types.removeAll(FILTERED_TYPES);
            return types;
        }

        @Override
        protected void validate(BeanTypeDeductionException ex) {
            Assert.isTrue(getTypes().size() == 1, () -> getAnnotationName() + " annotations must specify only one type (got " + CollectionUtils.toCommaDelimitedString(getTypes()) + StringConstants.RIGHT_PARENTHESES);
        }
    }

    private static final class MatchResult {

        private final Map<String, Collection<String>> matchedAnnotations = MapUtils.newHashMap();

        private final List<String> matchedNames = ListUtils.newArrayList();

        private final Map<String, Collection<String>> matchedTypes = MapUtils.newHashMap();

        private final List<String> unmatchedAnnotations = ListUtils.newArrayList();

        private final List<String> unmatchedNames = ListUtils.newArrayList();

        private final List<String> unmatchedTypes = ListUtils.newArrayList();

        private final Set<String> namesOfAllMatches = SetUtils.newHashSet();

        private void recordMatchedName(String name) {
            matchedNames.add(name);
            namesOfAllMatches.add(name);
        }

        private void recordUnmatchedName(String name) {
            unmatchedNames.add(name);
        }

        private void recordMatchedAnnotation(String annotation, Collection<String> matchingNames) {
            matchedAnnotations.put(annotation, matchingNames);
            namesOfAllMatches.addAll(matchingNames);
        }

        private void recordUnmatchedAnnotation(String annotation) {
            unmatchedAnnotations.add(annotation);
        }

        private void recordMatchedType(String type, Collection<String> matchingNames) {
            matchedTypes.put(type, matchingNames);
            namesOfAllMatches.addAll(matchingNames);
        }

        private void recordUnmatchedType(String type) {
            unmatchedTypes.add(type);
        }

        boolean isAllMatched() {
            return unmatchedAnnotations.isEmpty() && unmatchedNames.isEmpty() && unmatchedTypes.isEmpty();
        }

        boolean isAnyMatched() {
            return !matchedAnnotations.isEmpty() || !matchedNames.isEmpty() || !matchedTypes.isEmpty();
        }

        Map<String, Collection<String>> getMatchedAnnotations() {
            return matchedAnnotations;
        }

        List<String> getMatchedNames() {
            return matchedNames;
        }

        Map<String, Collection<String>> getMatchedTypes() {
            return matchedTypes;
        }

        List<String> getUnmatchedAnnotations() {
            return unmatchedAnnotations;
        }

        List<String> getUnmatchedNames() {
            return unmatchedNames;
        }

        List<String> getUnmatchedTypes() {
            return unmatchedTypes;
        }

        Set<String> getNamesOfAllMatches() {
            return namesOfAllMatches;
        }
    }

    static final class BeanTypeDeductionException extends RuntimeException {
        private static final long serialVersionUID = 8545104371253631861L;

        private BeanTypeDeductionException(String className, String beanMethodName, Throwable cause) {
            super("Failed to deduce bean type for " + className + StringConstants.DOT + beanMethodName, cause);
        }
    }
}