package com.github.bourbon.springframework.beans;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.springframework.beans.bean.MyBeanFactoryPostProcessor;
import com.github.bourbon.springframework.beans.bean.MyBeanPostProcessor;
import com.github.bourbon.springframework.beans.bean.UserDao;
import com.github.bourbon.springframework.beans.bean.UserService;
import com.github.bourbon.springframework.beans.event.CustomEvent;
import com.github.bourbon.springframework.beans.factory.config.BeanDefinition;
import com.github.bourbon.springframework.beans.factory.config.BeanReference;
import com.github.bourbon.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.github.bourbon.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.github.bourbon.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 14:00
 */
public class BeansTest {

    private final Logger logger = LoggerFactory.getLogger(BeansTest.class);

    @Test
    public void test_BeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        beanFactory.registerBeanDefinition("userDao", BeanDefinition.of(UserDao.class));

        PropertyValues propertyValues = new PropertyValues()
                .addPropertyValue(PropertyValue.of("uId", "10001"))
                .addPropertyValue(PropertyValue.of("userDao", BeanReference.of("userDao")));

        beanFactory.registerBeanDefinition("userService", BeanDefinition.of(UserService.class, propertyValues));

        UserService userService = (UserService) beanFactory.getBean("userService");
        logger.info("测试结果：" + userService.queryUserInfo());
        Assert.assertNotNull(userService.queryUserInfo());
    }

    @Test
    public void test_xml() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        new XmlBeanDefinitionReader(beanFactory).loadBeanDefinitions("classpath:spring.xml");

        UserService userService = beanFactory.getBean("userService", UserService.class);
        logger.info("测试结果：" + userService.queryUserInfo());
        Assert.assertNotNull(userService.queryUserInfo());

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutdownHook();
        userService = applicationContext.getBean("userService", UserService.class);
        logger.info("测试结果：" + userService.queryUserInfo());
        logger.info("ApplicationContextAware：" + userService.getApplicationContext());
        logger.info("BeanFactoryAware：" + userService.getBeanFactory());
        logger.info("BeanClassLoaderAware：" + userService.getBeanClassLoader());
        logger.info("BeanNameAware：" + userService.getBeanName());
        Assert.assertNotNull(userService.queryUserInfo());
    }

    @Test
    public void test_BeanFactoryPostProcessorAndBeanPostProcessor() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        new XmlBeanDefinitionReader(beanFactory).loadBeanDefinitions("classpath:spring.xml");
        new MyBeanFactoryPostProcessor().postProcessBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(new MyBeanPostProcessor());
        UserService userService = beanFactory.getBean("userService", UserService.class);
        logger.info("测试结果：" + userService.queryUserInfo());
        Assert.assertNotNull(userService.queryUserInfo());
    }

    @Test
    public void test_prototype() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springScope.xml");
        applicationContext.registerShutdownHook();

        UserService userService01 = applicationContext.getBean("userService", UserService.class);
        UserService userService02 = applicationContext.getBean("userService", UserService.class);

        logger.info(userService01);
        logger.info(userService02);

        Assert.assertNotEquals(userService01, userService02);
    }

    @Test
    public void test_factory_bean() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springScope.xml");
        applicationContext.registerShutdownHook();
        UserService userService = applicationContext.getBean("userService", UserService.class);
        logger.info("测试结果：" + userService.queryUserInfo());
        Assert.assertNotNull(userService.queryUserInfo());
    }

    @Test
    public void test_event() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springEvent.xml");
        Assert.assertNotNull(applicationContext);
        applicationContext.publishEvent(new CustomEvent(applicationContext, 1019129009086763L, "成功了！"));
        applicationContext.registerShutdownHook();
    }
}