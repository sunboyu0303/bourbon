package com.github.bourbon.springframework.aop.framework.autoproxy;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.springframework.aop.AdvisedSupport;
import com.github.bourbon.springframework.aop.Advisor;
import com.github.bourbon.springframework.aop.Pointcut;
import com.github.bourbon.springframework.aop.TargetSource;
import com.github.bourbon.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.github.bourbon.springframework.aop.framework.ProxyFactory;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.PropertyValues;
import com.github.bourbon.springframework.beans.factory.BeanFactory;
import com.github.bourbon.springframework.beans.factory.BeanFactoryAware;
import com.github.bourbon.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.github.bourbon.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 17:21
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Set<Object> earlyProxyReferences = SetUtils.synchronizedSet(SetUtils.newHashSet());
    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return earlyProxyReferences.contains(beanName) ? bean : wrapIfNecessary(bean, beanName);
    }

    private Object wrapIfNecessary(Object bean, String beanName) throws BeansException {

        Class<?> beanClass = bean.getClass();

        if (isInfrastructureClass(beanClass)) {
            return bean;
        }

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            if (!advisor.getPointcut().getClassFilter().matches(beanClass)) {
                continue;
            }

            try {
                return new ProxyFactory(new AdvisedSupport().setTargetSource(new TargetSource(bean)).setMethodInterceptor((MethodInterceptor) advisor.getAdvice()).setMethodMatcher(advisor.getPointcut().getMethodMatcher()).setProxyTargetClass(true)).getProxy();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        return bean;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }
}