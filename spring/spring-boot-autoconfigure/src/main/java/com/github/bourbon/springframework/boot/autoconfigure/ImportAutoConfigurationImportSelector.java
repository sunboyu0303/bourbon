package com.github.bourbon.springframework.boot.autoconfigure;

import com.github.bourbon.base.utils.*;
import com.github.bourbon.springframework.boot.context.annotation.DeterminableImports;
import com.github.bourbon.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/24 14:03
 */
class ImportAutoConfigurationImportSelector extends AutoConfigurationImportSelector implements DeterminableImports {

    private static final Set<String> ANNOTATION_NAMES = Collections.unmodifiableSet(SetUtils.newHashSet(
            ImportAutoConfiguration.class.getName(), "org.springframework.boot.autoconfigure.test.ImportAutoConfiguration"
    ));

    @Override
    public Set<Object> determineImports(AnnotationMetadata metadata) {
        Set<String> result = SetUtils.newLinkedHashSet(getCandidateConfigurations(metadata, null));
        result.removeAll(getExclusions(metadata, null));
        return Collections.unmodifiableSet(result);
    }

    @Override
    protected AnnotationAttributes getAttributes(AnnotationMetadata metadata) {
        return null;
    }

    @Override
    protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
        List<String> candidates = new ArrayList<>();
        getAnnotations(metadata).forEach((source, annotations) -> collectCandidateConfigurations(source, annotations, candidates));
        return candidates;
    }

    private void collectCandidateConfigurations(Class<?> source, List<Annotation> annotations, List<String> candidates) {
        annotations.forEach(annotation -> candidates.addAll(getConfigurationsForAnnotation(source, annotation)));
    }

    private Collection<String> getConfigurationsForAnnotation(Class<?> source, Annotation annotation) {
        return BooleanUtils.defaultSupplierIfPredicate((String[]) AnnotationUtils.getAnnotationAttributes(annotation, true).get("classes"),
                c -> c.length > 0, ListUtils::newArrayList, () -> loadFactoryNames(source)
        );
    }

    protected Collection<String> loadFactoryNames(Class<?> source) {
        return SpringFactoriesLoader.loadFactoryNames(source, getBeanClassLoader());
    }

    @Override
    protected Set<String> getExclusions(AnnotationMetadata metadata, AnnotationAttributes attributes) {
        Set<String> exclusions = new LinkedHashSet<>();
        Class<?> source = ClassUtils.resolveClassName(metadata.getClassName());
        for (String name : ANNOTATION_NAMES) {
            Class<?>[] exclude = ObjectUtils.defaultIfNull(AnnotatedElementUtils.getMergedAnnotationAttributes(source, name), m -> m.getClassArray("exclude"));
            if (!ArrayUtils.isEmpty(exclude)) {
                exclusions.addAll(Arrays.stream(exclude).map(Class::getName).collect(Collectors.toList()));
            }
        }
        for (List<Annotation> annotations : getAnnotations(metadata).values()) {
            for (Annotation annotation : annotations) {
                String[] exclude = (String[]) AnnotationUtils.getAnnotationAttributes(annotation, true).get("exclude");
                if (!ArrayUtils.isEmpty(exclude)) {
                    exclusions.addAll(ListUtils.newArrayList(exclude));
                }
            }
        }
        exclusions.addAll(getExcludeAutoConfigurationsProperty());
        return exclusions;
    }

    protected final Map<Class<?>, List<Annotation>> getAnnotations(AnnotationMetadata metadata) {
        MultiValueMap<Class<?>, Annotation> annotations = new LinkedMultiValueMap<>();
        collectAnnotations(ClassUtils.resolveClassName(metadata.getClassName()), annotations, new HashSet<>());
        return Collections.unmodifiableMap(annotations);
    }

    private void collectAnnotations(Class<?> source, MultiValueMap<Class<?>, Annotation> annotations, HashSet<Class<?>> seen) {
        if (source != null && seen.add(source)) {
            for (Annotation annotation : source.getDeclaredAnnotations()) {
                if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotation)) {
                    if (ANNOTATION_NAMES.contains(annotation.annotationType().getName())) {
                        annotations.add(source, annotation);
                    }
                    collectAnnotations(annotation.annotationType(), annotations, seen);
                }
            }
            collectAnnotations(source.getSuperclass(), annotations, seen);
        }
    }

    @Override
    public int getOrder() {
        return super.getOrder() - 1;
    }

    @Override
    protected void handleInvalidExcludes(List<String> invalidExcludes) {
        // Ignore for test
    }
}