package com.github.bourbon.springframework.aop;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 17:09
 */
public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();
}