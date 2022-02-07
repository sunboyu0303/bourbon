package com.github.bourbon.springframework.boot.autoconfigure;

import com.github.bourbon.base.lang.Assert;
import com.github.bourbon.base.utils.*;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/24 11:01
 */
class AutoConfigurationSorter {

    private final MetadataReaderFactory metadataReaderFactory;

    private final AutoConfigurationMetadata autoConfigurationMetadata;

    AutoConfigurationSorter(MetadataReaderFactory metadataReaderFactory, AutoConfigurationMetadata autoConfigurationMetadata) {
        Assert.notNull(metadataReaderFactory, "MetadataReaderFactory must not be null");
        this.metadataReaderFactory = metadataReaderFactory;
        this.autoConfigurationMetadata = autoConfigurationMetadata;
    }

    List<String> getInPriorityOrder(Collection<String> classNames) {
        AutoConfigurationClasses classes = new AutoConfigurationClasses(metadataReaderFactory, autoConfigurationMetadata, classNames);
        List<String> orderedClassNames = ListUtils.newArrayList(classNames);
        Collections.sort(orderedClassNames);
        orderedClassNames.sort(Comparator.comparingInt(o -> classes.get(o).getOrder()));
        orderedClassNames = sortByAnnotation(classes, orderedClassNames);
        return orderedClassNames;
    }

    private List<String> sortByAnnotation(AutoConfigurationClasses classes, List<String> classNames) {
        List<String> toSort = ListUtils.newArrayList(classNames);
        toSort.addAll(classes.getAllNames());
        Set<String> sorted = new LinkedHashSet<>();
        Set<String> processing = new LinkedHashSet<>();
        while (!toSort.isEmpty()) {
            doSortByAfterAnnotation(classes, toSort, sorted, processing, null);
        }
        sorted.retainAll(classNames);
        return ListUtils.newArrayList(sorted);
    }

    private void doSortByAfterAnnotation(AutoConfigurationClasses classes, List<String> toSort, Set<String> sorted, Set<String> processing, String current) {
        if (current == null) {
            current = toSort.remove(0);
        }
        processing.add(current);
        for (String after : classes.getClassesRequestedAfter(current)) {
            checkForCycles(processing, current, after);
            if (!sorted.contains(after) && toSort.contains(after)) {
                doSortByAfterAnnotation(classes, toSort, sorted, processing, after);
            }
        }
        processing.remove(current);
        sorted.add(current);
    }

    private void checkForCycles(Set<String> processing, String current, String after) {
        Assert.notTrue(processing.contains(after), () -> "AutoConfigure cycle detected between " + current + " and " + after);
    }

    private static class AutoConfigurationClasses {

        private final Map<String, AutoConfigurationClass> classes = MapUtils.newHashMap();

        AutoConfigurationClasses(MetadataReaderFactory metadataReaderFactory, AutoConfigurationMetadata autoConfigurationMetadata, Collection<String> classNames) {
            addToClasses(metadataReaderFactory, autoConfigurationMetadata, classNames, true);
        }

        Set<String> getAllNames() {
            return classes.keySet();
        }

        private void addToClasses(MetadataReaderFactory metadataReaderFactory, AutoConfigurationMetadata autoConfigurationMetadata, Collection<String> classNames, boolean required) {
            for (String className : classNames) {
                if (!classes.containsKey(className)) {
                    AutoConfigurationClass autoConfigurationClass = new AutoConfigurationClass(className, metadataReaderFactory, autoConfigurationMetadata);
                    boolean available = autoConfigurationClass.isAvailable();
                    if (required || available) {
                        classes.put(className, autoConfigurationClass);
                    }
                    if (available) {
                        addToClasses(metadataReaderFactory, autoConfigurationMetadata, autoConfigurationClass.getBefore(), false);
                        addToClasses(metadataReaderFactory, autoConfigurationMetadata, autoConfigurationClass.getAfter(), false);
                    }
                }
            }
        }

        AutoConfigurationClass get(String className) {
            return classes.get(className);
        }

        Set<String> getClassesRequestedAfter(String className) {
            Set<String> classesRequestedAfter = SetUtils.newLinkedHashSet(get(className).getAfter());
            classes.forEach((name, autoConfigurationClass) -> {
                if (autoConfigurationClass.getBefore().contains(className)) {
                    classesRequestedAfter.add(name);
                }
            });
            return classesRequestedAfter;
        }
    }

    private static class AutoConfigurationClass {

        private final String className;

        private final MetadataReaderFactory metadataReaderFactory;

        private final AutoConfigurationMetadata autoConfigurationMetadata;

        private volatile AnnotationMetadata annotationMetadata;

        private volatile Set<String> before;

        private volatile Set<String> after;

        AutoConfigurationClass(String className, MetadataReaderFactory metadataReaderFactory, AutoConfigurationMetadata autoConfigurationMetadata) {
            this.className = className;
            this.metadataReaderFactory = metadataReaderFactory;
            this.autoConfigurationMetadata = autoConfigurationMetadata;
        }

        boolean isAvailable() {
            try {
                if (!wasProcessed()) {
                    getAnnotationMetadata();
                }
                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        Set<String> getBefore() {
            if (before == null) {
                before = BooleanUtils.defaultSupplierIfFalse(wasProcessed(),
                        () -> autoConfigurationMetadata.getSet(className, AutoConfigureBefore.class.getSimpleName(), Collections.emptySet()),
                        () -> getAnnotationValue(AutoConfigureBefore.class)
                );
            }
            return before;
        }

        Set<String> getAfter() {
            if (after == null) {
                after = BooleanUtils.defaultSupplierIfFalse(wasProcessed(),
                        () -> autoConfigurationMetadata.getSet(className, AutoConfigureAfter.class.getSimpleName(), Collections.emptySet()),
                        () -> getAnnotationValue(AutoConfigureAfter.class)
                );
            }
            return after;
        }

        private int getOrder() {
            return BooleanUtils.defaultSupplierIfFalse(wasProcessed(),
                    () -> autoConfigurationMetadata.getInteger(className, AutoConfigureOrder.class.getSimpleName(), AutoConfigureOrder.DEFAULT_ORDER),
                    () -> ObjectUtils.defaultIfNullElseFunction(AnnotationHelperUtils.getAnnotationAttributes(getAnnotationMetadata(), AutoConfigureOrder.class),
                            annotationAttributes -> annotationAttributes.getNumber("value"), AutoConfigureOrder.DEFAULT_ORDER
                    )
            );
        }

        private boolean wasProcessed() {
            return ObjectUtils.defaultIfNullElseFunction(autoConfigurationMetadata, metadata -> metadata.wasProcessed(className), false);
        }

        private Set<String> getAnnotationValue(Class<? extends Annotation> annotation) {
            return ObjectUtils.defaultIfNullElseFunction(AnnotationHelperUtils.getAnnotationAttributes(getAnnotationMetadata(), annotation, true), attributes -> {
                Set<String> value = SetUtils.newLinkedHashSet();
                Collections.addAll(value, attributes.getStringArray("value"));
                Collections.addAll(value, attributes.getStringArray("name"));
                return value;
            }, Collections.emptySet());
        }

        private AnnotationMetadata getAnnotationMetadata() {
            if (annotationMetadata == null) {
                try {
                    annotationMetadata = metadataReaderFactory.getMetadataReader(className).getAnnotationMetadata();
                } catch (IOException ex) {
                    throw new IllegalStateException("Unable to read meta-data for class " + className, ex);
                }
            }
            return annotationMetadata;
        }
    }
}