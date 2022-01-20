package com.github.bourbon.springframework.aop.aspectj;

import com.github.bourbon.springframework.aop.Pointcut;
import com.github.bourbon.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 17:16
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    private AspectJExpressionPointcut pointcut;

    private Advice advice;

    private String expression;

    @Override
    public Pointcut getPointcut() {
        if (null == pointcut) {
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    public AspectJExpressionPointcutAdvisor setAdvice(Advice advice) {
        this.advice = advice;
        return this;
    }

    public AspectJExpressionPointcutAdvisor setExpression(String expression) {
        this.expression = expression;
        return this;
    }
}