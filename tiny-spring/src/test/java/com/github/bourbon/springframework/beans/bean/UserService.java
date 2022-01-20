package com.github.bourbon.springframework.beans.bean;

import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.springframework.beans.factory.*;
import com.github.bourbon.springframework.context.ApplicationContext;
import com.github.bourbon.springframework.context.ApplicationContextAware;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 13:59
 */
public class UserService implements InitializingBean, DisposableBean, ApplicationContextAware, BeanFactoryAware, BeanNameAware, BeanClassLoaderAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;
    private String beanName;
    private ClassLoader beanClassLoader;

    private String uId;
    private String company;
    private String location;
    private IUserDao userDao;

    public String queryUserInfo() {
        return userDao.queryUserName(uId) + ", " + company + ", " + location;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public String getBeanName() {
        return beanName;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUserDao(IUserDao iUserDao) {
        this.userDao = iUserDao;
    }

    @Override
    public void destroy() {
        LOGGER.info("执行：destroy");
    }

    @Override
    public void afterPropertiesSet() {
        LOGGER.info("执行：afterPropertiesSet");
    }
}