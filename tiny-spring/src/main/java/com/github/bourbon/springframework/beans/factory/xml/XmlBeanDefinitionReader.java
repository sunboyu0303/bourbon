package com.github.bourbon.springframework.beans.factory.xml;

import com.github.bourbon.base.constant.StringConstants;
import com.github.bourbon.base.io.Resource;
import com.github.bourbon.base.io.ResourceLoader;
import com.github.bourbon.base.utils.CharSequenceUtils;
import com.github.bourbon.base.utils.ObjectUtils;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.PropertyValue;
import com.github.bourbon.springframework.beans.factory.config.BeanDefinition;
import com.github.bourbon.springframework.beans.factory.config.BeanReference;
import com.github.bourbon.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.github.bourbon.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import com.github.bourbon.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.github.bourbon.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/5 15:46
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try (InputStream inputStream = resource.getInputStream()) {
            doLoadBeanDefinitions(inputStream);
        } catch (IOException | ClassNotFoundException | DocumentException e) {
            throw new BeansException("Exception parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        Arrays.stream(resources).forEach(this::loadBeanDefinitions);
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        loadBeanDefinitions(getResourceLoader().getResource(location));
    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        Arrays.stream(locations).forEach(this::loadBeanDefinitions);
    }

    private void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException, DocumentException {
        Element root = new SAXReader().read(inputStream).getRootElement();

        Element componentScan = root.element("component-scan");
        if (null != componentScan) {
            String scanPath = componentScan.attributeValue("base-package");
            if (CharSequenceUtils.isEmpty(scanPath)) {
                throw new BeansException("The value of base-package attribute can not be empty or null");
            }
            new ClassPathBeanDefinitionScanner(getRegistry()).doScan(CharSequenceUtils.splitToArray(scanPath, StringConstants.COMMA));
        }

        List<Element> beanList = root.elements("bean");

        for (Element bean : beanList) {
            String id = bean.attributeValue("id");
            String name = bean.attributeValue("name");
            String className = bean.attributeValue("class");
            String initMethod = bean.attributeValue("init-method");
            String destroyMethodName = bean.attributeValue("destroy-method");
            String beanScope = bean.attributeValue("scope");

            Class<?> clazz = Class.forName(className);
            String beanName = CharSequenceUtils.isEmpty(id) ? name : id;
            if (CharSequenceUtils.isEmpty(beanName)) {
                beanName = CharSequenceUtils.lowerFirst(clazz.getSimpleName());
            }
            BeanDefinition beanDefinition = BeanDefinition.of(clazz).setInitMethodName(initMethod).setDestroyMethodName(destroyMethodName);

            if (!CharSequenceUtils.isEmpty(beanScope)) {
                ConfigurableBeanFactory.Scope scope = ConfigurableBeanFactory.Scope.getScope(beanScope);
                if (!ObjectUtils.isNull(scope)) {
                    beanDefinition.setScope(scope);
                }
            }

            List<Element> propertyList = bean.elements("property");
            for (Element property : propertyList) {
                String attrName = property.attributeValue("name");
                String attrValue = property.attributeValue("value");
                String attrRef = property.attributeValue("ref");
                beanDefinition.getPropertyValues().addPropertyValue(PropertyValue.of(attrName, CharSequenceUtils.isEmpty(attrRef) ? attrValue : BeanReference.of(attrRef)));
            }

            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }
}