package com.github.bourbon.springframework.context;

import com.github.bourbon.base.convert.Converter;
import com.github.bourbon.base.convert.impl.StringToIntegerConverter;
import com.github.bourbon.base.logger.Logger;
import com.github.bourbon.base.logger.LoggerFactory;
import com.github.bourbon.springframework.beans.BeansException;
import com.github.bourbon.springframework.context.bean.IUserService;
import com.github.bourbon.springframework.context.bean.circle.Husband;
import com.github.bourbon.springframework.context.bean.circle.Wife;
import com.github.bourbon.springframework.context.support.ClassPathXmlApplicationContext;
import com.github.bourbon.springframework.core.convert.support.StringToNumberConverterFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/8 14:48
 */
public class ContextTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextTest.class);

    @Test
    public void test_scan() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-scan.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        LOGGER.info("测试结果：" + userService.queryUserInfo());
        Assert.assertNotNull(userService.queryUserInfo());
    }

    @Test
    public void test_property() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-property.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        LOGGER.info("测试结果：" + userService);
        Assert.assertNotNull(userService);
    }

    @Test
    public void test_value() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-value.xml");
        IUserService userService = applicationContext.getBean("userInjectService", IUserService.class);
        LOGGER.info("测试结果：" + userService);
        Assert.assertThrows(BeansException.class, () -> applicationContext.getBean(IUserService.class));
    }

    @Test
    public void test_circular() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-circle.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        Wife wife = applicationContext.getBean("wife", Wife.class);
        Assert.assertNotNull(husband);
        LOGGER.info("老公的媳妇：" + husband.queryWife());
        Assert.assertNotNull(wife);
        LOGGER.info("媳妇的老公：" + wife.queryHusband());
    }

    @Test
    public void test_convert() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-convert.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        Assert.assertNotNull(husband);
        LOGGER.info("测试结果：" + husband);
    }

    @Test
    public void test_StringToIntegerConverter() {
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer num = converter.convert("1234", 123);
        LOGGER.info("测试结果：" + num);
        Assert.assertNotEquals(num.longValue(), 123L);
    }

    @Test
    public void test_StringToNumberConverterFactory() {
        StringToNumberConverterFactory converterFactory = new StringToNumberConverterFactory();

        Converter<String, Integer> stringToIntegerConverter = converterFactory.getConverter(Integer.class);
        LOGGER.info("测试结果：" + stringToIntegerConverter.convert("1234", 123));
        Assert.assertNotEquals(stringToIntegerConverter.convert("1234", 123).longValue(), 123L);

        Converter<String, Long> stringToLongConverter = converterFactory.getConverter(Long.class);
        LOGGER.info("测试结果：" + stringToLongConverter.convert("1234", 123L));
        Assert.assertNotEquals(stringToLongConverter.convert("1234", 123L).longValue(), 123L);
    }
}