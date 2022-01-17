package com.github.bourbon.springframework.boot.autoconfigure.condition;

import com.github.bourbon.base.utils.BooleanUtils;
import com.github.bourbon.base.utils.ClassLoaderUtils;
import com.github.bourbon.base.utils.CollectionUtils;
import com.github.bourbon.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import com.github.bourbon.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/15 11:08
 */
abstract class FilteringSpringBootCondition extends SpringBootCondition implements AutoConfigurationImportFilter,
        BeanFactoryAware, BeanClassLoaderAware, EnvironmentAware, ResourceLoaderAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    private ClassLoader classLoader;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return classLoader;
    }

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        ConditionEvaluationReport report = ConditionEvaluationReport.find(context.getBeanFactory());
        ConditionOutcome[] outcomes = getOutcomes(autoConfigurationClasses, autoConfigurationMetadata);
        boolean[] match = new boolean[outcomes.length];
        for (int i = 0; i < outcomes.length; i++) {
            match[i] = outcomes[i] == null || outcomes[i].isMatch();
            if (!match[i] && outcomes[i] != null) {
                logOutcome(autoConfigurationClasses[i], outcomes[i]);
                if (report != null) {
                    report.recordConditionEvaluation(autoConfigurationClasses[i], this, outcomes[i]);
                }
            }
        }
        return match;
    }

    protected abstract ConditionOutcome[] getOutcomes(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata);

    protected final List<String> filter(Collection<String> collection, ClassNameFilter filter) {
        return BooleanUtils.defaultSupplierIfPredicate(collection, c -> !CollectionUtils.isEmpty(c),
                c -> c.stream().filter(n -> filter.matches(n, context.getClassLoader())).collect(Collectors.toList()), Collections::emptyList
        );
    }

    static Class<?> resolve(String className, ClassLoader classLoader) throws ClassNotFoundException {
        return classLoader != null ? Class.forName(className, false, classLoader) : Class.forName(className);
    }

    protected enum ClassNameFilter {
        PRESENT {
            @Override
            public boolean matches(String className, ClassLoader classLoader) {
                return isPresent(className, classLoader);
            }
        },
        MISSING {
            @Override
            public boolean matches(String className, ClassLoader classLoader) {
                return !isPresent(className, classLoader);
            }
        };

        abstract boolean matches(String className, ClassLoader classLoader);

        static boolean isPresent(String className, ClassLoader classLoader) {
            if (classLoader == null) {
                classLoader = ClassLoaderUtils.getClassLoader();
            }
            try {
                resolve(className, classLoader);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}