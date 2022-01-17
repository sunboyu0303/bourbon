package com.github.bourbon.springframework.boot.context.properties;

import com.github.bourbon.base.utils.ClassUtils;
import com.github.bourbon.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/27 20:06
 */
final class ConfigurationPropertiesJsr303Validator implements Validator {

    private static final String[] VALIDATOR_CLASSES = {"javax.validation.Validator",
            "javax.validation.ValidatorFactory", "javax.validation.bootstrap.GenericBootstrap"};

    private final Delegate delegate;

    ConfigurationPropertiesJsr303Validator(ApplicationContext applicationContext) {
        delegate = new Delegate(applicationContext);
    }

    @Override
    public boolean supports(Class<?> type) {
        return delegate.supports(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        delegate.validate(target, errors);
    }

    static boolean isJsr303Present(ApplicationContext applicationContext) {
        ClassLoader classLoader = applicationContext.getClassLoader();
        for (String validatorClass : VALIDATOR_CLASSES) {
            if (!ClassUtils.isPresent(validatorClass, classLoader)) {
                return false;
            }
        }
        return true;
    }

    private static class Delegate extends LocalValidatorFactoryBean {

        Delegate(ApplicationContext applicationContext) {
            setApplicationContext(applicationContext);
            setMessageInterpolator(new MessageInterpolatorFactory(applicationContext).getObject());
            afterPropertiesSet();
        }
    }
}