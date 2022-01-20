package com.github.bourbon.springframework.aop;

import com.github.bourbon.base.lang.Clock;
import com.github.bourbon.base.lang.SystemClock;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.springframework.aop.aspectj.AspectJExpressionPointcut;
import com.github.bourbon.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.github.bourbon.springframework.aop.bean.IUserService;
import com.github.bourbon.springframework.aop.bean.UserService;
import com.github.bourbon.springframework.aop.bean.UserServiceBeforeAdvice;
import com.github.bourbon.springframework.aop.bean.UserServiceInterceptor;
import com.github.bourbon.springframework.aop.framework.Cglib2AopProxy;
import com.github.bourbon.springframework.aop.framework.JdkDynamicAopProxy;
import com.github.bourbon.springframework.aop.framework.ProxyFactory;
import com.github.bourbon.springframework.aop.framework.ReflectiveMethodInvocation;
import com.github.bourbon.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import com.github.bourbon.springframework.context.support.ClassPathXmlApplicationContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/7 15:54
 */
public class AopTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopTest.class);

    @Test
    public void test_pointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* com.github.bourbon.springframework.aop.bean.UserService.*(..))");
        Class<UserService> clazz = UserService.class;
        Assert.assertTrue(pointcut.matches(clazz));
        Assert.assertTrue(pointcut.matches(clazz.getDeclaredMethod("queryUserInfo"), clazz));
    }

    @Test
    public void test_dynamic() {
        IUserService userService = new UserService();

        AdvisedSupport advisedSupport = new AdvisedSupport()
                .setTargetSource(new TargetSource(userService))
                .setMethodInterceptor(new UserServiceInterceptor())
                .setMethodMatcher(new AspectJExpressionPointcut("execution(* com.github.bourbon.springframework.aop.bean.IUserService.*(..))"));

        LOGGER.info(((IUserService) new JdkDynamicAopProxy(advisedSupport).getProxy()).queryUserInfo());
        Assert.assertNotNull(((IUserService) new Cglib2AopProxy(advisedSupport).getProxy()).register("李四"));
    }

    @Test
    public void test_proxy_class() {
        IUserService userService = (IUserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IUserService.class}, (proxy, method, args) -> "你被代理了！");
        Assert.assertNotNull(userService);
        LOGGER.info(userService.queryUserInfo());
    }

    @Test
    public void test_proxy_method() {
        Object targetObj = new UserService();

        IUserService proxy = (IUserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), targetObj.getClass().getInterfaces(), new InvocationHandler() {
            MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* com.github.bourbon.springframework.aop.bean.IUserService.*(..))");

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (methodMatcher.matches(method, targetObj.getClass())) {
                    MethodInterceptor methodInterceptor = invocation -> {
                        long start = Clock.currentTimeMillis();
                        try {
                            return invocation.proceed();
                        } finally {
                            LOGGER.info("监控 - Begin By AOP");
                            LOGGER.info("方法名称：" + invocation.getMethod().getName());
                            LOGGER.info("方法耗时：" + (Clock.currentTimeMillis() - start) + " ms");
                            LOGGER.info("监控 - End\r\n");
                        }
                    };
                    return methodInterceptor.invoke(new ReflectiveMethodInvocation(targetObj, method, args));
                }
                return method.invoke(targetObj, args);
            }
        });
        Assert.assertNotNull(proxy.queryUserInfo());
    }

    private AdvisedSupport advisedSupport;

    @Before
    public void init() {
        advisedSupport = new AdvisedSupport()
                .setTargetSource(new TargetSource(new UserService()))
                .setMethodInterceptor(new UserServiceInterceptor())
                .setMethodMatcher(new AspectJExpressionPointcut("execution(* com.github.bourbon.springframework.aop.bean.IUserService.*(..))"));
        Clock.currentTimeMillis();
        SystemClock.currentTimeMillis();
    }

    @Test
    public void test_proxyFactory() {
        advisedSupport.setProxyTargetClass(false);
        IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();
        Assert.assertNotNull(proxy.queryUserInfo());
    }

    @Test
    public void test_beforeAdvice() {
        advisedSupport.setMethodInterceptor(new MethodBeforeAdviceInterceptor(new UserServiceBeforeAdvice()));
        IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();
        Assert.assertNotNull(proxy.queryUserInfo());
    }

    @Test
    public void test_advisor() {
        IUserService userService = new UserService();

        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor()
                .setExpression("execution(* com.github.bourbon.springframework.aop.bean.IUserService.*(..))")
                .setAdvice(new MethodBeforeAdviceInterceptor(new UserServiceBeforeAdvice()));

        if (advisor.getPointcut().getClassFilter().matches(userService.getClass())) {
            AdvisedSupport advisedSupport = new AdvisedSupport()
                    .setTargetSource(new TargetSource(userService))
                    .setMethodInterceptor((MethodInterceptor) advisor.getAdvice())
                    .setMethodMatcher(advisor.getPointcut().getMethodMatcher())
                    .setProxyTargetClass(true);

            IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();
            Assert.assertNotNull(proxy.queryUserInfo());
        }
    }

    @Test
    public void test_aop() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:springAop.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        Assert.assertNotNull(userService.queryUserInfo());
    }

    @Test
    public void test_proxy() {
        Object targetObj = new UserService();
        IUserService proxy = (IUserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), targetObj.getClass().getInterfaces(), new InvocationHandler() {
            MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* com.github.bourbon.springframework.aop.bean.IUserService.*(..))");

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (methodMatcher.matches(method, targetObj.getClass())) {
                    MethodInterceptor methodInterceptor = invocation -> {
                        long start = SystemClock.currentTimeMillis();
                        long start1 = Clock.currentTimeMillis();
                        long start2 = System.currentTimeMillis();
                        try {
                            return invocation.proceed();
                        } finally {
                            LOGGER.info("监控 - Begin By AOP");
                            LOGGER.info("方法名称：" + invocation.getMethod().getName());
                            LOGGER.info("方法耗时：" + (SystemClock.currentTimeMillis() - start) + " ms");
                            LOGGER.info("方法耗时1：" + (Clock.currentTimeMillis() - start1) + " ms");
                            LOGGER.info("方法耗时2：" + (System.currentTimeMillis() - start2) + " ms");
                            LOGGER.info("监控 - End\r\n");
                        }
                    };
                    return methodInterceptor.invoke(new ReflectiveMethodInvocation(targetObj, method, args));
                }
                return method.invoke(targetObj, args);
            }
        });
        Assert.assertNotNull(proxy.queryUserInfo());
    }
}