package com.github.bourbon.springframework.boot.autoconfigure;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.*;
import com.github.bourbon.springframework.boot.context.annotation.DeterminableImports;
import com.github.bourbon.springframework.context.annotation.InfrastructureBeans;
import com.github.bourbon.springframework.core.annotation.AnnotationHelperUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/19 17:45
 */
public final class AutoConfigurationPackages {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoConfigurationPackages.class);

    private static final String BEAN = AutoConfigurationPackages.class.getName();

    public static boolean has(BeanFactory bf) {
        return bf.containsBean(BEAN) && !get(bf).isEmpty();
    }

    public static List<String> get(BeanFactory beanFactory) {
        try {
            return beanFactory.getBean(BEAN, BasePackages.class).get();
        } catch (NoSuchBeanDefinitionException ex) {
            throw new IllegalStateException("Unable to retrieve @EnableAutoConfiguration base packages");
        }
    }

    public static void register(BeanDefinitionRegistry registry, String... packageNames) {
        if (registry.containsBeanDefinition(BEAN)) {
            ((BasePackagesBeanDefinition) registry.getBeanDefinition(BEAN)).addBasePackages(packageNames);
        } else {
            registry.registerBeanDefinition(BEAN, new BasePackagesBeanDefinition(packageNames));
        }
    }

    static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {

        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            register(registry, new PackageImports(metadata).getPackageNames().toArray(StringConstants.EMPTY_STRING_ARRAY));
            InfrastructureBeans.register(registry);
        }

        @Override
        public Set<Object> determineImports(AnnotationMetadata metadata) {
            return SetUtils.newHashSet(new PackageImports(metadata));
        }
    }

    private static final class PackageImports {

        private final List<String> packageNames;

        PackageImports(AnnotationMetadata metadata) {
            AutoConfigurationPackage annotation = AnnotationHelperUtils.getAnnotation(metadata, AutoConfigurationPackage.class);
            List<String> packageNames = ListUtils.newArrayList(annotation.basePackages());
            packageNames.addAll(Arrays.stream(annotation.basePackageClasses()).map(c -> c.getPackage().getName()).collect(Collectors.toList()));
            if (packageNames.isEmpty()) {
                packageNames.add(ClassUtils.getPackageName(metadata.getClassName()));
            }
            this.packageNames = ListUtils.unmodifiableList(packageNames);
        }

        List<String> getPackageNames() {
            return packageNames;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return packageNames.equals(((PackageImports) obj).packageNames);
        }

        @Override
        public int hashCode() {
            return packageNames.hashCode();
        }

        @Override
        public String toString() {
            return "Package Imports " + packageNames;
        }
    }

    static final class BasePackages {

        private final List<String> packages;

        private boolean loggedBasePackageInfo;

        BasePackages(String... names) {
            packages = Arrays.stream(names).filter(n -> !CharSequenceUtils.isBlank(n)).collect(Collectors.toList());
        }

        List<String> get() {
            if (!loggedBasePackageInfo) {
                if (packages.isEmpty()) {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn("@EnableAutoConfiguration was declared on a class in the default package. Automatic @Repository and @Entity scanning is not enabled.");
                    }
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("@EnableAutoConfiguration was declared on a class in the package '{}'. Automatic @Repository and @Entity scanning is enabled.", CollectionUtils.toCommaDelimitedString(packages));
                    }
                }
                loggedBasePackageInfo = true;
            }
            return packages;
        }
    }

    static final class BasePackagesBeanDefinition extends GenericBeanDefinition {

        private static final long serialVersionUID = 1370475784791936888L;
        private final Set<String> basePackages = new LinkedHashSet<>();

        BasePackagesBeanDefinition(String... basePackages) {
            setBeanClass(BasePackages.class);
            setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            addBasePackages(basePackages);
        }

        @Override
        public Supplier<?> getInstanceSupplier() {
            return () -> new BasePackages(CollectionUtils.toStringArray(basePackages));
        }

        private void addBasePackages(String[] additionalBasePackages) {
            basePackages.addAll(ListUtils.newArrayList(additionalBasePackages));
        }
    }

    private AutoConfigurationPackages() {
    }
}