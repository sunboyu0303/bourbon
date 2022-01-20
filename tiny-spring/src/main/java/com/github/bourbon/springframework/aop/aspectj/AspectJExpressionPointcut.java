package com.github.bourbon.springframework.aop.aspectj;

import com.github.bourbon.base.utils.SetUtils;
import com.github.bourbon.springframework.aop.ClassFilter;
import com.github.bourbon.springframework.aop.MethodMatcher;
import com.github.bourbon.springframework.aop.Pointcut;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 15:46
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {

    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = SetUtils.newHashSet(PointcutPrimitive.EXECUTION);

    private final PointcutExpression pointcutExpression;

    public AspectJExpressionPointcut(String expression) {
        pointcutExpression = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, getClass().getClassLoader()).parsePointcutExpression(expression);
    }

    @Override
    public boolean matches(Class<?> c) {
        return pointcutExpression.couldMatchJoinPointsInType(c);
    }

    @Override
    public boolean matches(Method m, Class<?> c) {
        return pointcutExpression.matchesMethodExecution(m).alwaysMatches();
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
}