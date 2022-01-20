package com.github.bourbon.springframework.context;

import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.beans.factory.Aware;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/6 15:18
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}