package com.github.bourbon.springframework.context.support;

import com.github.bourbon.base.io.support.DefaultResourceLoader;
import com.github.bourbon.base.utils.ClassUtils;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.github.bourbon.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.github.bourbon.springframework.beans.factory.config.BeanPostProcessor;
import com.github.bourbon.springframework.context.ApplicationEvent;
import com.github.bourbon.springframework.context.ApplicationListener;
import com.github.bourbon.springframework.context.ConfigurableApplicationContext;
import com.github.bourbon.springframework.context.event.ApplicationEventMulticaster;
import com.github.bourbon.springframework.context.event.ContextClosedEvent;
import com.github.bourbon.springframework.context.event.ContextRefreshedEvent;
import com.github.bourbon.springframework.context.event.SimpleApplicationEventMulticaster;
import com.github.bourbon.springframework.core.convert.ConversionService;

import java.util.Map;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 23:18
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    private static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void refresh() throws BeansException {
        // 1. 创建 BeanFactory，并加载 BeanDefinition
        refreshBeanFactory();

        // 2. 获取 BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 3. 添加 ApplicationContextAwareProcessor，让继承自 ApplicationContextAware 的 Bean 对象都能感知所属的 ApplicationContext
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 4. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // 5. BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
        registerBeanPostProcessors(beanFactory);

        // 6. 初始化事件发布者
        initApplicationEventMulticaster();

        // 7. 注册事件监听器
        registerListeners();

        // 8. 设置类型转换器、提前实例化单例Bean对象
        finishBeanFactoryInitialization(beanFactory);

        // 9. 发布容器刷新完成事件
        finishRefresh();
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory bf) {
        bf.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(p -> p.postProcessBeanFactory(bf));
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        bf.getBeansOfType(BeanPostProcessor.class).values().forEach(bf::addBeanPostProcessor);
    }

    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    @SuppressWarnings("unchecked")
    private void registerListeners() {
        getBeansOfType(ApplicationListener.class).values().forEach(l -> applicationEventMulticaster.addApplicationListener(l));
    }

    private void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        String beanName = ClassUtils.lowerFirst(ConversionService.class);
        if (beanFactory.containsBean(beanName)) {
            Object conversionService = beanFactory.getBean(beanName);
            if (conversionService instanceof ConversionService) {
                beanFactory.setConversionService((ConversionService) conversionService);
            }
        }
        beanFactory.preInstantiateSingletons();
    }

    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        publishEvent(new ContextClosedEvent(this));
        getBeanFactory().destroySingletons();
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public Object getBean(String beanName) {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return getBeanFactory().getBean(beanName, args);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> type) {
        return getBeanFactory().getBean(beanName, type);
    }

    @Override
    public <T> T getBean(Class<T> type) {
        return getBeanFactory().getBean(type);
    }

    @Override
    public boolean containsBean(String beanName) {
        return getBeanFactory().containsBean(beanName);
    }
}