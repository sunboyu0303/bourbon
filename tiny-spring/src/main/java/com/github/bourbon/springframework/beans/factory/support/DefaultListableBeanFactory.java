package com.github.bourbon.springframework.beans.factory.support;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.github.bourbon.springframework.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 22:03
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No bean named '" + beanName + "' is defined");
        }
        return beanDefinition;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<String, BeanDefinition> e : beanDefinitionMap.entrySet()) {
            if (type.isAssignableFrom(e.getValue().getClazz())) {
                String beanName = e.getKey();
                result.put(beanName, (T) getBean(beanName));
            }
        }
        return result;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(StringConstants.EMPTY_STRING_ARRAY);
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }

    @Override
    public <T> T getBean(Class<T> type) throws BeansException {
        List<String> beanNames = beanDefinitionMap.entrySet().stream().filter(entry -> type.isAssignableFrom(entry.getValue().getClazz())).map(Map.Entry::getKey).collect(Collectors.toList());
        if (1 == beanNames.size()) {
            return getBean(beanNames.get(0), type);
        }
        throw new BeansException(type + "expected single bean but found " + beanNames.size() + ": " + beanNames);
    }
}