package com.github.bourbon.springframework.boot.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author sunboyu
 * @version 1.0
 * @date 2021/12/15 17:48
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnBeanCondition.class)
public @interface ConditionalOnMissingBean {

    Class<?>[] value() default {};

    String[] type() default {};

    Class<?>[] ignored() default {};

    String[] ignoredType() default {};

    Class<? extends Annotation>[] annotation() default {};

    String[] name() default {};

    SearchStrategy search() default SearchStrategy.ALL;

    Class<?>[] parameterizedContainer() default {};
}